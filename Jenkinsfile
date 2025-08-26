pipeline {
    agent any

    tools {
        maven 'M3'  // Jenkins tool configuration name
    }

    environment {
        ANDROID_HOME = "/Users/rohitsharma/Library/Android/sdk"
        PATH = "/usr/local/bin:${env.ANDROID_HOME}/platform-tools:${env.ANDROID_HOME}/tools:${env.PATH}"
    }
parameters {
        string(name: 'TAGS', defaultValue: '@Web or @Mobile', description: 'Cucumber tags to execute')
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
