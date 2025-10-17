pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Clean Workspace') {
            steps {
                echo 'Cleaning previous results...'
                bat 'rd /s /q allure-results || echo "No old results found"'
                bat 'rd /s /q allure-report || echo "No old report found"'
            }
        }

        stage('Build & Test') {
            steps {
                echo 'Building and running tests...'
                bat 'mvn clean test'
            }
            post {
                always {
                    echo 'Archiving test results...'
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Allure Report') {
            steps {
                echo 'Generating Allure Report...'
                allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
            }
        }
    }

    post {
        always {
            echo 'Pipeline complete!'
        }
    }
}
