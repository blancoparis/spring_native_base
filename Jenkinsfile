pipeline{
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
                sh "./mvnw clean spring-boot:build-image -Pnative -P${ENTORNO} "
            }
        }
        stage('Publicar'){
            steps{
                sh "docker compose -f ./infra/docker-compose.yml -f ./infra/docker-compose.${ENTORNO}.yml  push"
            }
        }
        stage('Recuperar imagen'){
            steps{
                sh "docker -H ssh://pdb@192.168.1.42 pull dblanco80/spring-native-base:latest"
            }
        }
        stage('Desplegar'){
            steps{
                script{
                    sh "docker -H ssh://pdb@192.168.1.42 compose -f ./infra/docker-compose.yml -f ./infra/docker-compose.${ENTORNO}.yml  up --detach"
                }
            }
        }
    }
}