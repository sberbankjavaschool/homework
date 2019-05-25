#!/usr/bin/env groovy
import java.time.LocalDateTime

//def githubUser = "sberbankjavaschool"
def githubUser = "sberbankjavaschool"
def githubRepo = "homework"
def watsonHello = "Привет, это Ватсон!"
def sherlockFailed = false

def gitInstruction = """Универсальный способ исправить проблемы с гитом:
1. Сделать checkout ветки source из общего репозитория в новую локальную ветку,
2. Сделать pull из своей ветки общего репо в локальную ветку, созданную на шаге 1.
3. Сделать pull из своей ветки в форке, при этом обязательно выбрать "squash commit".
4. Сделать force-push в ветку в своём форке"""
pipeline {
    triggers {
        issueCommentTrigger('START-TEST')
    }
    agent any
    stages {
        stage('Gradle Clean on Start') {
            steps {
                script {
                    try {
                        sh './gradlew clean'
                    } catch (Throwable ex) {
                        //ex.printStackTrace()
                        pullRequest.comment('Проект какой-то кривой! Запусти локальную сборку "gradle build"')
                        error('Clean task Failed')
                    }
                }
            }
        }
        stage('Branch check') {
            when { expression { env.CHANGE_ID } }
            steps {
                script {
                    def errorString = null
                    def fixNeeded = false
                    print "${pullRequest.headRef} ${pullRequest.base}"
                    if (pullRequest.base == 'source') {
                        def comment = pullRequest.comment("ПР в ветку Source запрещен! " +
                                "Жми EDIT, меняй ветку на свою")
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
                    } catch (ignore) {
                        pullRequest.comment("Что произошло? \n" +
                                "Последний коммит из ветки source не был найден в ветке форка. " +
                                "Скрипты и задания могут быть неактуальными!\n" +
                                "Что делать? \n" +
                                "Сделать pull с ребейзом из ветки source в форк.")
                        pullRequest.comment(gitInstruction)
                        println "Do a barrel roll!"
                        pullRequest.addLabel('REBASE NEEDED')
                    }

                    try {
                        sh "./gradlew --stacktrace forceRebase " +
                                "-PtargetBranch='${pullRequest.base}'"
                        removeLabel('HELP ME')
                    } catch (ignore) {
                        pullRequest.comment("Что произошло? \n" +
                                "Скрипт не может сделать автоматический ребейз в твою ветку этого репозитория.\n" +
                                "Что делать? \n" +
                                "Я попробую сделать это автоматически. Если у меня не получится - обратись к преподавателям.")
                        pullRequest.addLabel('HELP ME')
                        fixNeeded = true
                    }

                    if (!fixNeeded) {
                        for (comment in pullRequest.comments) {
                            String body = comment.body
                            println body
                            if (body.contains('FIX-GIT')) {
                                println 'We are here'
                                fixNeeded = true
                                pullRequest.deleteComment(comment.id)
                            }
                            break
                        }
                    }
                    if (fixNeeded) {
                        sh "./gradlew --stacktrace fixGit " +
                                "-PsourceBranch='${pullRequest.headRef}' " +
                                "-PforkRepo='https://github.com/${CHANGE_AUTHOR}/homework.git'"
                        pullRequest.comment("Починил твою ветку. Не благодари.")
                        removeLabel('HELP ME')
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
                        //ex.printStackTrace()
                        pullRequest.comment("Проект не собирается. Попробуй собрать локально. " +
                                "Если и после этого не понятно, зови препода.")
                        error('Build Failed')
                    }
                }
            }
        }

        stage('Static code analysis') {
            when { expression { env.CHANGE_ID } }
            steps {
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

                                commentTemplate                       : "{{violation.message}}",

                                violationConfigs                      : [
                                        [pattern : '.*/reports/checkstyle/.*\\.xml$',
                                         parser  : 'CHECKSTYLE',
                                         reporter: 'Checkstyle']
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
                        if (!ex.getMessage().contains('exit code 1')) {
                            pullRequest.comment("Шерлоку стало плохо.")
                        }
                        sherlockFailed = true
                    }
                }
                fileOperations([folderCreateOperation("/var/www/ulmc.ru/web/reports/${env.CHANGE_ID}/"),
                                folderCopyOperation(destinationFolderPath: "/var/www/ulmc.ru/web/reports/${env.CHANGE_ID}/",
                                        sourceFolderPath: "./watson/build/reports/tests/test",)])
                script {
                    for (comment in pullRequest.comments) {
                        if (comment.body.startsWith('START-TEST')
                                || comment.body == gitInstruction
                                || comment.body.startsWith("Что произошло?")) {
                            echo "Author: ${comment.user}, Comment: ${comment.body}"
                            pullRequest.deleteComment(comment.id)
                        }
                    }
                    def statusMsg, status
                    if (sherlockFailed) {
                        status = 'failure'
                        pullRequest.addLabel('FAILED')
                        removeLabel('OK')
                        statusMsg = 'Дела плохи, тесты не проходят! Поразбирайся ещё!'
                    } else {
                        status = 'success'
                        removeLabel('FAILED')
                        pullRequest.addLabel('OK')
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

private void removeLabel(String labelToRemove) {
    Iterable<String> labels = pullRequest.labels
    labels.each { label ->
        if (label.equalsIgnoreCase(labelToRemove)) {
            pullRequest.removeLabel(labelToRemove)
        }
    }
}
