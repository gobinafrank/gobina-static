pipeline {
    agent any

    environment {
        DOCKER_HOST = '16.171.239.65'
        SSH_KEY_ID = 'docker-host-key'                // SSH private key for Docker host
        DOCKER_IMAGE = 'dobretech/myapp'              // Your Docker Hub image
        DOCKER_CREDENTIALS = credentials('dockerhub-creds') // Jenkins credential ID: dockerhub-creds
    }

    stages {
        stage('Clone Code') {
            steps {
                git 'https://github.com/DobreTech-Repo/Lab1.git'
            }
        }

        stage('Build WAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Transfer to Docker Host') {
            steps {
                sshagent (credentials: [env.SSH_KEY_ID]) {
                    sh """
                        scp Dockerfile target/myapp.war ubuntu@${DOCKER_HOST}:/home/ubuntu/
                        ssh ubuntu@${DOCKER_HOST} '
                            mkdir -p ~/tomcat-app &&
                            mv Dockerfile myapp.war ~/tomcat-app/
                        '
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sshagent (credentials: [env.SSH_KEY_ID]) {
                    sh """
                        ssh ubuntu@${DOCKER_HOST} '
                            cd ~/tomcat-app &&
                            docker build -t ${DOCKER_IMAGE} .
                        '
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                sshagent (credentials: [env.SSH_KEY_ID]) {
                    sh """
                        ssh ubuntu@${DOCKER_HOST} '
                            echo "${DOCKER_CREDENTIALS_PSW}" | docker login -u "${DOCKER_CREDENTIALS_USR}" --password-stdin &&
                            docker push ${DOCKER_IMAGE}
                        '
                    """
                }
            }
        }

        stage('Deploy to Docker') {
            steps {
                sshagent (credentials: [env.SSH_KEY_ID]) {
                    sh """
                        ssh ubuntu@${DOCKER_HOST} '
                            docker rm -f tomcat-container || true &&
                            docker run -d --name tomcat-container -p 8080:8080 ${DOCKER_IMAGE}
                        '
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Deployment successful. App should be available at: http://16.171.239.65:8080/myapp/'
        }
        failure {
            echo '❌ Pipeline failed. Check logs for details.'
        }
    }
}
