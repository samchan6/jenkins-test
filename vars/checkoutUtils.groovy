def getProjectType() {
    if (fileExists('app/pom.xml')) {
        return 'MAVEN'
    } else if (fileExists('package.json')) {
        return 'NPM'
    } else {
        return 'UNKNOWN'
    }
}

def getRepoName() {
    if (fileExists('README.md')) {
        return readFile('README.md').split('\n')[0].replace('#', '').trim().replace(' ', '_').toLowerCase()
    }
    return 'UNKNOWN'
}

def getMavenProjectName() {
    def pomContent = readFile('app/pom.xml')
    def matcher = pomContent =~ /<name>(.+?)<\/name>/
    return matcher ? matcher[0][1] : 'UNKNOWN'
}

def getNpmProjectName() {
    def packageContent = readJSON(file: 'package.json')
    return packageContent.name ?: 'UNKNOWN'
}

def getProjectName(projectType) {
    if (projectType == 'MAVEN') {
        return getMavenProjectName()
    } else if (projectType == 'NPM') {
        return getNpmProjectName()
    } else {
        return 'UNKNOWN'
    }
}

def getAuthor() {
    return sh(script: 'git log -1 --pretty=format:"%an"', returnStdout: true).trim()
}

def getAuthorEmail() {
    return sh(script: 'git log -1 --pretty=format:"%ae"', returnStdout: true).trim()
}

def getCommitTime() {
    return sh(script: 'git log -1 --pretty=format:"%cd"', returnStdout: true).trim()
}

def getBranchName() {
    return sh(script: 'git log -1 --pretty=format:%D | grep -o "[^,]*" | tail -n 1 | sed "s|.*/||" ', returnStdout: true).trim()
}

def setEnvVars() {
    env.SCANNER_HOME = tool 'SonarQubeScanner'
    env.PROJECT_TYPE = getProjectType()
    env.PROJECT_NAME = getProjectName(PROJECT_TYPE)
    env.BRANCH_NAME = getBranchName()
    env.REPO_NAME = getRepoName()
    env.PROJECT_KEY = getRepoName().toUpperCase()
    env.COMMIT_AUTHOR_NAME = getAuthor()
    env.COMMIT_AUTHOR_EMAIL = getAuthorEmail()
    env.COMMIT_TIME = getCommitTime()
}

def printCheckoutInfo() {
    if (env.PROJECT_TYPE == 'UNKNOWN') {
        error 'Unknown project'
    } else {
        echo "repository: ${env.REPO_NAME}"
        echo "project name: ${env.PROJECT_NAME}"
        echo "branch: ${env.BRANCH_NAME}"
        echo "git commit: ${env.GIT_COMMIT}"
        echo "git author name: ${env.COMMIT_AUTHOR_NAME}"
        echo "git author email: ${env.COMMIT_AUTHOR_EMAIL}"
        echo "git commit time: ${env.COMMIT_TIME}"
        echo "workspace: ${env.WORKSPACE}"
        echo "repository identified as ${env.PROJECT_TYPE} project"
        sh 'ls -la $WORKSPACE'
    }
}