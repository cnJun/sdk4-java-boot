# 获取微信js_ticket

调用微信的 JS-SDK 时，获取通过权限 js_ticket。

## 公共请求参数

|参数          |值
|-------------|-------
|method       |comm.wx_js_ticket

## 业务请求参数

|参数          |类型       |是否必填 |描述
|-------------|-----------|--------|----
|wid         |String     |是      |配置项
|url         |String     |是      |调用 JS-SDK 的网页 URL

*wid 参数说明*

- car 固定值

## 业务响应参数

|参数              |是否必填 |描述
|-----------------|--------|----
|appId            |是      |公众号的唯一标识
|timestamp        |是      |生成签名的时间戳
|nonceStr         |是      |生成签名的随机串
|signature        |是      |签名
