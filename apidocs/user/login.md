# 登录

用户登录

## 公共请求参数

|参数          |值
|-------------|-------
|method       |user.login

## 业务请求参数

|参数                 |类型    |是否必填 |描述
|--------------------|-------|--------|----
|mobile              |String |是      |手机号码
|password            |String |否      |登录密码
|sms_code            |String |否      |短信登录验证码(和password必须二选一)
|client_type         |String |是      |[设备类型](../appendix/client_type.md)
|client_id           |String |否      |登录设备ID
|bind_account        |Object |否      |使用/绑定第三方账号
|　type              |String |否      |[第三方账号类型](../appendix/3account_type.md)
|　code              |String |否      |第三方系统授权通过以后返回的 code
|　access_token      |String |否      |第三方系统授权通过以后，根据 code 换取的 access_token，和 code 二选一
|　uid               |String |否      |第三方系统授权用户id（微信传 `openid`/微博传 `uid`）
|　bind_id           |String |否      |使用第三方账号，如果该账号未绑定，登录接口返回的绑定ID `bind_id`

*client_id* 为登录设备的标识

- web端可不传
- iOS端传：iOS系统版本 + UUID，逗号分隔
- 安卓端传：手机厂家 + 手机型号 + Android系统版本号 + IMEI，逗号分隔

## 业务响应参数

返回 [用户信息](user.md) 
