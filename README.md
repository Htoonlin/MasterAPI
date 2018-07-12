# MasterAPI
This RESTful framework was developed by following:
- [jersey 2](https://jersey.github.io/) for RESTful.
- [maven](https://maven.apache.org/) for repository management.
- [hibernate ORM](http://hibernate.org/) and mysql jdbc for Database.

So, you have to know about these frameworks and libraries. [Getting Started](#getting_started)

## Version 3.0 Changelog
- Upgrade maven libraries
- Remove and Reduce 
    - Remove SESSION management to prevent multi session creation 
    - Remove JSP Web Page 
    - Remove application path "api" 
    - Generate upload directory ({basePath}/upload) if does not exist in setting (com.sdm.path.upload)
    - Remove GeoIPCache 
    - Hide (Null/Empty) property in JSON Response
- Change routeList path (sample/customers/route => /sample/customers/~info)
- Auth allowed userName/email 
- Performance Improvement and Bug Fixed 
- Use Exceptions instead of MessageModel for ErrorMessages
```java
MessageModel(204, "...") => throw new NullPointerException("...") 
MessageModel(400, "...") => throw new InvalidRequestException("...", "...", "...")
```

### New Rest Resources 
```java
@Path("sample/customers")
public class CustomerResource extends RestResource<CustomerEntity, Integer> {
    public CustomerResource() {
        super();
    }
}
```
---

## <a name="getting_started"></a>Getting started
- Clone or download the project from [github](https://github.com/Htoonlin/MasterAPI)
- Open project in Java IDE such as Eclipse, NetBeans, etc ...
- Clone following setting files:
	- [example.hibernate.cfg.xml](./src/main/resources/example.hibernate.cfg.xml) => __hibernate.cfg.xml__ 
	- [example.log4j.properties](./src/main/resources/example.log4j.properties) => __log4j.properties__
	- [example.setting.properties](./src/main/resources/example.setting.properties) => __setting.properties__
- Edit require properties from setting files.

### Require properties to edit in hibernate.cfg.xml
Change database configuration at  (__line: 8-11__)
```xml
<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/{your_db_schema}?zeroDateTimeBehavior=convertToNull&amp;useUnicode=true&amp;characterEncoding=UTF-8</property>
<property name="hibernate.connection.username">{your_db_user}</property>
<property name="hibernate.connection.password">{your_db_password}</property>
<property name="hibernate.default_schema">{your_db_schema}</property>
```

### Require properties to edit in log4j.properties
Change Log directory name at (__line: 15__)
```properties
log4j.appender.fileAppender.File={your_log_directory}/sundew-api.log
```

### Require properties to edit in setting.properties
Change directory for system at (__line: 9, 10__)
```properties
com.sdm.path={system_root_directory}
com.sdm.path.storage={file_upload_directory}
```

Change mail setting for system mail service at (__line: 45-49__). You can use your gmail account.
```properties
com.sdm.mail.host=smtp.gmail.com
com.sdm.mail.port=465
com.sdm.mail.is_auth=true
com.sdm.mail.user={your_gmail_account}
com.sdm.mail.password={your_gmail_password}
```
### Ready for your API system now!
___

### Package Structure
#### entity (Table Modeling)*
	- Naming Standard
		- Property => camelCase
		- Class => {ObjectNameWithCamelCase}Entity
#### resource (Request/Response Manager)*
	- Naming Standard
		- Class => {ResourceNameWithCamelCase}Resource
#### request  (Request from Client Object)
	- Naming Standard
		- Property => camelCase
		- Class => {ObjectNameWithCamelCase}Request
#### response (Response to Client Object)
- Naming Standard
	- Property => camelCase
	- Class => {ObjectNameWithCamelCase}Response
#### util	(Other Helper/Manager)
#### dao	(Database Accessing Manager)
- Naming Standard
	- Property => camelCase
	- Class => {ObjectNameWithCamelCase}DAO
___
	
### DATABASE Standard
- Table => tbl_{name_with_snake_case}
- View => vw_{name_with_snake_case}
- Procedure => proc_{name_with_snake_case}
- User Columns => {nameWithCamelCase}	
___

