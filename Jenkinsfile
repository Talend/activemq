#!/usr/bin/env groovy

// The job packages activemq-osgi_5.15.11 with patch
// https://github.com/Talend/activemq/tree/activemq-5.15.11-tipaas

def slackChannel = 'tic-notifications'
def decodedJobName = env.JOB_NAME.replaceAll("%2F", "/")

pipeline {

    agent {
        kubernetes {
            label 'activemq-osgi-build'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
    - name: maven
      image: jenkinsxio/builder-maven:0.1.273
      command:
      - cat
      tty: true
      resources:
          requests:
            memory: "5120Mi"
            cpu: "2.0"
          limits:
            memory: "5120Mi"
            cpu: "2.0"
      volumeMounts:
      - name: docker
        mountPath: /var/run/docker.sock
  volumes:
  - name: docker
    hostPath:
      path: /var/run/docker.sock
  """
        }
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '20', artifactNumToKeepStr: '5'))
        timeout(time: 120, unit: 'MINUTES')
        skipStagesAfterUnstable()
        disableConcurrentBuilds()
    }

    parameters {
        booleanParam(name: 'SKIP_MAVEN_TEST', defaultValue: true, description: 'Pick to disable maven test')
        booleanParam(name: 'PUBLISH', defaultValue: false, description: 'Pick to publish to artifacts-zl.talend.com')
        string(name: 'CLASSIFIER', defaultValue: 'tipaasTest', description: 'Jar classifier name')
    }

    stages {

        stage('Maven clean') {
          steps {
            container('maven') {
              sh "mvn clean"
            }
          }
        }

        stage('Maven package') {
          steps {
            container('maven') {
              configFileProvider([configFile(fileId: 'maven-settings-nexus-zl', variable: 'MAVEN_SETTINGS')]) {
                sh "mvn -Dmaven.test.skip=${params.SKIP_MAVEN_TEST} -Dtipaas.classifier=${params.CLASSIFIER} install -s $MAVEN_SETTINGS"
                archiveArtifacts artifacts: 'activemq-osgi/target/activemq-osgi-5.15.11-*.jar', fingerprint: true, onlyIfSuccessful: true
              }
            }
          }
        }

        stage('Publlish to Nexus') {
          when { expression { params.PUBLISH } }
          steps {
            container('maven') {
              configFileProvider([configFile(fileId: 'maven-settings-nexus-zl', variable: 'MAVEN_SETTINGS')]) {
                sh "mvn deploy:deploy-file -s $MAVEN_SETTINGS -DgeneratePom=true -DrepositoryId=thirdparty-releases -DgroupId=org.apache.activemq -DartifactId=activemq-osgi -Dversion=5.15.11 -Dclassifier=${params.CLASSIFIER} -Dpackaging=jar -Durl=https://artifacts-zl.talend.com/nexus/content/repositories/thirdparty-releases -Dfile=activemq-osgi/target/activemq-osgi-5.15.11-${params.CLASSIFIER}.jar"
              }
            }
          }
        }
    }

    post {
        success {
            slackSend (color: 'good', channel: "${slackChannel}", message: "SUCCESSFUL: `${decodedJobName}` #${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)\nDuration: ${currentBuild.durationString}")
        }
        unstable {
            slackSend (color: 'warning', channel: "${slackChannel}", message: "UNSTABLE: `${decodedJobName}` #${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)")
        }
        failure {
            slackSend (color: '#e81f3f', channel: "${slackChannel}", message: "FAILED: `${decodedJobName}` #${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)")
        }
        aborted {
            slackSend (color: 'warning', channel: "${slackChannel}", message: "ABORTED: `${decodedJobName}` #${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)")
        }
    }

}
