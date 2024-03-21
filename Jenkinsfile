pipeline{
    //agent { docker { image 'vegardit/graalvm-maven:latest-java21' } }

    agent any
    stages {
        stage('Version') {
            steps{
               sh 'java --version'
               sh './mvnw -version'
               sh "docker --version"
               sh "docker-compose --version"
            }
        }
        stage('Build'){
            steps{
                sh './mvnw spring-boot:build-image -Pnative'
            }
        }
        stage('Desplegar'){
            agent any //{ docker { image 'alpinelinux/docker-compose:latest' } }
            steps{
                sh 'docker compose -f ./infra/docker-compose.yml -f ./infra/docker-compose.sit.yml  up --detach'
            }
        }
    }
}