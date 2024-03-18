pipeline{
    //agent { docker { image 'gradle:8.6-jdk21-alpine' } }
    agent any
    stages {
        stage('Version') {
            steps{
               sh 'java --version'
               sh './gradlew -version'
               sh "docker --version"
            }
        }
        stage('Build'){
            steps{
                sh './gradlew clean bootBuildImage'
            }
        }
    }
}