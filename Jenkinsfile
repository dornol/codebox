pipeline {
    agent { label 'default' }

    environment {
        MODULE_DIR = 'jpa-typed-id'
        GRADLE_CMD = './gradlew'
        SONAR_TASK = 'sonar'
    }

    stages {
        stage('SCM Checkout') {
            steps {
                checkout scm
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    withCredentials([
                            string(credentialsId: 'sonarqube.dornol.codebox', variable: 'SONAR_PROJECT_KEY')
                    ]) {
                        dir(env.MODULE_DIR) {
                            sh "${GRADLE_CMD} ${SONAR_TASK} -Dsonar.projectKey=${SONAR_PROJECT_KEY}"
                        }
                    }
                }
            }
        }
    }
}
