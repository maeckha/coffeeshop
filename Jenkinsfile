pipeline {
    agent any
    options {
        gitLabConnection('in gitlab')
    }
    tools {
        maven 'Maven'
        jdk 'JDK 8'
    }
    stages {
        // Run unit test in all cases
        stage('Unit Test') {
            steps {
                gitlabCommitStatus('Unit Test') {
                    echo BRANCH_NAME
                    sh 'mvn -Dmaven.test.failure.ignore=true clean test site'
                    jacoco()
                    recordIssues(tools: [checkStyle(), findBugs(useRankAsPriority: true), pmdParser()])
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        // Build involves integration tests and upload to artifactory
        stage('Build') {
            when {
                anyOf {
                    branch 'master'
                    branch 'development'
                }
            }
            steps {
                rtServer(
                        id: "ARTIFACTORY_SERVER",
                        url: "http://141.37.123.36:8081/artifactory/",
                        credentialsId: 'artifactory_token'
                )

                rtMavenDeployer(
                        id: "MAVEN_DEPLOYER",
                        serverId: "ARTIFACTORY_SERVER",
                        releaseRepo: "libs-release-local",
                        snapshotRepo: "libs-snapshot-local"
                )

                rtMavenResolver(
                        id: "MAVEN_RESOLVER",
                        serverId: "ARTIFACTORY_SERVER",
                        releaseRepo: "libs-release",
                        snapshotRepo: "libs-snapshot"
                )
                gitlabCommitStatus('Build') {
                    withSonarQubeEnv('HTWG SonarQube') {
                        rtMavenRun(
                                tool: "Maven", // Tool name from Jenkins configuration
                                pom: 'pom.xml',
                                goals: 'clean install site sonar:sonar',
                                deployerId: "MAVEN_DEPLOYER",
                                resolverId: "MAVEN_RESOLVER"
                        )
                    }
                }
            }
            post {
                always {
                    junit 'target/failsafe-reports/**/*.xml'
                }
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    rtPublishBuildInfo(
                            serverId: "ARTIFACTORY_SERVER"
                    )
                }
            }
        }


        stage('Deploy on staging') {
            when {
                anyOf {
                    branch 'master'
                    branch 'development'
                }
            }
            steps {
                gitlabCommitStatus('Deploy on staging') {
                    script {
                        def name = sh script: 'mvn help:evaluate -Dexpression=project.name | grep -v "^\\[" | tr -d \'\\n\'', returnStdout: true
                        def version = sh script: 'mvn help:evaluate -Dexpression=project.version | grep -v "^\\[" | tr -d \'\\n\'', returnStdout: true
                        def filename = name + "-" + version + ".jar"
                        echo filename

                        withCredentials([sshUserPrivateKey(credentialsId: '13e88844-d8e9-46dc-b6d0-196b13b9dc42', keyFileVariable: 'identity', passphraseVariable: 'passphrase', usernameVariable: 'userName')]) {
                            def remote = [:]
                            remote.user = userName
                            remote.identityFile = identity
                            remote.passphrase = passphrase
                            remote.name = '193.196.52.139'
                            remote.host = '193.196.52.139'
                            remote.allowAnyHosts = true
                            sshPut remote: remote, from: "target/" + filename, into: '/opt/coffeeshop'
                            sshCommand remote: remote, command: 'ln -s -f /opt/coffeeshop/' + filename + ' /opt/coffeeshop/shop-ui.jar'
                            sshCommand remote: remote, command: 'systemctl restart coffeeshop.service', sudo: true
                        }
                    }
                }
            }
        }
    }
}