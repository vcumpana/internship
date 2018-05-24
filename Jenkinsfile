pipeline {
   agent {
        node {
            label 'Slave03'
        }
    }
    
    stages {
        stage('Install') {
             steps {
                script {
                    version = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" ')  
                    version = version.trim()
                 }
                 echo 'target/service_system-' + version.trim() + '.war'
                 script {
                    version2 = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" | sed "s/-SNAPSHOT//g"')
                 }

                sh "mvn versions:set -DnewVersion=$version-$env.BUILD_NUMBER"
                sh 'mvn clean install -DskipTests'

                script {
                    version = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" ')
                 }
                 echo 'target/service_system-' + version.trim() + '.war'
                 script {
                    version2 = sh(returnStdout: true, script: 'mvn help:evaluate -Dexpression=project.version | grep -e "^[^[]" | sed "s/-SNAPSHOT//g"')
                 }
                
            }
        }

        stage('Jacoco Code Coverage') {
            steps {
                jacoco execPattern: '**/target/**.exec'
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
               nexusArtifactUploader artifacts: [[artifactId: 'service_system', classifier: '', file: 'target/service_system-' + version.trim() + '.war', type: 'war']], 
               credentialsId: '9d977555-9613-4485-8c0c-a25b72a316e3', 
               groupId: 'com.endava', 
               nexusUrl: 'nexus.endava.net', 
               nexusVersion: 'nexus3', 
               protocol: 'https', 
               repository: 'Intens_2018_second', 
               version: version2.trim()
           }
        }
        
        }     
}

//c