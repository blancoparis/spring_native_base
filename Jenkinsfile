pipeline{
    //agent { docker { image 'gradle:8.6-jdk21-alpine' } }
    agent any
    stages {
        stage('Version') {
            steps{
               sh 'java --version'
               sh './gradlew -version'
               sh "docker --version"
               sh "docker-compose --version"

            }
        }
        stage('Build'){
            steps{
                sh './gradlew clean bootBuildImage'
            }
        }
        stage('Desplegar'){
            steps{
                sh 'docker compose --project-directory ./infra up --detach'
            }
        }
        stage('Test end to end'){
            steps{
                sh './gradlew integracionTest'
            }
        }
        stage('desmontar'){
            steps{
                sh 'ls -la'
                //sh 'docker compose stop'
            }
        }
    }
}