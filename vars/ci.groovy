def AWS_SSM_PARAM(param_name) {
    def OUTPUT = sh ( script: "aws ssm get-parameter --name sonarqube.token --with-decryption --query 'Parameter.Value' --output text", returnStdout: true ).trim()
    return(OUTPUT)
}

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
            stage('Test Cases') {
                //sh 'npm test'
            }

            stage('Code Quality') {

                env.SONAR_TOKEN = AWS_SSM_PARAM('sonarqube.token')
                wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_TOKEN}", var: 'PASSWORD']]]) {

                    sh 'sonar-scanner -Dsonar.host.url=http://3.86.51.6:9000 -Dsonar.login=${SONAR_TOKEN} -Dsonar.projectKey=${repo_name} -Dsonar.qualitygate.wait=true -Dsonar.exclusions=node_modules/**'
                }
            }

        } else if (env.BRANCH_NAME == "main") {
            sh 'echo main'
            stage('Build') {}
        } else if (env.TAG_NAME ==~ ".*") {
            sh 'echo TAG'
            stage('Build') {

                sh 'zip -r ${repo_name}-${TAG_NAME}.zip *'
            }

            stage('Release App') {

                env.ARTIFACTORY_PASSWORD = AWS_SSM_PARAM('artifactory.password')
                wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${ARTIFACTORY_PASSWORD}", var: 'PASSWORD']]]) {
                    sh 'curl -sSf -u "admin:${ARTIFACTORY_PASSWORD}" -X PUT -T ${repo_name}-${TAG_NAME}.zip http://artifactory.vijayavanimanju.online:8081/artifactory/${repo_name}/${repo_name}-${TAG_NAME}.zip'
                }
            }
        } else {
            sh 'echo branch'
            stage('Test Cases') {}
            //sh 'npm test'
            //These test cases in organization are ideally written and we don't skip it.
        }

    }
}


