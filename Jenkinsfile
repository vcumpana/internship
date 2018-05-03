pipeline {
    agent any
    stages {
        stage('Install') {
             steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Upload artifact') {
           steps {
               nexusArtifactUploader artifacts: [[artifactId: 'service_system', classifier: '', file: 'target/service_system-0.0.1-SNAPSHOT.jar', type: 'jar']], 
               credentialsId: '9d977555-9613-4485-8c0c-a25b72a316e3', 
               groupId: 'com.endava', 
               nexusUrl: 'nexus.endava.net', 
               nexusVersion: 'nexus3', 
               protocol: 'https', 
               repository: 'Intens_2018_second', 
               version: '0.0.1'
           }
        }

        stage('Sonar scan') {
            steps {
                withSonarQubeEnv('New Sonar Endava') {
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
                }
            }
        }    
            
    } 
}