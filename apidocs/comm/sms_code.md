# 短信验证码

通过该接口下发短信验证码。

## 公共请求参数

|参数          |值
|-------------|-------
|method       |sms.code

## 业务请求参数

|参数          |类型       |是否必填 |描述
|-------------|-----------|--------|----
|type         |String     |是      |验证码类型
|mobile       |String     |是      |手机号码

*type 参数说明*

- user_login 用户登录
- change_mobile 修改手机号

## 业务响应参数

|参数              |是否必填 |描述
|-----------------|--------|----
|sms_id           |是      |短信验证码ID
