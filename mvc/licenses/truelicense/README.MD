# KeyTool生成密钥对
#### 密钥对命令
#### cmd.exe下运行下列命令

keytool -genkeypair -keysize 1024 -validity 3650 -alias "privateKey" -keystore "privateKeys.keystore" -storepass "public_password" -keypass "private_password" -dname "CN=localhost, OU=localhost, O=localhost, L=SH, ST=SH, C=CN"

##### 导出命令<br/>
keytool -exportcert -alias "privateKey" -keystore "privateKeys.keystore" -storepass "public_password" -file "certfile.cer"

##### 导入命令<br/>
keytool -import -alias "publicCert" -file "certfile.cer" -keystore "publicCerts.keystore" -storepass "public_password"

命令执行完之后，会生成三个文件：privateKeys.keystore，publicCerts.keystore，certfile.cer<br/>
其中certfile.cer是暂存文件，删除即可；<br/>
privateKeys.keystore：用于服务端生成license文件<br/>
publicCerts.key
