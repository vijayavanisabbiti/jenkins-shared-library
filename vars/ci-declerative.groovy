def call() {
    pipeline {
        agent {
            node { label 'workstation'}
        }
        stages {

            stage('Compile') {
                expression { BRANCH_NAME == "main" }
                steps {
                    echo 'Compile'
                }
            }

            stage('Test Cases') {
                steps {
                    echo 'Test Cases'
                }
            }

            stage('Integration Test Cases') {
                steps {
                    echo 'Test Cases'
                }
            }

            stage('Build') {
                when {
                    branch 'main'
                }
                steps {
                    echo 'Build'
                }
            }

            stage('Release App') {
                steps {
                    echo 'Release'
                }
            }
        }
    }
}