pipeline {
    agent any

    // Daily Schedule: Har din dopahar 1:00 PM (13:00) ko automatic run karega
    triggers {
        cron('0 13 * * *')
    }

    tools {
        // Must match the tool names configured in Jenkins -> Global Tool Configuration
        maven 'Maven-3.9.9'
        jdk 'JDK 25'
    }

    // Parameters jo Jenkins UI par "Build with Parameters" me dikhenge
    parameters {
        string(name: 'APP_URL', defaultValue: 'https://lakshya.zfourhr.in', description: 'Target Application URL to test')
        choice(name: 'BROWSER', choices: ['chrome', 'edge', 'firefox'], description: 'Target browser for execution')
        choice(name: 'HEADLESS', choices: ['true', 'false'], description: 'Run browser in headless background mode')
        string(name: 'CUCUMBER_TAGS', defaultValue: '@adminLoginLogout', description: 'Cucumber tag filter expression')
    }

    stages {
        stage('Checkout') {
            steps {
                echo '>>> Checking out source code from Git repository...'
                checkout scm
            }
        }

        stage('Build & Dependencies') {
            steps {
                echo '>>> Compiling test framework and validating dependencies...'
                bat 'mvn clean test-compile'
            }
        }

        stage('Execute Automation Tests') {
            steps {
                echo ">>> Running Cucumber BDD scenarios against: ${params.APP_URL} on ${params.BROWSER} [Headless: ${params.HEADLESS}] with tags: ${params.CUCUMBER_TAGS}..."
                // Pass dynamic app.url along with browser, headless, and tags
                bat "mvn test -Dapp.url=${params.APP_URL} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS} -Dcucumber.filter.tags=\"${params.CUCUMBER_TAGS}\""
            }
        }
    }

    post {
        always {
            echo '>>> Archiving test execution results and reports...'

            // 1. Publish standard TestNG / Surefire XML test results for trend graphs
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true

            // 2. Archive interactive Cucumber HTML reports and ExtentReports
            archiveArtifacts artifacts: 'target/cucumber-reports/**, target/extent-reports/**', allowEmptyArchive: true
        }
        failure {
            echo '>>> Automated test execution encountered failures! Review attached screenshots and ExtentReports in Jenkins artifacts.'
        }
        success {
            echo '>>> All automated scenarios executed and passed successfully!'
        }
    }
}
