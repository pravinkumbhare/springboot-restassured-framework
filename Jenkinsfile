pipeline {
  agent any

  environment {
    SONAR_SERVER_NAME   = 'SonarQube'
    SONAR_ORGANIZATION  = 'pravinkumbhare'
    SONAR_PROJECT_KEY   = 'pravinkumbhare_spring-boot-project-for-api-testing-messages-testing-microservices'
    SONAR_CREDENTIALS_ID = '7a405c30-4852-4e0e-b0d9-c84de38a8e3c'
  }

  stages {
    stage('Checkout') { steps { checkout scm } }

    stage('Clean Workspace') {
      steps {
        echo 'Cleaning previous results...'
        bat 'rd /s /q allure-results || echo No old results found'
        bat 'rd /s /q allure-report || echo No old report found'
      }
    }

    stage('Build & Test') {
      steps {
        echo 'Building and running tests...'
        bat 'mvn -B clean test'
      }
      post {
        always { junit 'target/surefire-reports/*.xml' }
      }
    }

    stage('SonarCloud Analysis') {
      steps {
        echo "Starting SonarCloud analysis for ${env.SONAR_PROJECT_KEY}"

        // Preferred: if you configured Sonar server in Jenkins, use withSonarQubeEnv
        withSonarQubeEnv("${env.SONAR_SERVER_NAME}") {
          bat "mvn -B -Dsonar.organization=${env.SONAR_ORGANIZATION} -Dsonar.projectKey=${env.SONAR_PROJECT_KEY} -Dsonar.host.url=https://sonarcloud.io sonar:sonar"
        }

        // Alternative: if you DID NOT configure Sonar server in Jenkins, uncomment this:
        // withCredentials([string(credentialsId: env.SONAR_CREDENTIALS_ID, variable: 'SONAR_TOKEN')]) {
        //   bat "mvn -B -Dsonar.organization=${env.SONAR_ORGANIZATION} -Dsonar.projectKey=${env.SONAR_PROJECT_KEY} -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=%SONAR_TOKEN% sonar:sonar"
        // }
      }
    }

    stage('Wait For Quality Gate') {
      steps {
        echo 'Waiting for Sonar Quality Gate result...'
        waitForQualityGate abortPipeline: true
      }
    }

    stage('Allure Report') {
      steps {
        echo 'Generating Allure Report...'
        allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]

        script {
          if (isUnix()) {
            sh 'allure generate allure-results --clean -o allure-report || echo "Allure CLI not available"'
          } else {
            bat 'allure generate allure-results --clean -o allure-report || echo Allure CLI not available'
          }
        }
      }
      post {
        always {
          archiveArtifacts artifacts: 'allure-report/**', allowEmptyArchive: true
          echo 'Allure report archived.'
        }
      }
    }
  }

  post {
    success { echo '✅ Build succeeded — Allure & Sonar published!' }
    unsuccessful { echo 'Build finished with problems (UNSTABLE/FAILURE).' }
    always { echo 'Pipeline complete!' }
  }
}
