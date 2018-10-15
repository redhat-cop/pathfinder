Feature: Customer

#Scenario: 01 - Create new customer and delete that customer
#Given we login:
#|Username |Password |
#|admin    |admin    |
#And navigate to the "Customers" page
#When click the "Add Customer" button
#And enter the following into the "New Customer" dialog:
#|Name   |Description |Vertical |Assessor |
#|Test 1 |            |         |         |
#And click the "Create" button
#Then customers exist with the following details:
#|Name   |Description |Vertical |Assessor |
#|Test 1 |            |         |         |
##And delete the customer:
##|Name   |Description |Vertical |Assessor |
##|Test 1 |            |         |         |
##And customers exist with the following details:
##|Name   |Description |Vertical |Assessor |
#
#
#Scenario: 02 - Create several new customers
#Given we login:
#|Username |Password |
#|admin    |admin    |
#And create the following customers:
#|Name          |Description |Vertical |Assessor |
#|Customer S2C1 |            |         |         |
#|Customer S2C2 |            |         |         |
#|Customer S2C3 |            |         |         |
#Then customers exist with the following details:
#|Name          |Description |Vertical |Assessor |
#|Customer S2C1 |            |         |         |
#|Customer S2C2 |            |         |         |
#|Customer S2C3 |            |         |         |
#
#
#Scenario: 03 - Edit an existing customer
## TODO
#
#
## this scenario could do with some enhancing - select a subset, delete and check the remaining customers, then drop the remainder to cleanup the feature set
#Scenario: 04 - Delete all customers
#Given we delete all customers

