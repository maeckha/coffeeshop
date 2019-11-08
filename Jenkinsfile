pipeline { 
    agent any  
     tools { 
        maven 'Maven' 
        jdk 'JDK 8' 
    }
    stages { 
        stage('Build') { 
             steps {
                sh 'mvn -Dmaven.test.failure.ignore=true install site' 
                jacoco()
                recordIssues(tools: [findBugs(useRankAsPriority: true)])
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml' 
                }
            }
        }
    }
}