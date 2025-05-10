pipeline {
    agent any

    environment {
        DOCKER_HOST = '16.171.239.65'
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
                    // Run Maven to build the WAR file
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Prepare SSH Known Hosts') {
            steps {
                script {
                    // Add Docker host to SSH known hosts
                    sh 'mkdir -p ~/.ssh && ssh-keyscan -H $DOCKER_HOST >> ~/.ssh/known_hosts'
                }
            }
        }

        stage('Copy WAR and Dockerfile to Docker Host') {
            steps {
                sshagent(['docker-ssh-key']) {
                    script {
                        // Copy WAR file and Dockerfile to the Docker host
                        sh "scp target/myapp.war Dockerfile $SSH_USER@$DOCKER_HOST:/home/ubuntu/"
                    }
                }
            }
        }

        stage('Build Docker Image on Host') {
            steps {
                sshagent(['docker-ssh-key']) {
                    script {
                        // Diagnostic: Check Docker version and status
                        sh """
                            ssh $SSH_USER@$DOCKER_HOST '
                            docker --version
                            docker info
                        '
                        """
                        // Build Docker image on the remote host
                        sh """
                            ssh $SSH_USER@$DOCKER_HOST '
                            cd /home/ubuntu &&
                            docker build --no-cache -t $IMAGE_NAME .'
                        """
                    }
                }
            }
        }

        stage('Deploy Container') {
            steps {
                sshagent(['docker-ssh-key']) {
                    script {
                        // Run the Docker container on the Docker host
                        sh """
                            ssh $SSH_USER@$DOCKER_HOST '
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
            echo "Deployment succeeded!"
        }
        failure {
            echo "❌ Deployment failed. Check the logs."
        }
    }
}
