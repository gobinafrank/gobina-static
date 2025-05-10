pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS = credentials('docker-credentials')
        SSH_KEY = credentials('docker-ssh-key')
        DOCKER_HOST = 'ubuntu@16.171.239.65'
        DOCKER_PORT = '22'
        DOCKER_IMAGE_NAME = 'jenkins-image'
        DOCKER_REPO = 'ewanedon/jenkins'
    }

    stages {
        stage('Checkout SCM') {
            steps {
                script {
                    // Ensure we're checking out from the correct branch
                    checkout([$class: 'GitSCM',
                              branches: [[name: '*/main']],
                              userRemoteConfigs: [[url: 'https://github.com/DobreTech-Repo/Lab1.git']]
                    ])
                }
            }
        }

        stage('Build WAR') {
            steps {
                script {
                    // Build the WAR file using Maven
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Add Docker Host to Known Hosts') {
            steps {
                script {
                    // Adding Docker host to known hosts
                    sh 'ssh-keyscan -H 16.171.239.65 >> ~/.ssh/known_hosts'
                }
            }
        }

        stage('Transfer Files to Docker Host') {
            steps {
                script {
                    // Transfer the Dockerfile and WAR file to the Docker host
                    sh """
                    scp -i ${SSH_KEY} Dockerfile target/myapp.war ${DOCKER_HOST}:/home/ubuntu/
                    """
                }
            }
        }

        stage('Build Docker Image on Docker Host') {
            steps {
                script {
                    // Build Docker image on the remote host
                    sh """
                    ssh -i ${SSH_KEY} ${DOCKER_HOST} "docker build -t ${DOCKER_IMAGE_NAME} /home/ubuntu"
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    // Log in to Docker Hub
                    withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                        echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                        docker tag ${DOCKER_IMAGE_NAME} ${DOCKER_REPO}:${BUILD_ID}
                        docker push ${DOCKER_REPO}:${BUILD_ID}
                        """
                    }
                }
            }
        }

        stage('Deploy Docker Container') {
            steps {
                script {
                    // Deploy Docker container on the Docker host
                    sh """
                    ssh -i ${SSH_KEY} ${DOCKER_HOST} "docker run -d -p 8080:8080 ${DOCKER_REPO}:${BUILD_ID}"
                    """
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
