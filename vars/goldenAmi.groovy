def call() {
    node('workstation') {
        common.codeCheckout()
    }
}
