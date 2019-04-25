#!/usr/bin/env groovy
//def githubUser = "sberbankjavaschool"
def githubUser = "sberbankjavaschool"
def githubRepo = "homework"
def watsonHello = "Привет, это Ватсон!"
def sherlockFailed = false
pipeline {
    agent any
    stages {
        stage('Gradle Build') {
            steps {
                script {
                    try {
                        sh './gradlew clearSherlock build -x test'
                    } catch (Throwable ex) {
                        ex.printStackTrace()
                    }
                }
            }
        }

        stage('Static code analysis') {
            when { expression { env.CHANGE_ID } }
            steps {
                //sh './gradlew check -x test'

                step([
                        $class: 'ViolationsToGitHubRecorder',
                        config: [
                                gitHubUrl                             : 'https://api.github.com/',
                                repositoryOwner                       : githubUser,
                                repositoryName                        : githubRepo,
                                pullRequestId                         : CHANGE_ID,

                                credentialsId                         : 'jsj-github',

                                createCommentWithAllSingleFileComments: false,
                                createSingleFileComments              : true,
                                commentOnlyChangedContent             : true,
                                minSeverity                           : 'INFO',
                                maxNumberOfViolations                 : 99999,
                                keepOldComments                       : false,

                                commentTemplate                       : """
    **Reporter**: {{violation.reporter}}{{#violation.rule}}  **Rule**: {{violation.rule}}{{/violation.rule}} **Severity**: {{violation.severity}}
{{violation.message}}""",

                                violationConfigs                      : [
                                        [pattern: '.*/reports/checkstyle/.*\\.xml$', parser: 'CHECKSTYLE', reporter: 'Checkstyle']
                                ]
                        ]
                ])
            }

        }
        stage('Gradle Test') {
            steps {
                script {
                    sh './gradlew test'
                }
            }
        }
        stage('Sherlock') {
            when { expression { env.CHANGE_ID } }
            steps {
                script {
                    sh './gradlew copySherlock'
                }
                script {
                    try {
                        sh './gradlew :watson:test'
                    } catch (ex) {
                        sherlockFailed = true
                    }
                }
                fileOperations([folderCreateOperation("/var/www/ulmc.ru/web/reports/${env.CHANGE_ID}/"),
                                folderCopyOperation(destinationFolderPath: "/var/www/ulmc.ru/web/reports/${env.CHANGE_ID}/",
                                        sourceFolderPath: "./watson/build/reports/tests/test",)])
                script {
                    for (comment in pullRequest.comments) {
                        if (comment.body.startsWith(watsonHello)) {
                            echo "Author: ${comment.user}, Comment: ${comment.body}"
                            pullRequest.deleteComment(comment.id)
                        }
                    }
                    def statusMsg, status
                    if (sherlockFailed) {
                        status = 'failure'
                        pullRequest.labels = ['FAILED' ]
                        statusMsg = 'Дела плохи, тесты не проходят! Поразбирайся ещё!'
                    } else {
                        status = 'success'
                        pullRequest.labels = ['OK' ]
                        statusMsg = 'Всё чисто. Можно звать преподователя. '
                    }
                    def uri = "https://ulmc.ru/reports/${env.CHANGE_ID}/"
                    pullRequest.createStatus(status: status,
                            context: 'continuous-integration/jenkins/pr-merge/sherlock',
                            description: statusMsg,
                            targetUrl: uri)

                    def msg = "${watsonHello} \n ${statusMsg} " + uri
                    echo msg
                    //def comment = pullRequest.comment(msg)
                    //echo "Leaving comment OK"
                }
                script {
                    sh './gradlew clearSherlock'
                    sherlockFailed ? 1 : 0
                }
            }
        }
        stage('Gradle Publish') {
            when { branch 'source' }
            steps {
                script {
                    sh './gradlew publish'
                }
            }
        }

        stage('Gradle Clean') {
            steps {
                script {
                    sh './gradlew clean'
                }
            }
        }
    }
}