pipeline {
    agent any

    environment {
        DOCKER_HOST = '16.171.239.65'  // Docker host IP address
        DOCKER_USER = 'ubuntu'         // Docker host username
        DOCKER_PORT = '22'             // Docker host SSH port
        DOCKER_IMAGE_NAME = 'myapp'    // Docker image name
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Clone Code') {
            steps {
                git 'https://github.com/DobreTech-Repo/Lab1.git'
            }
        }

        stage('Build WAR') {
            steps {
                script {
                    // Run Maven to clean, compile, and package the WAR file
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Add Docker Host to Known Hosts') {
            steps {
                script {
                    // Add Docker host to known hosts to prevent SSH verification errors
                    sh '''
                        ssh-keyscan -H ${DOCKER_HOST} >> ~/.ssh/known_hosts
                    '''
                }
            }
        }

        stage('Transfer Files to Docker Host') {
            steps {
                sshagent(['ubuntu']) {
                    // Transfer the WAR file and Dockerfile to the Docker host
                    sh 'scp Dockerfile target/myapp.war ${DOCKER_USER}@${DOCKER_HOST}:/home/ubuntu/'
                }
            }
        }

        stage('Build Docker Image on Docker Host') {
            steps {
                sshagent(['ubuntu']) {
                    // SSH into Docker host and build the Docker image
                    sh '''
                        ssh ${DOCKER_USER}@${DOCKER_HOST} "
                            docker build -t ${DOCKER_IMAGE_NAME} /home/ubuntu
                        "
                    '''
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                sshagent(['ubuntu']) {
                    // Push the Docker image to Docker Hub (make sure to log in first)
                    sh '''
                        ssh ${DOCKER_USER}@${DOCKER_HOST} "
                            docker login -u <dockerhub-username> -p <dockerhub-password>
                            docker push ${DOCKER_IMAGE_NAME}
                        "
                    '''
                }
            }
        }

        stage('Deploy Docker Container') {
            steps {
                sshagent(['ubuntu']) {
                    // SSH into Docker host and run the Docker container
                    sh '''
                        ssh ${DOCKER_USER}@${DOCKER_HOST} "
                            docker run -d -p 8080:8080 ${DOCKER_IMAGE_NAME}
                        "
                    '''
                }
            }
        }
    }

    post {
        always {
            // Add a message to indicate that the pipeline has finished
            echo '❌ Deployment failed. Check console output for errors.'
        }
        success {
            // Add a success message for when the pipeline runs successfully
            echo '✅ Deployment successful!'
        }
    }
}
