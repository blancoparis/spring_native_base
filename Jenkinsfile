pipeline{
   // agent { docker { image 'gradle:jdk21-graal-jammy' } }
    agent any
    stages {
        stage('Version') {
            steps{
               sh './gradlew -version'
               sh 'docker --version'
            }

        }
    }
}