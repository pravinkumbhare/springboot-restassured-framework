// stages {
//     stage('Build & Test') {
//         steps {
//             sh 'mvn clean verify -Dspring.profiles.active=test'
//         }
//     }
//     stage('Code Quality') {
//         steps {
//             sh 'mvn sonar:sonar'
//         }
//     }
// }

pipeline {
    agent any

//     tools {
//         maven 'Maven-3.9.9'        // Replace with your Maven tool name in Jenkins
//         jdk 'JDK17'                // Replace with your JDK name in Jenkins
//     }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                echo 'Building and running tests...'
                bat 'mvn clean test'  // Use `sh` if running on Linux
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
