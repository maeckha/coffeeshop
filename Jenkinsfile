pipeline { 
    agent any  
     tools { 
        maven 'Maven' 
        jdk 'JDK 8' 
    }
    stages {

        stage ('Unit Test') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true clean test'
            }
             post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
             }
        }

        stage ('Artifactory configuration') {
                when {
                    branch 'master|development'
                }
                steps {
                    rtServer (
                        id: "ARTIFACTORY_SERVER",
                        url: "http://141.37.123.36:8081/artifactory/",
                        credentialsId: 'artifactory_token'
                    )

                    rtMavenDeployer (
                        id: "MAVEN_DEPLOYER",
                        serverId: "ARTIFACTORY_SERVER",
                        releaseRepo: "libs-release-local",
                        snapshotRepo: "libs-snapshot-local"
                    )

                    rtMavenResolver (
                        id: "MAVEN_RESOLVER",
                        serverId: "ARTIFACTORY_SERVER",
                        releaseRepo: "libs-release",
                        snapshotRepo: "libs-snapshot"
                    )
                }
        }

        stage('Build') {
            when {
                branch 'master|development'
            }
            steps {
                withSonarQubeEnv('HTWG SonarQube') {
                    rtMavenRun (
                                    tool: "Maven", // Tool name from Jenkins configuration
                                    pom: 'pom.xml',
                                    goals: 'clean install site sonar:sonar',
                                    deployerId: "MAVEN_DEPLOYER",
                                    resolverId: "MAVEN_RESOLVER"
                                )
                }
                jacoco()
                recordIssues(tools: [checkStyle(),findBugs(useRankAsPriority: true),pmdParser()])
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

        stage ('Publish build info') {
            when {
                branch 'master|development'
            }
            steps {
                        rtPublishBuildInfo (
                            serverId: "ARTIFACTORY_SERVER"
                        )
                    }
        }
    }
}