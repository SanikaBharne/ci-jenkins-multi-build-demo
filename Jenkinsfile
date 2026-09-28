// Declarative pipeline for the News Publishing workflow (Windows agent).
pipeline {
    agent any
    tools { maven 'Maven-3.9.16' }   // must match the Maven name in Manage Jenkins > Tools
    stages {
        stage('Checkout') {
            steps { checkout scm }
        }
        stage('Build and Test') {
            steps { bat 'mvn -B clean verify' }
        }
        stage('Archive') {
            steps { archiveArtifacts artifacts: 'target/*.jar', fingerprint: true }
        }
        stage('Deploy to Staging') {
            steps { bat 'if not exist staging mkdir staging & copy /Y target\\newsflow-ci-demo-1.0.0.jar staging\\' }
        }
        stage('Editor Approval') {
            steps { input message: 'Editor approval: publish to production?' }
        }
        stage('Deploy to Production') {
            steps { bat 'if not exist production mkdir production & copy /Y target\\newsflow-ci-demo-1.0.0.jar production\\' }
        }
    }
    post {
        success { echo 'Pipeline SUCCESS: article workflow build published.' }
        failure { echo 'Pipeline FAILURE: fix the failing check before publishing.' }
    }
}
