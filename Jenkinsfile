pipeline {
    agent any

    environment {
        DOCKER_HOST = '16.171.239.65'
        SSH_KEY_ID = 'docker-host-key' // Jenkins SSH key ID
        DOCKER_IMAGE = 'dobretech/myapp'
        DOCKER_CREDENTIALS = credentials('dockerhub-creds')
    }

    stages {
        stage('Clone Code') {
            steps {
                git branch: 'main', url: 'https://github.com/DobreTech-Repo/Lab1.git'
            }
        }

        stage('Build WAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Transfer Files to Docker Host') {
            steps {
                sshagent (credentials: [env.SSH_KEY_ID]) {
                    sh """
                        scp Dockerfile target/*.war ubuntu@${DOCKER_HOST}:/home/ubuntu/
                        ssh ubuntu@${DOCKER_HOST} '
                            mkdir -p ~/tomcat-app &&
                            mv *.war Dockerfile ~/tomcat-app/
                        '
                    """
                }
            }
        }

        stage('Build Docker Image on Docker Host') {
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

        stage('Deploy Docker Container') {
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
            echo '✅ Deployment successful! Visit: http://16.171.239.65:8080/your-app-path/'
        }
        failure {
            echo '❌ Deployment failed. Check console output for errors.'
        }
    }
}
