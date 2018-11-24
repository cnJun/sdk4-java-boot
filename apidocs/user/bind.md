# 绑定第三方账号

用户在已登录的情况下，绑定第三方微信、微博等账号，绑定后可使用第三方账号登录。

## 公共请求参数

|参数          |值
|-------------|-------
|method       |user.bind

## 业务请求参数

|参数         |类型    |是否必填 |描述
|-------------|-------|--------|----
|client_type  |String |是      |[设备类型](../appendix/client_type.md)
|type         |String |是      |[第三方账号类型](../appendix/3account_type.md)
|code         |String |否      |第三方系统授权通过以后返回的 code
|access_token |String |否      |第三方系统授权通过以后，根据 code 换取的 access_token，和 code 二选一
|uid          |String |否      |第三方系统授权用户id（微信传 `openid`/微博传 `uid`）

## 业务响应参数

返回 [用户信息](user.md) 
