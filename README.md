# Server Side Application for SHABEL - Realime Messaging Application

## 概要
SHABELはLine SUMMER INTERNSHIP 2018 エンジニアスクールコースにてKyoto Aチームによって開発されたリアルタイムメッセージングアプリです。

このレポジトリではSHABELにおけるサーバーサイドアプリケーションを扱っています。
クライアントのレポジトリは[こちら](https://github.com/line-school2018summer/kyoto-a-client)からアクセスできます。

## 開発環境
- Kotlin
- SpringBoot (REST API, STOMP over Websocket)
- IntelliJ

## デプロイ
1. docs/models.sqlをサーバー上で実行
```
mysql -h localhost -u [user] -p < models.sql
```

2. プロジェクトルートでGradleビルドを行う
```
gradlew build
```

3. build/libs/に作成されたjarファイルをサーバーに転送

4. nohupを用いてバックグラウンドで実行
```
sudo nohup java -jar api.jar &
```

## 実装機能

### STOMP over Websocket
SHABELではリアルタイムでのメッセージのやり取りをSTOMP over Websocketを用いて実現しています。
STOMPはPub - Sub型のメッセージングプロトコルであり、TCP上、Websocket上で利用できますす。今回はWebsocket上での実装を行いました。

### REST API
リアルタイム以外で情報を受け取る時はSTOMP上ではなくREST APIを叩いて取得するようになっています。


### APIリファレンス
[こちら](https://kyoto-a-api.pinfort.me/swagger-ui.html)からSwagger UI形式のAPIリファレンスを見ることができます。

https://kyoto-a-api.pinfort.me/swagger-ui.html
