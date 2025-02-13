def buildMaven(String pomLocation = "$WORKSPACE/app") {
    echo "Maven Build with pomLocation: ${pomLocation}"
    configFileProvider([configFile(fileId: 'bbbe1198-7511-421a-b98d-13af7cc08480', variable: 'MAVEN_SETTINGS')]) {
        sh """
        cd ${pomLocation}
        mvn -s $MAVEN_SETTINGS clean install \
            -Dmaven.wagon.http.ssl.insecure=true \
            -X
        mvn -s $MAVEN_SETTINGS jacoco:prepare-agent test jacoco:report -X
        """
    }
}

def buildNpm() {
    echo 'NPM Build'
    sh '''
        rm package-lock.json
        npm cache clean --force
        npm install
    '''

    sh 'CI=false npm run build'
    sh 'npm test -- --coverage'
}