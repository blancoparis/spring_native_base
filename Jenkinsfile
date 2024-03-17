pipeline{
    agent { docker { image 'gradle:jdk21-graal' } }
   // agent any
    stages {
        stage('Version') {
            steps{
               sh 'java --version'
               sh 'gradle -version'
            }

        }
    }
}