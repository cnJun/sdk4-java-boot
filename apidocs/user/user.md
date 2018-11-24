# 用户信息

用户登录或用户信息接口返回的用户信息。

|属性               |类型     |描述
|-------------------|--------|----
|token              |String  |授权访问token
|id                 |String  |用户ID
|mobile             |String  |手机号码
|nickname           |String  |用户昵称
|avatar             |String  |用户头像
|points             |Integer |积分
|create_time        |String  |创建时间，格式：yyyy-MM-dd HH:mm:ss
|bind_account3      |String  |已绑定的第三方账号，多个账号逗号分隔
|new_comment        |Integer |有新的我的评论：大于0表示有
|points_dlbag       |Integer |是否可领取每日登陆福袋：0 - 不可以；1 - 可以
|points_dlbag_count |Integer |每日登陆福袋 - 奖励积分值
|web_base_url       |String  |Web端基地址
|app_download_url   |String  |App下载地址

*使用第三方账号登录时，如果该账号未绑定，则返回以下内容*

|属性               |类型     |描述
|-------------------|--------|----
|bind_type          |String  |[第三方账号类型](../appendix/3account_type.md)
|bind_id            |String  |绑定ID
