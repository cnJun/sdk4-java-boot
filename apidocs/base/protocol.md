# API 调用协议

API 接口使用 JSON 格式 HTTP API，接口权限使用 token 方式进行验证。

## 调用流程

API 协议：填充参数 > 拼接HTTP请求 > 发起HTTP请求 > 得到HTTP响应 > 解析JSON结果

**以下参数需要放在 Http Request Header 中**

|参数名          |参数说明
|---------------|--------------------|
|Content-Type   |application/json
|Authorization  |Token *{token}*

- *{token}* 为登录成功后，服务端返回的授权访问 token，用于标识用户身份信息及合法性。
- 对于 Web 端，调用登录接口后，后端会保存用户会话 Session，不需要传

## 公共请求参数

调用任意 API 接口都必须传入以下公共请求参数：

|参数          |是否必填 |描述
|-------------|--------|----
|method       |是      |接口名称
|timestamp    |否      |发送请求时间，格式：yyyy-MM-dd HH:mm:ss
|version      |否      |调用接口版本，固定值：1.0
|token        |否      |授权访问 token，该参数可以放入 http 头中
|biz_content  |否      |业务请求参数的集合，JSON格式

## 业务参数

API 接口本身业务级的参数，以 JSON 格式，放入 biz_content 字段。

## 公共响应参数

API 接口响应结果以 JSON 格式返回，包含以下公共响应参数。

|参数          |是否必填 |描述
|-------------|--------|----
|code         |是      |返回代码，0 - 成功；非0 - 失败
|msg          |是      |返回消息
|request_id   |是      |请求ID
|rsp_content  |否      |业务响应数据的集合，JSON格式

## 注意事项

1. 所有接口都只支持 POST 请求
2. 所有接口请求 Content-Type 类型为：application/json
3. 所有接口请求类型和返回类型都是 JSON 格式数据，并使用 UTF-8 编码
