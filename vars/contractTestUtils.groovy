def runContractTestMaven() {
    echo "Contract Test"
    configFileProvider([configFile(fileId: 'bbbe1198-7511-421a-b98d-13af7cc08480', variable: 'MAVEN_SETTINGS')]) {
        sh '''
        cd $WORKSPACE/app
        mvn -s $MAVEN_SETTINGS -f pom.xml surefire:test -P contract-test,!unit-test -X
        '''
    }
}