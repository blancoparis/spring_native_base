pipeline{
    agent { docker { image 'gradle:8.6-jdk21-alpine' } }
    //agent any
    stages {
        stage('Version') {
            steps{
               sh 'java --version'
               sh 'gradle -version'
    //           sh './gradlew -version'
            }

        }
    }
}