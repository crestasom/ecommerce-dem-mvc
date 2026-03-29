pipeline {

    agent any

    tools {
        jdk 'JDK17'
        maven 'MAVEN'
    }

    environment {
        REPO_URL = 'https://github.com/crestasom/ecommerce-dem-mvc'
        APP_NAME = "ecommerce-demo-1.0-SNAPSHOT.jar"
        DEV_PATH = "D:\\deployment\\dev"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'development',
                    credentialsId: 'github-creds',
                    url: "${REPO_URL}"
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy Dev') {
            steps {
                bat """
                if not exist "${DEV_PATH}" mkdir "${DEV_PATH}"
                copy target\\${APP_NAME} ${DEV_PATH}
                """

                bat """
                cd ${DEV_PATH}
                taskkill /IM java.exe /F || exit 0
                start "" java -jar ${APP_NAME} --server.port=8081
                """
            }
        }
    }
}