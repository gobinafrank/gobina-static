pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'dobretech/myapp'
        DOCKER_CREDENTIALS = credentials('dockerhub-creds') // username/password
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

        stage('Transfer Files') {
            agent { label 'docker' }
            steps {
                sh '''
                    mkdir -p ~/tomcat-app
                    cp Dockerfile target/myapp.war ~/tomcat-app/
                '''
            }
        }

        stage('Build Docker Image') {
            agent { label 'docker' }
            steps {
                dir('~/tomcat-app') {
                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }

        stage('Push to Docker Hub') {
            agent { label 'docker' }
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}
                    '''
                }
            }
        }

        stage('Deploy to Docker') {
            agent { label 'docker' }
            steps {
                sh '''
                    docker rm -f tomcat-container || true
                    docker run -d --name tomcat-container -p 8080:8080 ${DOCKER_IMAGE}
                '''
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
