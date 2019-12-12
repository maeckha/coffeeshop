pipeline { 
    agent any  
     tools { 
        maven 'Maven' 
        jdk 'JDK 8' 
    }
    stages {

        stage ('Artifactory configuration') {
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
             steps {
                withSonarQubeEnv('HTWG SonarQube') {
                    rtMavenRun (
                                    tool: "Maven", // Tool name from Jenkins configuration
                                    pom: 'pom.xml',
                                    goals: 'clean install site sonar:sonar',
                                    deployerId: "MAVEN_DEPLOYER",
                                    resolverId: "MAVEN_RESOLVER"
                                )
                    //sh 'mvn -Dmaven.test.failure.ignore=true package site sonar:sonar'
                }
                jacoco()
                recordIssues(tools: [checkStyle(),findBugs(useRankAsPriority: true),pmdParser()])
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    junit 'target/surefire-reports/**/*.xml' 
                }
            }
        }

        stage ('Publish build info') {
                    steps {
                        rtPublishBuildInfo (
                            serverId: "ARTIFACTORY_SERVER"
                        )
                    }
        }
    }
}