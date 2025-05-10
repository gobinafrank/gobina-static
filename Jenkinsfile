pipeline {
    agent any

    environment {
        DOCKER_HOST = '13.49.80.179'
        SSH_USER = 'ubuntu'
        IMAGE_NAME = 'myapp:latest'
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build WAR') {
            steps {
                script {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Prepare SSH Known Hosts') {
            steps {
                script {
                    sh 'mkdir -p ~/.ssh && ssh-keyscan -H $DOCKER_HOST >> ~/.ssh/known_hosts'
                }
            }
        }

        stage('Copy WAR and Dockerfile to Docker Host') {
            steps {
                sshagent(['docker-ssh-key']) {
                    script {
                        sh "scp target/myapp.war Dockerfile $SSH_USER@$DOCKER_HOST:/home/ubuntu/"
                    }
                }
            }
        }

        stage('Build Docker Image on Host') {
            steps {
                sshagent(['docker-ssh-key']) {
                    script {
                        sh """
                            ssh $SSH_USER@$DOCKER_HOST '
                            cd /home/ubuntu &&
                            docker build --no-cache -t $IMAGE_NAME .'
                        """
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                sshagent(['docker-ssh-key']) {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')]) {
                        script {
                            sh """
                                ssh $SSH_USER@$DOCKER_HOST '
                                echo "$DOCKER_HUB_PASS" | docker login -u "$DOCKER_HUB_USER" --password-stdin &&
                                docker tag $IMAGE_NAME $DOCKER_HUB_USER/$IMAGE_NAME &&
                                docker push $DOCKER_HUB_USER/$IMAGE_NAME'
                            """
                        }
                    }
                }
            }
        }

        stage('Deploy Container') {
            steps {
                sshagent(['docker-ssh-key']) {
                    script {
                        sh """
                            ssh $SSH_USER@$DOCKER_HOST '
                            docker rm -f myapp || true &&
                            docker run -d -p 8080:8080 --name myapp $IMAGE_NAME'
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline finished"
        }
        success {
            echo "✅ Deployment succeeded!"
        }
        failure {
            echo "❌ Deployment failed. Check the logs."
        }
    }
}
