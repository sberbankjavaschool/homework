pipeline {
    agent { docker { image 'gradle:5.2' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}