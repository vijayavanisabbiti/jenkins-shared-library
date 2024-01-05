def call() {
    ansiColor('xterm') {
        node('workstation') {
            common.codeCheckout()

            stage('Terraform Apply') {
                sh 'terraform init'
                sh 'terraform apply -auto-approve'
            }
        }
    }
}
