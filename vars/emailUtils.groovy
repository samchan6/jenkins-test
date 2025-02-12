def sendEmail() {
    def buildStatus = currentBuild.currentResult
    def emailSubject = "[${env.REPO_NAME}] CI/CD ${buildStatus.toLowerCase()} on build #${env.BUILD_NUMBER}"
    def emailBody = """
        <p>Build Status: <strong>${buildStatus}</strong></p>
        <p>Project: ${env.REPO_NAME}</p>
        <p>Branch: ${env.BRANCH_NAME}</p>
        <p>Commit ${env.GIT_COMMIT} by ${env.COMMIT_AUTHOR_NAME} (${env.COMMIT_AUTHOR_EMAIL}) at ${env.COMMIT_TIME}</p>
        <p>SonarQube Quality Gate Status: <strong>${env.SONAR_QG_STATUS ?: 'N/A'}</strong></p>
        <p>SonarQube Report: <a href="${env.SONAR_PROJECT_URL ?: '#'}">View Report</a></p>
        <p><a href="${env.BUILD_URL}">${env.JOB_NAME} #${env.BUILD_NUMBER}</a></p>
    """
    emailext(
        subject: emailSubject,
        body: emailBody,
        to: env.COMMIT_AUTHOR_EMAIL,
        mimeType: 'text/html'
    )
}