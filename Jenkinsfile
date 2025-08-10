pipeline {
    agent { label 'default' }

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
                            string(credentialsId: 'sonarqube.dornol.codebox', variable: 'SONAR_PROJECT_KEY'),
                            string(credentialsId: 'sonarqube.dornol.token.codebox', variable: 'SONAR_TOKEN')
                    ]) {
                        sh """
                            chmod +x ./gradlew
                            ./gradlew build sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.token=${SONAR_TOKEN}
                        """
                    }
                }
            }
        }
    }
}
