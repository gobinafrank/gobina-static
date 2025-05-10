pipeline {
    agent any
    environment {
        DOCKER_HOST = '16.171.239.65'
        SSH_USER = 'ubuntu'
        IMAGE_NAME = 'jenkins-image'
    }
    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build WAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Prepare Docker Host') {
            steps {
                sh '''
                    mkdir -p ~/.ssh
                    ssh-keyscan -H $DOCKER_HOST >> ~/.ssh/known_hosts
                '''
            }
        }

        stage('Transfer Files to Docker Host') {
            steps {
                sshagent(['ubuntu']) {
                    sh '''
                        scp target/myapp.war Dockerfile $SSH_USER@$DOCKER_HOST:/home/ubuntu/
                    '''
                }
            }
        }

        stage('Build Docker Image on Docker Host') {
            steps {
                sshagent(['ubuntu']) {
                    sh '''
                        ssh $SSH_USER@$DOCKER_HOST \
                        'cd /home/ubuntu && docker build -t $IMAGE_NAME .'
                    '''
                }
            }
        }

        stage('Push to Docker Hub') {
            when {
                expression { return false } // Only enable if you're pushing to DockerHub
            }
            steps {
                // docker login + docker push commands go here
            }
        }

        stage('Deploy Docker Container') {
            steps {
                sshagent(['ubuntu']) {
                    sh '''
                        ssh $SSH_USER@$DOCKER_HOST '
                        docker rm -f webapp || true &&
                        docker run -d --name webapp -p 8080:8080 $IMAGE_NAME
                        '
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful!"
        }
        failure {
            echo "❌ Deployment failed. Check console output for errors."
        }
    }
}
