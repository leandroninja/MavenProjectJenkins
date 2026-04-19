pipeline {
    agent any

    tools {
        maven 'Maven 3.9'
        jdk 'JDK 17'
    }

    environment {
        APP_NAME    = 'app-livros'
        APP_VERSION = '1.0.0'
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Obtendo código-fonte do SCM..."
                checkout scm
            }
        }

        stage('Compilar') {
            steps {
                echo "Compilando o projeto com Maven..."
                sh 'mvn clean compile -B'
            }
        }

        stage('Testes') {
            steps {
                echo "Executando testes unitários..."
                sh 'mvn test -B'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco(
                        execPattern: 'target/jacoco.exec',
                        classPattern: 'target/classes',
                        sourcePattern: 'src/main/java'
                    )
                }
            }
        }

        stage('Empacotar') {
            steps {
                echo "Gerando artefato JAR..."
                sh 'mvn package -DskipTests -B'
            }
        }

        stage('Arquivar Artefato') {
            steps {
                echo "Arquivando artefato de build..."
                archiveArtifacts(
                    artifacts: 'target/*.jar',
                    fingerprint: true,
                    allowEmptyArchive: false
                )
            }
        }
    }

    post {
        success {
            echo "Build ${env.BUILD_NUMBER} concluido com sucesso! Artefato: ${APP_NAME}-${APP_VERSION}.jar"
        }
        failure {
            echo "Build ${env.BUILD_NUMBER} falhou. Verifique os logs acima."
        }
        always {
            cleanWs()
        }
    }
}
