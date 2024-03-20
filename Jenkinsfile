pipeline{
    agent { docker { image 'vegardit/graalvm-maven:latest-java21' } }

 //   agent any
    stages {
        stage('Version') {
            steps{
               sh 'java --version'
               sh 'mvn -version'
               sh "docker --version"
            }
        }
        stage('Build'){
            steps{
                sh 'mvn spring-boot:build-image -Pnative'
            }
        }


    }
}