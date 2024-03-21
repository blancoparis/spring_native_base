pipeline{
    //agent { docker { image 'vegardit/graalvm-maven:latest-java21' } }
    agent any
    environment {
        ENTORNO = credentials('entorno')
    }
    stages {
        stage('Version') {
            steps{
               sh 'java --version'
               sh './mvnw -version'
               sh "docker --version"
               sh "docker-compose --version"
               sh "echo ${ENTORNO}"
            }
        }
        stage('Build'){
            steps{
                sh "./mvnw clean spring-boot:build-image -Pnative -P ${ENTORNO}"
            }
        }
        stage('Desplegar'){
            steps{
                sh "docker compose -f ./infra/docker-compose.yml -f ./infra/docker-compose.${ENTORNO}.yml  up --detach"
            }
        }
    }
}