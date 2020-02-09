### 前言
`comic-view`项目主要致力于实现一个在线漫画阅读网站。~~仅供个人娱乐~~。

### 项目介绍
`comic-view`是一个在线漫画阅读网站，包括前台个人用户管理系统、漫画阅读系统和后台管理系统，
基于Springboot+MyBatis实现。
前台个人账号注册系统包括用户注册、用户登陆、订阅信息、阅读记录、基本信息管理、安全信息管理等模块。
漫画阅读系统包括漫画展示、漫画搜索、漫画订阅、章节展示、漫画阅读等模块。
后台管理系统包括漫画管理、章节管理、用户管理等模块。

### 搭建步骤
1. 克隆`comic-view`项目到本地，并导入IDEA中部署。
2. 导入`sql.sql`表到MySQL数据库。
3. 在`main\resources`目录下新建`secret.properties`文件，并输入MySQL账号和密码。
```$xslt
database.username = 你的账号
database.password = 你的密码
```
> 启用`ElasticSearch`搜索
4. 下载并安装
   [Elasticsearch](https://www.elastic.co/cn/downloads/past-releases#elasticsearch)
   和[Kibana](https://www.elastic.co/cn/downloads/past-releases#kibana)，
   安装**相应版本**的中文分词器[elasticsearch-analysis-ik](https://github.com/medcl/elasticsearch-analysis-ik/releases/)。
5. 启动项目，在Swagger-UI中访问 <http://localhost:8080/esComic/importAll>，把数据库中内容导入`Elasticsearch`。
   （post请求，且需要`ROLE_ADMIN`权限，默认账号见`sql.sql`，密码均为`123456`）。

### 技术选型
#### 后端技术
|技术                  |说明               |
|----------------------|-------------------|
|SpringBoot            |容器+MVC框架       |
|SpringSecurity        |认证和授权框架     |
|MyBatis               |ORM框架            |
|PageHelper            |MyBatis物理分页插件|
|Swagger-UI            |文档生产工具       |
|Elasticsearch         |搜索引擎           |

#### 前端技术
>前端没有使用主流前后端分离框架，~~未来可能实现。~~

|技术                  |说明               |
|----------------------|-------------------|
|Thymeleaf             |模板引擎           |
|Bootstrap             |前端框架           |
|Jquery                |JavaScript库       |

#### 待实现
+ 新建`tag`表以及`comic-tag-relation`表，支持漫画按`tag`分类
+ 评论功能
+ 实现众多小功能ing...
+ 界面大幅优化...
+ 使用现代化的前端框架重构，实现前后端分离
