def call() {
    node ('workstation') {
        if(env.TAG_NAME ==~ ".*") {
            env.branchName = env.TAG_NAME
        } else {
            env.branchName = env.BRANCH_NAME
        }
        stage('Code checkout') {
            git branch: env.branchName, credentialsId: 'vijayavani', url: 'https://github.com/vijayavanisabbiti/expense-backend'
        }
        stage('Compile') {}
        if(env.BRANCH_NAME == "main") {
            stage('Build') {}
        } else if(env.BRANCH_NAME ==~ "PR.*") {
            stage('test cases') {}
            stage('Integration Test cases') {}
        } else if(env.TAG_NAME ==~ ".*") {
            stage('Build') {}
            stage('Release App') {}
        } else {
            stage('test cases') {}
        }
        /*    stage('Build') {}
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
        }*/
    }
}

