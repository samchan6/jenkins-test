def centralScan() {
    if (env.PROJECT_TYPE == 'NPM') {
        sh'''
        $SCANNER_HOME/bin/sonar-scanner -X \
        -Dsonar.projectKey=${PROJECT_KEY} \
        -Dsonar.projectName="Netop (${PROJECT_KEY})" \
        -Dsonar.projectVersion=1.0 \
        -Dsonar.sources=$WORKSPACE/src \
        -Dsonar.tests=src \
        -Dsonar.test.inclusions=**/*.spec.ts \
        -Dsonar.sourceEncoding=UTF-8 \
        -Dsonar.javascript.lcov.reportPaths=$WORKSPACE/coverage/lcov.info \
        -Dsonar.dynamicAnalysis=reuseReports \
        -Dsonar.ws.timeout=600
        '''
    } else if (env.PROJECT_TYPE == 'MAVEN') {
        sh '''
        $SCANNER_HOME/bin/sonar-scanner -X \
        -Dsonar.projectKey=${PROJECT_KEY} \
        -Dsonar.projectName="Netop (${PROJECT_KEY})" \
        -Dsonar.projectVersion=1.0 \
        -Dsonar.sources=$WORKSPACE/app/src/main/java \
        -Dsonar.tests=$WORKSPACE/app/src/test/java \
        -Dsonar.java.binaries=$WORKSPACE/app/target/classes \
        -Dsonar.java.test.binaries=$WORKSPACE/app/target/test-classes \
        -Dsonar.exclusions=/model/ \
        -Dsonar.core.codeCoveragePlugin=jacoco \
        -Dsonar.coverage.jacoco.xmlReportPaths=$WORKSPACE/app/target/site/jacoco/jacoco.xml \
        -Dsonar.dynamicAnalysis=reuseReports \
        -Dsonar.java.source=1.8 \
        -Dsonar.sourceEncoding=UTF-8 \
        -Dsonar.ws.timeout=600
        '''
    }
}

def scanMaven(String projectName, String appFolder = "$WORKSPACE/app") {
    withSonarQubeEnv('SonarQube') {
        echo "Running sonar scan for ${projectName}; appFolder: ${appFolder}"
        sh """
        $SCANNER_HOME/bin/sonar-scanner -X \
        -Dsonar.projectKey=${projectName} \
        -Dsonar.projectName="Netop (${projectName})" \
        -Dsonar.projectVersion=1.0 \
        -Dsonar.sources=${appFolder}/src/main/java \
        -Dsonar.tests=${appFolder}/src/test/java \
        -Dsonar.java.binaries=${appFolder}/target/classes \
        -Dsonar.java.test.binaries=${appFolder}/target/test-classes \
        -Dsonar.exclusions=/model/ \
        -Dsonar.core.codeCoveragePlugin=jacoco \
        -Dsonar.dynamicAnalysis=reuseReports \
        -Dsonar.java.source=1.8 \
        -Dsonar.sourceEncoding=UTF-8 \
        -Dsonar.ws.timeout=600
        """
    }
}

def setQGInfo() {
    def qg = waitForQualityGate()
    env.SONAR_QG_STATUS = qg.status
    env.SONAR_PROJECT_URL = 'http://hkgwsdv00627.oocl.com:30001/dashboard?id=' + env.PROJECT_KEY
}