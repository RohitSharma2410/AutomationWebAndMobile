pipeline {
    agent any

    tools {
        maven 'M3'        // Jenkins Maven tool name
        allure 'allure'    // Jenkins Allure tool name
    }

    environment {
        SIT_USERS = 'testerrohitsharma@gmail.com'
        UAT_USERS = 'rohitsharmasdet@gmail.com'
        ANDROID_HOME = "/Users/rohitsharma/Library/Android/sdk"
        PATH = "/usr/local/bin:${env.ANDROID_HOME}/platform-tools:${env.ANDROID_HOME}/tools:${env.PATH}"
    }

    parameters {
        string(name: 'TAGS', defaultValue: '@Mobile', description: 'Cucumber tags to execute')
        choice(name: 'ENVIRONMENT', choices: ['SIT', 'UAT'], description: 'Select target environment')
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                sshagent(['4470f16b-442d-4053-a0bf-835a2b08383e']) {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: '*/main']],
                        userRemoteConfigs: [[
                            url: 'git@github.com:RohitSharma2410/AutomationWebAndMobile.git',
                            credentialsId: '4470f16b-442d-4053-a0bf-835a2b08383e'
                        ]],
                        extensions: [[$class: 'WipeWorkspace']]
                    ])
                }
            }
        }

        stage('Clean Reports') {
            steps {
                echo 'Cleaning previous reports...'
                sh 'rm -rf allure-results allure-report clean-allure-results allure-report.zip'
            }
        }

        stage('Start Appium Container') {
            steps {
                echo 'Starting Appium Docker container...'
                dir('appium/docker') {
                    sh 'docker-compose up -d'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Define URLs per environment
                    def envUrls = [
                        SIT: "https://demo.applitools.com/",
                        UAT: "https://uat.example.com",
                       
                    ]
                    def envbaseURIs=[
						 SIT:"https://reqres.in/"
						
					]

                    def selectedEnv = params.ENVIRONMENT
                    def baseUrl = envUrls[selectedEnv]
					def baseURI=envbaseURIs[selectedEnv]
                    def tags = "${params.TAGS}"
                    def parallel = tags.contains("@Mobile") ? "none" : "methods"
                    def threads = tags.contains("@Mobile") ? "1" : "4"

                    echo "Running tests in environment: ${selectedEnv} with base URL: ${baseUrl}"
                    echo "Running tests with tags: ${tags}"

                    // Pass base.url as system property to Maven
                    sh """
                      mvn clean test \
                      -Dbase.url=${baseUrl} \
                      -Dbase.url=${baseURI} \
                      -Dcucumber.filter.tags="${tags}" \
                      -Dparallel.mode="${parallel}" \
                      -Dparallel.threads="${threads}"
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Generating Allure report...'
            allure includeProperties: false,
                   jdk: '',
                   reportBuildPolicy: 'ALWAYS',
                   commandline: 'allure',
                   results: [[path: 'allure-results']]

            echo 'Stopping Appium Docker container...'
            dir('appium/docker') {
                sh 'docker-compose down || true'
            }
        }

        failure {
            echo 'Build or tests failed!'
        }

        success {
            script {
                def recipients = params.ENVIRONMENT == 'SIT' ? env.SIT_USERS : env.UAT_USERS

                echo "Sending email to ${recipients}"

                // Zip the allure report folder to attach in email
                sh 'zip -r allure-report.zip allure-report'

                emailext(
                    subject: "Allure Report - ${params.ENVIRONMENT} - SUCCESS",
                    body: """<p>Hi Team,<br><br>
                             Please find attached the Allure Test Report for the <b>${params.ENVIRONMENT}</b> environment.<br><br>
                             Regards,<br>Jenkins</p>""",
                    mimeType: 'text/html',
                    to: recipients,
                    attachmentsPattern: 'allure-report.zip'
                )
            }
        }
    }
}
