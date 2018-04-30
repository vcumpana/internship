pipeline {
    agent any
    stages {
        
        stage("Tests") {
             steps {
                sh 'mvn test'
            }
        }
        stage("Install") {
             steps {
                sh 'mvn install -DskipTests'
            }
        }
    }
}