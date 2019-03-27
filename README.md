# Pathfinder

A workload assessment tool used to determine an applications suitability for running on OpenShift/Kubernetes

[![Build Status](https://travis-ci.org/redhat-cop/pathfinder.svg?branch=master)](https://travis-ci.org/redhat-cop/pathfinder) [![Docker Repository on Quay](https://quay.io/repository/pathfinder/pathfinder-server/status "Docker Repository on Quay")](https://quay.io/repository/pathfinder/pathfinder-server)

[![Waffle.io - Kanban board &amp; card count](https://badge.waffle.io/redhat-cop/pathfinder.svg?columns=all)](https://waffle.io/redhat-cop/pathfinder)

# Setup environment on OpenShift (incl. minishift)

The following commands will create a new project and deploy a mongo, pathfinder-server and pathfinder-ui instance into your environment
```
wget https://raw.githubusercontent.com/redhat-cop/pathfinder/master/pathfinder-full-template.yaml

oc new-project <your-new-project-name>
oc new-app --template=mongodb-persistent --param=MONGODB_DATABASE=pathfinder
oc process -f pathfinder-full-template.yaml|oc create -f-
```


# How to contribute code

* In Github, fork the [https://github.com/redhat-cop/pathfinder project into your github account.
* Clone your forked project to your local machine
* Make changes, commit and push to your forked Github repository
* In Github, click the "Create Pull Request" and select your changes you want to contribute to the core pathfinder/pathfinder-ui project
* A member of the core project will review the contribution and include it


