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
                    def js_package = readJSON file: 'package.json'
                    def version_old = js_package.version.split(".")
                    def version_new = "${version_old[0]}.${version_old[1]}.${env.BUILD_NUMBER}"
                    currentBuild.displayName = "${version_new}"
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
