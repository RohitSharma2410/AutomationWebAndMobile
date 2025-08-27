pipeline {
    agent any  // or agent { label 'docker-enabled' }
    
    environment {
        SELENIUM_GRID_URL = 'http://selenium-hub:4444'
        ANDROID_HOME = "/Users/rohitsharma/Library/Android/sdk"
        PATH = "/usr/local/bin:${env.ANDROID_HOME}/platform-tools:${env.ANDROID_HOME}/tools:${env.PATH}"
    }

    tools {
        maven 'M3'
        allure 'allure'
    }

    parameters {
        string(name: 'TAGS', defaultValue: '@Web or @Mobile', description: 'Cucumber tags to execute')
    }

    stages {
        stage('Checkout') {
            steps {
                sshagent(['4470f16b-442d-4053-a0bf-835a2b08383e']) {
                    checkout scm
                }
            }
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

                // Wait for Appium readiness, if needed
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

        stage('Generate Report & Cleanup') {
            steps {
                allure includeProperties: false,
                       jdk: '',
                       reportBuildPolicy: 'ALWAYS',
                       commandline: 'allure',
                       results: [[path: 'allure-results']]

                dir('appium/docker') {
                    sh 'docker-compose down || true'
                }
            }
        }
    }

    post {
        failure {
            echo 'Build or tests failed!'
        }
        success {
            echo 'Build and tests succeeded!'
        }
    }
}
