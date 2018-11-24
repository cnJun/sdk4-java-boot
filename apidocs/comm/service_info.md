# 服务信息

获取服务信息

## 公共请求参数

|参数          |值
|-------------|-------
|method       |server.info

## 业务请求参数

*无*

## 业务响应参数

|参数              |类型   |描述
|-----------------|--------|----
|app_launch       |Object  |APP启动信息
|　image          |String  |图片地址
|　url            |String  |点击图片时跳转url地址，为空时不需要跳转
|　min_version    |String  |服务端接口支持的APP最低版本，低于此版本需要强制升级
|file_upload_url  |String  |文件上传地址
