pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS = credentials('dockerhub-creds')
        SSH_KEY = credentials('docker-ssh-key')
        DOCKER_HOST = 'ubuntu@16.171.239.65'
        DOCKER_IMAGE_NAME = 'jenkins-image'
        DOCKER_REPO = 'ewanedon/jenkins'
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[url: 'https://github.com/DobreTech-Repo/Lab1.git']]
                ])
            }
        }

        stage('Build WAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Add Docker Host to Known Hosts') {
            steps {
                sh '''
                mkdir -p ~/.ssh
                ssh-keyscan -H 16.171.239.65 >> ~/.ssh/known_hosts
                '''
            }
        }

        stage('Transfer Files to Docker Host') {
            steps {
                sshagent (credentials: ['docker-ssh-key']) {
                    sh """
                    scp Dockerfile target/myapp.war ${DOCKER_HOST}:/home/ubuntu/
                    """
                }
            }
        }

        stage('Build Docker Image on Docker Host') {
            steps {
                sshagent (credentials: ['docker-ssh-key']) {
                    sh """
                    ssh ${DOCKER_HOST} "docker build -t ${DOCKER_IMAGE_NAME} /home/ubuntu"
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                    echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                    docker tag ${DOCKER_IMAGE_NAME} ${DOCKER_REPO}:${BUILD_ID}
                    docker push ${DOCKER_REPO}:${BUILD_ID}
                    """
                }
            }
        }

        stage('Deploy Docker Container') {
            steps {
                sshagent (credentials: ['docker-ssh-key']) {
                    sh """
                    ssh ${DOCKER_HOST} "docker run -d -p 8080:8080 ${DOCKER_REPO}:${BUILD_ID}"
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
