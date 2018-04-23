import groovy.json.JsonSlurper

def call(body) {

    def config = [:]

    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    dockerNode(dockerImage: 'stakater/frontend-tools:latest') {
        container(name: 'docker') {
            try {
                stage("Checkout") {
                    checkout scm
                    echo new File(".").toString()
                    new File(".").list().each {
                         echo it
                    }
                    def js_package = new JsonSlurper().parse(new File("package.json"))
                    def version_prefix = js_package.version
                    echo "Version ${version_prefix}"
                    currentBuild.displayName = "${version_prefix}" //env.BUILD_NUMBER
                    echo "ok"
                }
//
//                stage("Install") {
//                    withCredentials([[$class  : 'StringBinding', credentialsId: 'NEXUS_NPM_AUTH',
//                                      variable: 'NEXUS_NPM_AUTH']]) {
//                        sh "NEXUS_NPM_AUTH=${NEXUS_NPM_AUTH} yarn version --no-git-tag-version --new-version ${version_prefix}"
//                        sh "NEXUS_NPM_AUTH=${NEXUS_NPM_AUTH} yarn install"
//                    }
//                }
//
//                stage("Lint") {
//                    withCredentials([[$class  : 'StringBinding', credentialsId: 'NEXUS_NPM_AUTH',
//                                      variable: 'NEXUS_NPM_AUTH']]) {
//                        sh "NEXUS_NPM_AUTH=${NEXUS_NPM_AUTH} yarn lint -f junit -o lint-report.xml"
//                    }
//                }
//
//                stage("Test") {
//                    withCredentials([[$class  : 'StringBinding', credentialsId: 'NEXUS_NPM_AUTH',
//                                      variable: 'NEXUS_NPM_AUTH']]) {
//                        sh "NEXUS_NPM_AUTH=${NEXUS_NPM_AUTH} yarn test-ci"
//                    }
//                }
//
//                stage("Tag") {
//
//                }
//
//                stage("Build") {
//                    withCredentials([[$class  : 'StringBinding', credentialsId: 'NEXUS_NPM_AUTH',
//                                      variable: 'NEXUS_NPM_AUTH']]) {
//                        sh "NEXUS_NPM_AUTH=${NEXUS_NPM_AUTH} yarn build"
//                    }
//                }
//
////                stage("Publish to nexus") {
////                    withCredentials([[$class  : 'StringBinding', credentialsId: 'NEXUS_NPM_AUTH',
////                                      variable: 'NEXUS_NPM_AUTH']]) {
////                        sh "NEXUS_NPM_AUTH=${NEXUS_NPM_AUTH} npm publish"
////                    }
////                }
//
//                stage("Deploy") {
//                    s3Upload(file: 'lib/', bucket: '847616476486-microfrontends2', path: "${js_package}/${version_prefix}/")
//                }
            } finally {
//                junit 'lint-report.xml'
//                junit 'junit.xml'
            }
        }
    }
}
