pipeline {
    agent any

    stages {
        stage('SCM Checkout') {
            steps {
                // Clona o repositório definido no Jenkinsfile
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: '*/main']], 
                    userRemoteConfigs: [[url: 'https://github.com/usuario/repo.git']]
                ])
            }
        }
        stage('Build') {
            steps {
                // Exemplo de comando de build
                sh 'echo "Building..."'
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



