pipeline {
    agent any

    tools {
        // Must match the tool names configured in Jenkins -> Global Tool Configuration
        maven 'Maven-3.9.9'
        jdk 'JDK-17'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'edge', 'firefox'], description: 'Target browser for execution')
        choice(name: 'HEADLESS', choices: ['true', 'false'], description: 'Run browser in headless background mode')
        string(name: 'CUCUMBER_TAGS', defaultValue: '@adminLoginLogout', description: 'Cucumber tag filter expression')
    }

    environment {
        // Prevent credentials exposure in logs
        TEST_ENV = 'production'
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
                echo ">>> Running Cucumber BDD scenarios on ${params.BROWSER} [Headless: ${params.HEADLESS}] with tags: ${params.CUCUMBER_TAGS}..."
                // Execute tests via Maven Surefire
                bat "mvn test -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS} -Dcucumber.filter.tags=\"${params.CUCUMBER_TAGS}\""
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
