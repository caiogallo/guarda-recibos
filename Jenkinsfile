node('android-node'){
    stage('checkout') {
      step([$class: 'WsCleanup'])
        
      sh '''
        mkdir android
        mkdir credentials
      '''
      
      dir('android'){
          git url: 'https://github.com/caiogallo/guarda-recibos.git', credentialsId: 'github', branch: 'master'
      }
      dir('credentials'){
        git url: 'https://github.com/caiogallo/credentials.git', credentialsId: 'github', branch: 'master'
      }
    }
    
    stage('build apk'){
        withCredentials([string(credentialsId: 'signingPassSecret', variable: 'JKS_PSS')]) {
            dir('android'){
                sh '''
                    ./gradlew clean assembleRelease\
                              -PkeyAlias=guarda-arquivo \
                              -PkeyPass=$JKS_PSS \
                              -PstoreFile='../credentials/cgsource.jks' \
                              -PstorePass=$JKS_PSS 
                '''
            }
        }
    }
    
    stage('sign apk'){
        dir('android'){
            sh '''
                $ANDROID_HOME/build-tools/28.0.3/zipalign -v -p 4 \
                        app/build/outputs/apk/release/app-*-unsigned.apk \
                        app/build/outputs/apk/release/app-unsigned-aligned.apk
            '''
        }
    }
    
    stage('signed apk'){
        withCredentials([string(credentialsId: 'signingPassSecret', variable: 'JKS_PSS')]) {
            dir('android'){
                sh '''
                    $ANDROID_HOME/build-tools/28.0.3/apksigner sign --ks ../credentials/cgsource.jks \
                                   --ks-pass pass:$JKS_PSS \
                                   --out signed.apk \
                                   app/build/outputs/apk/release/app-unsigned-aligned.apk
                '''
            }
        }
    }
    
    stage('archive artifacts'){
        archiveArtifacts artifacts: 'android/signed.apk', fingerprint: true
    }
}
