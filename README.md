# MasterAPI
==============
Table Standard
	- Table => tbl_{name_with_snake_case}
	- View => vw_{name_with_snake_case}
	- Column
		- User Column Name => {nameWithCamelCase}
		- Default Columns => {name_with_snake_case}
			- version	int(11)	No		
			- created_by	int(11)	No	FK(tbl_user)	
			- modified_by	char(1)	No	FK(tbl_user)	
			- created_at	timestamp	No		Default CURRENT_TIMESTAMP
			- modified_at	datetime	Yes		
			- deleted_at	datetime	Yes		

Package Structure
	- entity (Table Modeling) *
		- Naming Standard
			- Property => camelCase
			- Class => {ObjectNameWithCamelCase}Entity
	- resource (Request/Response Manager) *
		- Naming Standard
			- Class => {ResourceNameWithCamelCase}Resource
	- request  (Request from Client Object)
		- Naming Standard
			- Property => camelCase
			- Class => {ObjectNameWithCamelCase}Request
	- response (Response to Client Object)
		- Naming Standard
			- Property => camelCase
			- Class => {ObjectNameWithCamelCase}Response
	- util	(Other Helper/Manager)
	- dao	(Database Accessing Manager)