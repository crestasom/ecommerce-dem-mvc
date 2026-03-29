pipeline {

    agent any

     tools {
         jdk 'JDK17'
        maven 'MAVEN'
    }

    environment {
        APP_NAME = "ecommerce-demo-1.0-SNAPSHOT"
        RELEASE_DEPLOY_PATH = "D:\\deployments\\prod"
        REPO = "https://github.com/crestasom/ecommerce-dem-mvc.git"
    }

  
    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: 'github-creds',
                    url: "${REPO}"
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {

            steps {
                withSonarQubeEnv('Sonar Local') {

                    bat '''
                    mvn sonar:sonar ^
                      -Dsonar.projectKey=ecommerce-demo ^
                      -Dsonar.projectName=ecommerce-demo ^
                      -Dsonar.java.binaries=target/classes
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }


        stage('Deploy Release') {

            steps {

                bat """
               // mkdir ${RELEASE_DEPLOY_PATH} 2>nul
                copy target\\${APP_NAME} ${RELEASE_DEPLOY_PATH}
                """

                bat """
                cd ${RELEASE_DEPLOY_PATH}
                taskkill /IM java.exe /F || exit 0
                start java -jar ${APP_NAME} --server.port=8082
                """
            }
        }

    }
}