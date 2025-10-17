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

                // 👇 Retain previous history for trend charts
                script {
                    if (fileExists('allure-report/history')) {
                        echo 'Copying previous Allure history...'
                        bat 'xcopy /E /I /Y allure-report\\history allure-results\\history'
                    }
                }

                // 👇 Generate Allure report
                allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
            }
        }
    }

    post {
        success {
            echo '✅ Build succeeded — Allure report published!'
        }
        always {
            echo 'Pipeline complete!'
        }
    }
}
