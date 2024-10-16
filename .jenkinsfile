pipeline {
    agent any

    stages {
        stage('SCM Checkout') {
            steps {
                // Clona o repositório definido no Jenkinsfile
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: '*/main']], 
                    userRemoteConfigs: [[url: 'https://github.com/davidsferreira02/M1A_1240444_1170605.git']]
                ])
            }
        }
        stage('Build') {
            steps {
                sh 'echo "Building..."'
                sh 'mvn clean install'
            }
        }
        
        stage('Test') {
            steps {
                // Exemplo de comando de teste
                sh 'echo "Running Tests..."'
            }
        }
        
        stage('Deploy') {
            steps {
                // Exemplo de comando de deploy
                sh 'echo "Deploying..."'
            }
        }
    }
}



