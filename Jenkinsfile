def gitCredentialsId = 'github-qoadmin'

def createBuildInfoFile(targetDir) {
    sh "[ -d ${targetDir} ] &&  echo ${targetDir} already exists || mkdir -p ${targetDir}"
    sh "git log --no-decorate --date=iso | head -4 > ${targetDir}/build_info.txt"
}

def getCurrentCommit(projectDir) {
    dir(projectDir) {
        return sh(script: "git log --no-decorate | head -1 | cut -f2 -d' '", returnStdout: true).trim()
    }
}

pipeline {
    agent { label 'jenkins-agent-1' }
    environment {
        MAVEN_OPTS = '-Duser.home=../.cache'
        NEXUS_IP = sh(script: "getent hosts nexus.qodev.es | cut -f1 -d ' ' ", returnStdout: true).trim()
        PROJECT_NAME = sh(script: 'echo $REPO_URL | awk -F / \'{print $NF}\' | cut -f1 -d"."', returnStdout: true).trim()
        DOCKER_IMAGE_URL = "h3lp3r/${PROJECT_NAME}"
    }
    parameters {
        string(name: 'REPO_URL', defaultValue: 'git@github.com:QualityObjects/h3lp3r-back.git', description: 'URL del repositorio')
        string(name: 'BUILD_BRANCH', defaultValue: 'master', description: 'Rama a construir')
        booleanParam(name: 'BUILD_BACK', defaultValue: true, description: 'Construye el back')
        choice(name: 'BACK_TESTS', choices: ['only_unit', 'unit_and_sonar', 'unit_and_sonar_strict_tests', 'no_tests'], description: 'Tests a ejecutar')
        choice(name: 'DEPLOY_NAMESPACE', choices: ['h3lp3r'], description: 'Namespace a desplegar')
    }
    stages {
        stage('Clean WS') {
            steps {
                script {
                    sh '[ $(ls -1 *.tgz 2>/dev/null | wc -l) -gt 0 ] && rm -f *.tgz || exit 0'
                    sh '[ $(ls -1 *.jar 2>/dev/null | wc -l) -gt 0 ] && rm -f *.jar || exit 0'
                    sh '[ ! -d .cache ] && mkdir .cache || echo .cache exists'
                }
            }
        }

        stage('Clone Git project') {
            when {
                environment name: 'BUILD_BACK', value: 'true'
            }
            steps {
                script {
                    try {
                        sh 'mkdir ${PROJECT_NAME}'
                    } catch (err) {
                        dir("${PROJECT_NAME}") {
                            deleteDir()
                        }
                        sh 'mkdir ${PROJECT_NAME}'
                    }
                }

                dir("${PROJECT_NAME}") {
                    git url: "${REPO_URL}",
                            branch: "${BUILD_BRANCH}",
                            credentialsId: gitCredentialsId

                    createBuildInfoFile('src/main/resources')
                }
            }
        }
        stage('Test') {
            when {
                expression {
                    return env.BACK_TESTS != 'no_tests';
                }
            }
            stages {
                stage('Unit tests') {
                    steps {
                        dir("${PROJECT_NAME}") {
                            sh 'mvn verify'
                        }
                    }
                }
                stage('Sonar tests') {
                    when {
                        anyOf {
                            environment name: 'BACK_TESTS', value: 'unit_and_sonar'
                            environment name: 'BACK_TESTS', value: 'unit_and_sonar_strict_tests'
                        }
                    }
                    steps {
                        dir("${PROJECT_NAME}") {
                            withSonarQubeEnv(installationName: 'QO Sonar') {
                                sh 'mvn verify sonar:sonar -DskipTests=true'
                            }
                        }
                    }
                }
                stage('Quality Gate') {
                    when {
                        environment name: 'BACK_TESTS', value: 'unit_and_sonar_strict_tests'
                    }
                    steps {
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }

        stage('Build') {
            when {
                environment name: 'BUILD_BACK', value: 'true'
            }
            environment {
                COMMIT_BACK = sh(script: 'cd ${PROJECT_NAME} && git log --no-decorate | head -1 | cut -f2 -d\' \'', returnStdout: true).trim()
                MAVEN_OPTS = '-Duser.home=../.cache'
                APP_VERSION = sh(script: 'python3 -c "from xml.etree.ElementTree import parse; print(parse(\'${PROJECT_NAME}/pom.xml\').getroot().find(\'./{*}version\').text)"', returnStdout: true).trim()
            }
            stages {
                stage('Build project') {
                    agent {
                        docker {
                            image 'maven:3-openjdk-11'
                            args '--add-host nexus.qodev.es:${NEXUS_IP} '
                            reuseNode true
                        }
                    }
                    steps {
                        dir("${PROJECT_NAME}") {
                            sh 'echo "Version: ${APP_VERSION}" >> src/main/resources/build_info.txt'
                            sh 'rm -f target/*.jar 2> /dev/null'
                            sh 'mvn clean package -U -DskipTests=true'
                            sh 'ls -lA "${WORKSPACE}/.cache/.m2" || echo No .m2'
                            dir('target') {
                                sh 'ls -a'
                                sh 'cp *.jar "${WORKSPACE}/"'
                            }
                        }
                        stash includes: '*.jar', name: "${PROJECT_NAME}-files"
                    }
                }
                stage('Docker image build') {
                    steps {
                        dir("${PROJECT_NAME}") {
                            unstash "${PROJECT_NAME}-files"
                            script {
                                //def jarfile = sh(script: 'ls -1tr *.jar | tail -1', returnStdout: true).trim()
                                def jarfile = sh(script: 'ls -1tr *.jar | grep $APP_VERSION | tail -1', returnStdout: true).trim()
                                def commit = getCurrentCommit("${PROJECT_NAME}")
                                def buildArgs = "--build-arg JARFILE=${jarfile} --label \"branch=${BUILD_BRANCH}\" --label \"version=${APP_VERSION}\" --label \"commit=${commit}\" --label \"maintainer=Jarvis team <jarvis_team@qualityobjects.com>\" ."
                                def appImage = docker.build("${DOCKER_IMAGE_URL}:latest", buildArgs)
                                docker.withRegistry('https://docker-registry.qodev.es', 'docker-registry.qodev.es') {
                                    appImage.push("${env.APP_VERSION}")
                                    appImage.push("latest")
                                    appImage.push("candidate")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
