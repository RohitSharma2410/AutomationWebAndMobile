pipeline {
    agent any

    tools {
        maven 'M3'  // Jenkins tool configuration name
    }

    environment {
        ANDROID_HOME = "/Users/rohitsharma/Library/Android/sdk"
        PATH = "/usr/local/bin:${env.ANDROID_HOME}/platform-tools:${env.ANDROID_HOME}/tools:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                sshagent(['4470f16b-442d-4053-a0bf-835a2b08383e']) {
                    git branch: 'main', url: 'git@github.com:RohitSharma2410/AutomationWebAndMobile.git'
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
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Clean Skipped Allure Results') {
            steps {
                sh '''
                mkdir -p clean-allure-results
                for file in allure-results/*.json; do
                    if ! grep -q '"status":"skipped"' "$file"; then
                        cp "$file" clean-allure-results/
                    fi
                done
                '''
            }
        }

        stage('Generate Allure Report') {
            steps {
                allure includeProperties: false,
                       jdk: '',
                       reportBuildPolicy: 'ALWAYS',
                       results: [[path: 'clean-allure-results']]
            }
        }
        stage('Archive Extent Report') {
            steps {
                archiveArtifacts artifacts: 'target/extent-report.html', fingerprint: true
            }
        }

        stage('Publish Extent Report') {
            steps {
                publishHTML(target: [
                    reportName: 'Extent Report',
                    reportDir: 'target',
                    reportFiles: 'extent-report.html',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])
    }

    post {
        always {
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
