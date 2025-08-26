pipeline {
    agent any

    tools {
        maven 'M3'        // Jenkins Maven tool name
        allure 'allure'   // Jenkins Allure tool name, make sure it matches your Jenkins configuration
    }

    environment {
        ANDROID_HOME = "/Users/rohitsharma/Library/Android/sdk"
        PATH = "/usr/local/bin:${env.ANDROID_HOME}/platform-tools:${env.ANDROID_HOME}/tools:${env.PATH}"
    }

    parameters {
        string(name: 'TAGS', defaultValue: '@Web or @Mobile', description: 'Cucumber tags to execute')
    }

    stages {
       sshagent(['4470f16b-442d-4053-a0bf-835a2b08383e']) {
    checkout([
        $class: 'GitSCM',
        branches: [[name: '*/main']],
        userRemoteConfigs: [[
            url: 'git@github.com:RohitSharma2410/AutomationWebAndMobile.git',
            credentialsId: '4470f16b-442d-4053-a0bf-835a2b08383e'
        ]],
        doGenerateSubmoduleConfigurations: false,
        extensions: [[$class: 'WipeWorkspace']]  // Wipe workspace before checkout
    ])
}


        stage('Clean Reports') {
            steps {
                sh 'rm -rf allure-results allure-report clean-allure-results'
            }
        }

        stage('Start Appium Container') {
            steps {
                dir('appium/docker') {
                    sh 'docker-compose up -d'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def tags = "${params.TAGS}"
                    def parallel = tags.contains("@Mobile") ? "none" : "methods"
                    def threads = tags.contains("@Mobile") ? "1" : "4"

                    sh """
                      mvn clean test \
                      -Dcucumber.filter.tags="${tags}" \
                      -Dparallel.mode="${parallel}" \
                      -Dparallel.threads="${threads}"
                    """
                }
            }
        }

        // Removed Clean Skipped Allure Results stage

    }

    post {
        always {
			   allure includeProperties: false,
               jdk: '',
               reportBuildPolicy: 'ALWAYS',
               commandline: 'allure',
               results: [[path: 'allure-results']]
            echo 'Stopping and cleaning up Docker containers...'
            dir('appium/docker') {
                sh 'docker-compose down || true'
            }
        }
        failure {
            echo 'Build or tests failed!'
        }
        success {
            echo 'Build and tests succeeded!'
        }
    }
}
