stages {
    stage('Build & Test') {
        steps {
            sh 'mvn clean verify -Dspring.profiles.active=test'
        }
    }
    stage('Code Quality') {
        steps {
            sh 'mvn sonar:sonar'
        }
    }
}
