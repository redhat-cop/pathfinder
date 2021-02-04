# Pathfinder

A workload assessment tool used to determine an applications suitability for running on OpenShift/Kubernetes

[![Build Status](https://travis-ci.org/redhat-cop/pathfinder.svg?branch=master)](https://travis-ci.org/redhat-cop/pathfinder) [![Docker Repository on Quay](https://quay.io/repository/pathfinder/pathfinder-server/status "Docker Repository on Quay")](https://quay.io/repository/pathfinder/pathfinder-server)


# Setup environment on OpenShift (incl. minishift)

The following commands will create a new project and deploy a mongo, pathfinder-server and pathfinder-ui instance into your environment
```
wget https://raw.githubusercontent.com/redhat-cop/pathfinder/master/pathfinder-full-template.yaml

oc new-project <your-new-project-name>
oc new-app --template=mongodb-persistent --param=MONGODB_DATABASE=pathfinder
oc process -f pathfinder-full-template.yaml|oc create -f-
```

Note: If the mongodb template can't be found, you can restore it after creating your new project and before creating the mongodb instance:
```
wget https://raw.githubusercontent.com/openshift/origin/master/examples/db-templates/mongodb-persistent-template.json
oc create -f mongodb-persistent-template.json
```

Login as admin/admin and start adding customers and apps. Once added assess the apps, review and decide what to do and then hit generate report to see the collective outcome and recommendations.

# Usage Tracking
Pathfinder uses analytics to track usage of the application. This can be turned off by setting the "-DdisableTracking=true" JVM option on the Pathfinder UI application.
See the file "run-local.sh" under the pathfinder-ui directory for an example of how to do this.

# License
The code is made available under the Apache License, Version 2.0

The questions are made available under a Creative Commons Attribution-ShareAlike 4.0 International License.

# How to contribute code

* In Github, fork the [https://github.com/redhat-cop/pathfinder project into your github account.
* Clone your forked project to your local machine
* Make changes, commit and push to your forked Github repository
* In Github, click the "Create Pull Request" and select your changes you want to contribute to the core pathfinder/pathfinder-ui project
* A member of the core project will review the contribution and include it


