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

生成许可接口 /license/generateLicense
要上传的参数例子
<pre>
{
    "subject": "license_demo",
    "privateAlias": "privateKey",
    "keyPass": "private_password",
    "storePass": "public_password",
    "licensePath": "E:/LicenseDemo/license.lic",
    "privateKeysStorePath": "E:/LicenseDemo/privateKeys.keystore",
    "issuedTime": "2022-04-26 14:48:12",
    "expiryTime": "2022-08-22 00:00:00",
    "consumerType": "User",
    "consumerAmount": 1,
    "description": "这是证书描述信息",
    "licenseCheckModel": {
        "ipAddress": [
            "192.168.3.57"
        ],
        "macAddress": [
            "D8-F2-CA-06-1A-F3"
        ],
        "cpuSerial": "BFEBFBFF000806EA",
        "mainBoardSerial": "PM01I01911000743"
    }
}
</pre>

出处： https://blog.csdn.net/weixin_44870914/article/details/126748857
