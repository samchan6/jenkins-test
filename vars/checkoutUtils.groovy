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

def getRepoNameGit() {
    def remoteName = sh(script: 'git remote', returnStdout: true).trim()
    echo "Remote Name: ${remoteName}"
    if (remoteName) {
        def repoUrl = sh(script: "git remote get-url ${remoteName}", returnStdout: true).trim()
        echo "Remote URL: ${repoUrl}"
        return repoUrl.tokenize('/').last().replace('.git', '')
    }
    echo "No remote name found"
    return 'UNKNOWN'
}

def getMavenProjectName() {
    def pomContent = readFile('app/pom.xml')
    def matcher = pomContent =~ /<name>(.+?)<\/name>/
    return matcher ? matcher[0][1] : 'UNKNOWN'
}

def getNpmProjectName() {
    def packageContent = readFile('package.json')
    return packageJson.name ?: 'UNKNOWN'
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