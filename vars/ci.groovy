def call() {
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

            stage('Integration Test cases') {
                steps {
                    echo 'Test cases'
                }
            }

            stage('Build') {
                steps {
                    echo 'Build'
                }
            }
            stage('Release Application') {
                steps {
                    echo 'Release'
                }
            }
        }
    }
}