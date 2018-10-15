Feature: Assessments

Scenario: 00 - Setup a new customer for assessment tests
Given we login:
|Username |Password |
|admin    |admin    |
And navigate to the "Customers" page
When click the "Add Customer" button
And enter the following into the "New Customer" dialog:
|Name           |Description |Vertical |Assessor |
|Assmt Customer |            |         |         |
And click the "Create" button
Then customers exist with the following details:
|Name           |Description |Vertical |Assessor |
|Assmt Customer |            |         |         |


Scenario: 01 - Create an application in the assessments page
Given we login with "admin/admin":
And click into the customer "Assmt Customer"
When click the "Add Application" button
And enter the following into the "New Application" dialog:
|Name  |Stereotype |Description |Owner |
|App 1 |TARGETAPP  |            |      |
And click the "Create" button
Then the following application assessments exist:
|Name   |Assessed |Reviewed |Criticality |Decision |Effort |Review Date |
|App 1  |No       |         |            |         |       |            |


#Scenario: 99 - Delete all customers
#Given we delete all customers