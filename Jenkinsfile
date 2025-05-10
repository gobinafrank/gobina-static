pipeline {
    agent any

    environment {
        IMAGE_NAME = "dobretech/myapp"
        CONTAINER_NAME = "myapp-container"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git 'https://github.com/DobreTech-Repo/Lab1.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME:latest .'
            }
        }

        stage('Stop Previous Container') {
            steps {
                sh '''
                if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
                    docker stop $CONTAINER_NAME || true
                    docker rm $CONTAINER_NAME || true
                fi
                '''
            }
        }

        stage('Run Docker Container') {
            steps {
                sh 'docker run -d --name $CONTAINER_NAME -p 8888:8080 $IMAGE_NAME:latest'
            }
        }

        stage('Push to Docker Hub (Optional)') {
            when {
                expression { return env.DOCKER_USER != null && env.DOCKER_PASS != null }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $IMAGE_NAME:latest
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployment completed successfully!"
        }
        failure {
            echo "❌ Build failed."
        }
    }
}
