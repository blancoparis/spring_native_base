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
                script{
                    try{
                        sh './gradlew integracionTest'
                    }finally{
                        sh 'docker compose --project-directory ./infra up --detach'
                        junit '**/build/test-results/**/*.xml' //make the junit test results available in any case (success & failure)
                    }
                }
            }
        }

    }
}