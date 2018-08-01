.. This document is formatted in reStructuredText

#################
Pathfinder Vision
#################

Pathfinder is a **decision support** tool for 
customers seeking to modernize a portfolio of enterprise applications.  
As such, it needs to support usage models for various types of customers and sales motions.  
Importantly, Pathfinder is not an automated analysis tool.

This document captures the vision for the tool by describing both 
the customer contexts in which it's expected to operate and 
certain usage models it should support.  
The document also captures future directions Pathfinder development 
might (or might not) pursue.

Context
#######

The context within which Pathfinder operates is comprised of 
the *personas* or roles which interact with the tool and 
the data those roles operate on, namely 
the application portfolio being assessed and 
the Pathfinder system itself.

Personas
========

The following sections describes both 
the roles of persons who interact with the Pathfinder system and also
certain expectations about those persons.

Superuser
---------
Superusers have complete control of a running Pathfinder system and thus
can perform all functions of all other roles defined for the system.
A superuser should be familiar with operation of the Pathfinder tool in
its entirety.

Customer Administrator
----------------------
Customer Administrators should have a comprehensive knowledge of 
the customer organization and a broad sense of the application portfolio to be assessed.
They can administer the portfolio data in the system and the assessors assigned to them.

Application Assessor
--------------------
Application Assessors should have a good understanding of the operation of
applications assigned to them to be assessed.  
That knowledge should pertain especially to 
how the application delivers value to the business, 
who within the organization supports and who depends on the application, and
what is the general architecture of the application being assessed.

Domain Objects
==============

Within the domain of application assessments, 
the personas described above operate on numerous objects, which
are generally partitioned into the client application portfolio and
the Pathfinder system itself.

Portfolio
---------
The portfolio is the collection of applications the client runs in 
support of their business.  
Applications will vary considerably among themselves along dimensions such as
age, supportability, business criticality, complexity, and many others.

Tens
''''
In cases where the client has a few tens of applications, it is expected that
in many cases a Red Hat Consulting Architect can work directly with the client to
assess the application.

Hundreds
''''''''
In cases where the client has as in the neighborhood of one hundred or more applications,
it is expected that a Red Hat Consulting Architect will first survey the portfolio and,
in close collaboration with the client, will distill the portfolio down to 
a handful of profiles, which conceptually relate to OpenShift deployment archetypes.

With those profiles defined, client staff 

Thousands
'''''''''

Pathfinder system
-----------------

Users
'''''

Administrators
''''''''''''''

Developers
''''''''''

Future Directions
=================

