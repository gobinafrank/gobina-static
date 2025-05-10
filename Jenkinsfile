pipeline {
    agent any

    environment {
        DOCKER_HOST = '16.171.239.65'
        SSH_USER = 'ubuntu'
        IMAGE_NAME = 'jenkins-image'
        CONTAINER_NAME = 'myapp'
        WAR_NAME = 'myapp.war'
    }

    stages {
        stage('Checkout Source') {
            steps {
                checkout scm
            }
        }

        stage('Build WAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Rename WAR') {
            steps {
                sh 'mv target/*.war target/${WAR_NAME}'
            }
        }

        stage('Prepare SSH Known Hosts') {
            steps {
                sh '''
                    mkdir -p ~/.ssh
                    ssh-keyscan -H $DOCKER_HOST >> ~/.ssh/known_hosts
                '''
            }
        }

        stage('Copy WAR and Dockerfile to Docker Host') {
            steps {
                sshagent(['ubuntu']) {
                    sh '''
                        scp target/${WAR_NAME} Dockerfile $SSH_USER@$DOCKER_HOST:/home/ubuntu/
                    '''
                }
            }
        }

        stage('Build Docker Image on Host') {
            steps {
                sshagent(['ubuntu']) {
                    sh '''
                        ssh $SSH_USER@$DOCKER_HOST '
                        cd /home/ubuntu &&
                        docker build -t $IMAGE_NAME .
                        '
                    '''
                }
            }
        }

        stage('Deploy Container') {
            steps {
                sshagent(['ubuntu']) {
                    sh '''
                        ssh $SSH_USER@$DOCKER_HOST '
                        docker rm -f $CONTAINER_NAME || true &&
                        docker run -d --name $CONTAINER_NAME -p 8080:8080 $IMAGE_NAME
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
            echo "❌ Deployment failed. Check the logs."
        }
    }
}
