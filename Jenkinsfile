pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'JDK 8'
    }
    stages {
        // Run unit test in all cases
        stage('Unit Test') {
            echo BRANCH_NAME
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
            jacoco()
            recordIssues(tools: [checkStyle(), findBugs(useRankAsPriority: true), pmdParser()])
        }

        // We only upload development and master branch to artifactoy
        stage('Artifactory configuration') {
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
                withSonarQubeEnv('HTWG SonarQube') {
                    rtMavenRun(
                            tool: "Maven", // Tool name from Jenkins configuration
                            pom: 'pom.xml',
                            goals: 'clean install site sonar:sonar',
                            deployerId: "MAVEN_DEPLOYER",
                            resolverId: "MAVEN_RESOLVER"
                    )
                }
                jacoco()
            }
            post {
                always {
                    junit 'target/failsafe-reports/**/*.xml'
                }
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Publish build info') {
            when {
                anyOf {
                    branch 'master'
                    branch 'development'
                }
            }
            steps {
                rtPublishBuildInfo(
                        serverId: "ARTIFACTORY_SERVER"
                )
            }
        }
    }
}