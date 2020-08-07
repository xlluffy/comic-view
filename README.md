### 前言
`comic-view`项目主要致力于实现一个在线漫画阅读网站。~~仅供个人娱乐~~。
前端部分请参考[`comic-view-web`](https://github.com/xlluffy/comic-view-web)

### 项目介绍
`comic-view`是一个在线漫画阅读网站，包括前台个人用户管理系统、漫画阅读系统和后台管理系统，
前端使用`Vue`框架，后端基于Springboot+MyBatis实现。

前台个人账号注册系统包括用户注册、用户登陆、订阅信息、阅读记录、基本信息管理、安全信息管理等模块。
漫画阅读系统包括漫画展示、漫画搜索、漫画订阅、章节展示、漫画阅读、漫画评论等模块。
后台管理系统包括漫画管理、章节管理、用户管理等模块。

### 搭建步骤
1. 克隆`comic-view`项目到本地，并导入IDEA中部署。
2. 导入`sql.sql`到MySQL数据库。
3. 在`application.properties`文件或系统变量中设置MySQL的账号和密码。
> 启用`ElasticSearch`搜索
4. 下载并安装
   [Elasticsearch](https://www.elastic.co/cn/downloads/past-releases#elasticsearch)
   和[Kibana](https://www.elastic.co/cn/downloads/past-releases#kibana)，
   安装**相应版本**的中文分词器[elasticsearch-analysis-ik](https://github.com/medcl/elasticsearch-analysis-ik/releases/)。
5. 克隆[`comic-view-web`](https://github.com/xlluffy/comic-view-web)项目到本地并部署。
6. 分别启动`comic-view`和`comic-view-web`即可访问[主页](localhost:8080)（sql文件中有默认账号和密码）。
也可将两个项目分别打包在`Nginx`中部署，具体步骤可参见各类教程。

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
|Nginx                 |反向代理服务器     |

#### 前端技术

|技术                   |说明               |
|----------------------|--------------------|
|Vue                   |前端框架            |
|Vue-router            |路由框架            |
|Vuex                  |全局状态管理框架    |
|Bootstrap             |前端UI框架          |
|Jquery                |JavaScript库        |


#### 待实现
+ ~~新建`category`表以及`comic-category-relation`表，支持漫画按`category`分类~~ √
+ ~~评论功能~~ √
+ 优化上传和漫画信息编辑页面
+ 实现众多小功能ing...
+ 界面大幅优化...
+ ~~使用现代化的前端框架重构，实现前后端分离~~√
