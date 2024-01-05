def call() {
    ansiColor('xterm') {
        node('workstation') {
            common.codeCheckout()

            stage('Terraform Apply') {
                sh 'terraform init'
                sh 'terraform apply -auto-approve'
            }

            stage('Terraform Destroy') {
                sh 'terraform init'
                sh 'terraform state rm aws_ami_from_instance.ami'
                sh 'terraform destroy -auto-approve'
            }
        }
    }
}
