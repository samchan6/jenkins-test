def runContractTestMaven() {
    when { expression { env.PROJECT_TYPE == 'MAVEN' } }
    agent {
        docker {
            image 'maven:3.9.8-eclipse-temurin-21'
            reuseNode true
            args '--network host'
        }
    }
    steps {
        echo "Contract Test"
        configFileProvider([configFile(fileId: 'bbbe1198-7511-421a-b98d-13af7cc08480', variable: 'MAVEN_SETTINGS')]) {
            sh '''
            cd $WORKSPACE/app
            mvn -s $MAVEN_SETTINGS -f pom.xml surefire:test -P contract-test,!unit-test -X
            '''
        }
    }
}