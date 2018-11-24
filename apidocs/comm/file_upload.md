# 文件上传

通用文件上传接口，此接口接收使用 HTTP form-data 上传的文件，文件属性名自定义，可同时上传多个文件。

接口地址：`/c/file_upload`

## 请求参数

|参数           |类型      |必须  |说明                |
|--------------|---------|------|-------------------|
|type          |String   |是    |上传文件类型
|`{filename}`  |form-data|是    |上传文件 form-data 数据
|......        |......

**type参数**

- userhead 用户头像

## 响应参数

|参数           |类型      |说明                |
|--------------|---------|--------------------|
|code          |Integer  |返回代码：0 - 成功；非0 - 失败
|msg           |String   |返回代码说明
|type          |String   |上传文件类型
|success_count |Integer  |
|items          |Array    |上传成功的文件信息
|　name         |String   |文件名（上传时对应的文件名）
|　file_id      |String   |文件标识（没有或为空时表示上传失败）
|　content_type |String   |文件内容类型
|　file_size    |String   |文件大小
|　url          |String   |文件访问地址
|　error        |String   |上传失败时，失败原因
