pipeline {
    agent any

    tools {
        maven "M3"
    }

    stages {
        stage('Build') {
            steps {
                sh "mvn clean compile"
            }
        }
        stage('UnitTests') {
            steps {
                sh "mvn test"
            }
        }
        stage('IntegrationTests') {
            steps {
                sh "mvn test -Pintegration-tests"
            }
        }
        stage('Deploy') {
            steps {
                sh "mvn clean heroku:deploy"
            }
        }
    }
}
