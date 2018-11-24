# 意见/BUG反馈

意见建议/BUG上报接口。

## 公共请求参数

|参数          |值
|-------------|-------
|method       |feedback.up

## 业务请求参数

|参数          |是否必填 |描述
|-------------|--------|----
|content      |是      |验证码类型
|files        |否      |图片附件，多个附件以逗号分隔

## 业务响应参数

|参数              |是否必填 |描述
|-----------------|--------|----
|id               |是      |报告ID