pipeline {
    agent any
    stages {
        
        stage("Build") {
             steps {
                sh 'mvn compile'
            }
        }

        stage("Tests") {
             steps {
                sh 'mvn test'
            }
        }

        stage("Deploy") {
             steps {
                sh 'mvn install -DskipTests'
            }
        }
    }
}