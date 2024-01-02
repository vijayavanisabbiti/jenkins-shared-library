def call() {

    node ('workstation') {

        sh "find . | sed -e '1d' |xargs rm -rf"

        if (env.TAG_NAME ==~ ".*") {
            env.branch_name = "refs/tags/${env.TAG_NAME}"
        } else {
            if (env.BRANCH_NAME ==~ "PR-.*") {
                env.branch_name = "${env.CHANGE_BRANCH}"
            } else {
                env.branch_name = "${env.BRANCH_NAME}"
            }
        }

        stage('Code Checkout') {
        checkout scmGit(
                branches: [[name: "${branch_name}"]],
                userRemoteConfigs: [[url: "https://github.com/vijayavanisabbiti/${repo_name}"]]
        )
            sh 'cat Jenkinsfile'
        }

        if (app_type == "nodejs") {
            stage('Download Dependencies') {
                sh 'npm install'
            }
        }

        if (env.JOB_BASE_NAME ==~ "PR-.*") {
            sh 'echo PR'
            stage('Test Cases') {}
            stage('Code Quality') {
                sh 'sonar-scanner -Dsonar.host.url=http://3.86.51.6:9000 -Dsonar.login=5ab20fcf75bb8d523de99176f1a46ee86871ff11 -Dsonar.projectKey=expense-backend'
            }
        } else if (env.BRANCH_NAME == "main") {
            sh 'echo main'
            stage('Build') {}
        } else if (env.TAG_NAME ==~ ".*") {
            sh 'echo TAG'
            stage('Build') {}
            stage('Release App') {}
        } else {
            sh 'echo branch'
            stage('Test Cases') {}
            //sh 'npm test'
            //These test cases in organization are ideally written and we don't skip it.
        }

    }
}


