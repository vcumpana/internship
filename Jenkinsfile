pipeline {
   agent {
        node {
            label 'Slave03'
        }
    }
    
    stages {
        stage('Install') {
             steps {
                sh 'mvn clean install -DskipTests'
                script {
                    version = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" ')
                 }
                 echo 'target/service_system-' + version.trim() + '.jar'
                 script {
                    version2 = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" | sed "s/-SNAPSHOT//g"')
                 }
                  echo version2.trim()
            }
        }

        stage('Jacoco Code Coverage') {
            steps {
                jacoco execPattern: '**/build/**.exec'
            }
        }
        stage('Sonar scan') {
            steps {
                withSonarQubeEnv('New Sonar Endava') {
                    sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
                }
            }
        }

        stage('Upload artifact') {
           steps {
               nexusArtifactUploader artifacts: [[artifactId: 'service_system', classifier: '', file: 'target/service_system-' + version.trim() + '.jar', type: 'jar']], 
               credentialsId: '9d977555-9613-4485-8c0c-a25b72a316e3', 
               groupId: 'com.endava', 
               nexusUrl: 'nexus.endava.net', 
               nexusVersion: 'nexus3', 
               protocol: 'https', 
               repository: 'Intens_2018_second', 
               version: version2.trim() + ${env.BUILD_NUMBER}
           }
        }
        
        }     
}