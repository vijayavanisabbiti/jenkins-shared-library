pipeline {
    agent { node { label 'workstation' } }

    stages {
        stage('Compile') {
            steps {
                echo 'Compile'
            }
        }
        stage('Test cases') {
            steps {
                echo 'Test cases'
            }
        }

        stage('Build') {
            steps {
                echo 'Build'
            }
        }
        stage('Release') {
            steps {
                echo 'Release'
            }
        }
    }
}