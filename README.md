# Web ssh shell & sftp
基于springboot插件式开发web shell和sftp控制台，数据库默认采用hsqldb，ORM采用springjpa，前端框架采用layui

## 使用方式
1. 执行test中的Application
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
![列表](https://github.com/deqyiyt/term/blob/master/images/list.png "列表")
