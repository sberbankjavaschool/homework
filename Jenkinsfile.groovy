#!/usr/bin/env groovy
import java.time.LocalDateTime

//def githubUser = "sberbankjavaschool"
def githubUser = "sberbankjavaschool"
def githubRepo = "homework"
def watsonHello = "Привет, это Ватсон!"
def sherlockFailed = false
pipeline {
    triggers {
        issueCommentTrigger('START-TEST')
    }
    agent any
    stages {
        stage('Branch check') {
            when { expression { env.CHANGE_ID } }
            steps {
                script {
                    print "${pullRequest.headRef} ${pullRequest.base}"
                    if (pullRequest.base == 'source') {
                        def comment = pullRequest.comment("ПР в ветку Source запрещен!")
                        pullRequest.addLabel('WRONG BRANCH')
                        println "To source branch! Forbidden!"
                        error('Unauthorized SOURCE branch modification')
                    } else {
                        removeLabel('WRONG BRANCH')
                    }
                    try {
                        sh "./gradlew --stacktrace checkIfSourceBranchPulled " +
                                "-PsourceBranch='${pullRequest.headRef}' " +
                                "-PforkRepo='https://github.com/${CHANGE_AUTHOR}/homework.git'"
                        removeLabel('REBASE NEEDED')
                    } catch (err) {
                        pullRequest.comment("Ошибка при сверке веток," +
                                " попробуй сделать Pull из ветки source с Rebase.\n${err}")
                        println "Da a barrel roll!"
                        pullRequest.addLabel('REBASE NEEDED')
                    }
                }
            }
        }
        stage('Rebase if needed') {
            when { expression { env.CHANGE_ID } }
            steps {
                script {
                    try {
                        sh "./gradlew --stacktrace forceRebase " +
                                "-PtargetBranch='${pullRequest.base}'"
                    } catch (err) {
                        pullRequest.comment("Ошибка при попытке сделать auto-rebase\n${err}")
                    }
                }
            }
        }
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

                                commentTemplate                       : """{{violation.message}}""",

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
                        String title = pullRequest.title
                        sh "./gradlew --info :watson:test -PprTitle='${title}'"
                    } catch (ex) {
                        pullRequest.comment("Шерлоку стало плохо:\n${ex}")
                        sherlockFailed = true
                    }
                }
                fileOperations([folderCreateOperation("/var/www/ulmc.ru/web/reports/${env.CHANGE_ID}/"),
                                folderCopyOperation(destinationFolderPath: "/var/www/ulmc.ru/web/reports/${env.CHANGE_ID}/",
                                        sourceFolderPath: "./watson/build/reports/tests/test",)])
                script {
                    for (comment in pullRequest.comments) {
                        if (comment.body.startsWith('START-TEST')) {
                            echo "Author: ${comment.user}, Comment: ${comment.body}"
                            pullRequest.deleteComment(comment.id)
                        }
                    }
                    def statusMsg, status
                    if (sherlockFailed) {
                        status = 'failure'
                        pullRequest.labels = ['FAILED']
                        statusMsg = 'Дела плохи, тесты не проходят! Поразбирайся ещё!'
                    } else {
                        status = 'success'
                        pullRequest.labels = ['OK']
                        statusMsg = 'Похоже, что всё чисто. Проверь все тесты и зови преподователя. '
                    }
                    def uri = "https://ulmc.ru/reports/${env.CHANGE_ID}/"
                    pullRequest.createStatus(status: status,
                            context: 'continuous-integration/jenkins/pr-merge/sherlock',
                            description: statusMsg,
                            targetUrl: uri)

                    def msg = "${watsonHello}\n${statusMsg} " + uri
                    echo msg
                    def comment = pullRequest.comment("${LocalDateTime.now()}: ${statusMsg}")
                    echo "Leaving comment OK"
                }
                script {
                   //sh './gradlew clearSherlock'
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

private void removeLabel(String labelToRemove) {
    Iterable<String> labels = pullRequest.labels
    labels.each { label ->
        if (label.equalsIgnoreCase(labelToRemove)) {
            pullRequest.removeLabel(labelToRemove)
        }
    }
}