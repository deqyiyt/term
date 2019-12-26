# Web ssh shell & sftp
基于springboot插件式开发web shell和sftp控制台，目前仅支持linux使用用户名和密码访问  

## 在线体验
[http://term.fishkj.com](http://term.fishkj.com "http://term.fishkj.com")  
用户名：test  
密码：test  

## 使用技术&框架
- 后端：springboot 2.x，理论上使用springboot 1.x也可以
- 前端：layui
- 通信：websocket
- 数据库：默认采用h2
- ORM：springjpa

## 使用方式
1. 执行test中的Application，权限使用基础security功能  
	用户名：admin  
	密码：123456  
	端口：见控制台随机输出  
2. 项目中引用
```xml
	<dependency>
		<groupId>com.fishkj.starter</groupId>
		<artifactId>fish-starter-term</artifactId>
		<version>1.0</version>
	</dependency>
```
访问方式：http://ip:host/term

## 数据库
1. 默认数据库采用h2  
	http://ip:host/h2  
	
1. 需更换springjpa的话，直接继承MachineService并实现即可
2. 需更换数据库为mysql
```xml
<dependency>
	<groupId>com.fishkj.starter</groupId>
	<artifactId>fish-starter-term</artifactId>
	<version>1.0</version>
	<exclusions>
		<groupId>org.hsqldb</groupId>
		<artifactId>hsqldb</artifactId>
	</exclusions>
</dependency>
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>
```
## 相关截图
- 列表
![image](https://github.com/deqyiyt/term/raw/master/images/list.png)
- shell 控制台
![image](https://github.com/deqyiyt/term/raw/master/images/terminal.png)
- ftp 控制台
![image](https://github.com/deqyiyt/term/raw/master/images/sftp.png)

## 许可证

[Apache License 2.0](https://github.com/deqyiyt/term/blob/master/LICENSE)

Copyright (c) 2019-2020 hujiuzhou
