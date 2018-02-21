# cloud-note
云笔记系统，系统使用Spring Cloud框架，数据库查询方面使用Spring Data JPA

项目构建：<br/>
1、首先必须安装MySQL和MongoDB；<br/>
2、将mysql的用户名密码配置到user-service的application.properties；<br/>
3、将mongodb的配置信息配置到note-service的application.properties上。<br/>
<br/>
技术栈：<br/>
1、使用Spring Cloud构建云笔记项目，使用Eureka作为注册中心；<br/>
2、使用Angular5构建前端项目，UI库使用Angular Materail，项目详见cloud-note-web；<br/>
3、Android客户端使用Ionic3，也可以生成iOs和Windows Phone客户端，项目详见cloud-note-phone。<br/>
