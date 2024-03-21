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
                sh './mvnw clean spring-boot:build-image -Pnative -P sit'
            }
        }
        stage('Desplegar'){
            agent any //{ docker { image 'alpinelinux/docker-compose:latest' } }
            steps{
                sh 'docker compose -f ./infra/docker-compose.yml -f ./infra/docker-compose.sit.yml  up --detach'
            }
        }
        stage('Test end to end'){
            agent any
            steps{
                script{
                    try{
                        sh './mvnw verify -P endToEnd'
                    }finally{
                        sh 'docker compose -f ./infra/docker-compose.yml -f ./infra/docker-compose.sit.yml  down '
                        junit '**/target/surefire-reports/**/*.xml' //make the junit test results available in any case (success & failure)
                    }
                }
            }
        }
    }
}