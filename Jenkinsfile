pipeline{
    agent { docker { image 'gradle:jdk21-graal-jammy' } }
    stages {
        stage('Version') {
            steps{
               sh 'gradle -version'
            }
        }
    }
}