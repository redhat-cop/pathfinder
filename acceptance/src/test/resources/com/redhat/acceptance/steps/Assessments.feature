Feature: Assessments

Scenario: 01 - Create an application in the assessments page
Given we login with "admin/admin":
And create and open customer:
|Name          |Description |Vertical |Assessor |
|Customer 1    |            |         |         |
When click the "Add Application" button
And enter the following into the "New Application" dialog:
|Name  |Stereotype |Description |Owner |
|App 1 |TARGETAPP  |            |      |
And click the "Create" button
Then the following application assessments exist:
|Name   |Assessed |Reviewed |Criticality |Decision |Effort |Review Date |
|App 1  |No       |         |            |         |       |            |
And delete the applications:
|Name   |Assessed |Reviewed |Criticality |Decision |Effort |Review Date |
|App 1  |No       |         |            |         |       |            |


Scenario: 02 - Create multiple applications in the assessments page
Given we login with "admin/admin":
And create and open customer:
|Name          |Description |Vertical |Assessor |
|Customer 1    |            |         |         |
When create the following applications:
|Name  |Stereotype |Description |Owner |
|App 1 |TARGETAPP  |            |      |
|App 2 |TARGETAPP  |            |      |
|App 3 |TARGETAPP  |            |      |
|App 4 |TARGETAPP  |            |      |
Then the following application assessments exist:
|Name   |Assessed |Reviewed |Criticality |Decision |Effort |Review Date |
|App 1  |No       |         |            |         |       |            |
|App 2  |No       |         |            |         |       |            |
|App 3  |No       |         |            |         |       |            |
|App 4  |No       |         |            |         |       |            |
And delete all applications


#Scenario: 99 - Delete all customers
#Given we delete all customers