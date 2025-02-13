def buildMaven(String pomLocation = "$WORKSPACE/app", Boolean exportJdk = false) {
    echo "Maven Build with pomLocation: ${pomLocation}"
    configFileProvider([configFile(fileId: 'bbbe1198-7511-421a-b98d-13af7cc08480', variable: 'MAVEN_SETTINGS')]) {
        def exportCommand = exportJdk ? "export JAVA_HOME=/opt/java/openjdk" : ""
        sh """
        ${exportCommand}
        cd ${pomLocation}
        mvn -s $MAVEN_SETTINGS clean install \
            -Dmaven.wagon.http.ssl.insecure=true \
            -X
        mvn -s $MAVEN_SETTINGS jacoco:prepare-agent test jacoco:report -X
        """
    }
}

def buildNpm(String packageLocation = "$WORKSPACE") {
    echo "NPM Build in ${packageLocation}"
    sh """
        cd ${packageLocation}
        rm package-lock.json
        npm cache clean --force
        npm install
    """

    sh "cd ${packageLocation} && CI=false npm run build"
    sh "cd ${packageLocation} && npm test -- --coverage"
}