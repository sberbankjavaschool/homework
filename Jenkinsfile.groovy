#!/usr/bin/env groovy
//def githubUser = "sberbankjavaschool"
def githubUser = "jenkins-java-school-2019"
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

                                createCommentWithAllSingleFileComments: true,
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
                    def statusMsg = sherlockFailed
                            ? 'Дела плохи, тесты не проходят! Поразбирайся ещё!'
                            : 'Всё чисто. Можно звать преподователя.'
                    def msg = "${watsonHello} \n ${statusMsg}\n Улики, которые нашел Шерлок: " +
                            "https://ulmc.ru/reports/${env.CHANGE_ID}/"
                    echo msg
                    def comment = pullRequest.comment(msg)
                    echo "Leaving comment OK"
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