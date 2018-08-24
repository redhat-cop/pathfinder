Feature: Customer

Scenario: 01 - Create new customer
Given we login with the following credentials:
|Username |Password |
|admin    |admin    |
And we navigate to the "Customers" page
When the "Add Customer" button is clicked
And we enter the following into the "New Customer" dialog:
|Name   |Description |Vertical |Assessor |
|Test 1 |            |         |         |
Then a customer exists in the customers screen with the following details:
|Name   |Description |Vertical |Assessor |
|Test 1 |            |         |         |