# Pathfinder

A workload assessment tool used to determine an applications suitability for running on OpenShift/Kubernetes

[![Build Status](https://travis-ci.org/redhat-cop/pathfinder.svg?branch=master)](https://travis-ci.org/redhat-cop/pathfinder) [![Docker Repository on Quay](https://quay.io/repository/pathfinder/pathfinder-server/status "Docker Repository on Quay")](https://quay.io/repository/pathfinder/pathfinder-server)

[![Waffle.io - Kanban board &amp; card count](https://badge.waffle.io/redhat-cop/pathfinder.svg?columns=all)](https://waffle.io/redhat-cop/pathfinder)

# Setup environment on OpenShift (incl. minishift)

The following commands will create a new project and deploy a mongo, pathfinder-server and pathfinder-ui instance into your environment
```
oc new-project <your-new-project-name>
./deploy.sh
```

If you deployed on Red Hat's IT Open PaaS, then your url for the UI will be [http://pathfinder-ui-YOURNAME.int.open.paas.redhat.com/pathfinder-ui](http://pathfinder-ui-YOURNAME.int.open.paas.redhat.com/pathfinder-ui)


# Setup LOCAL/Laptop environment (but with Mongo on OpenShift)


Deploy a MongoDB on OpenShift (incl. minishift)
```
oc new-project <your-new-project-name>
oc new-app --template=mongodb-persistent --param=MONGODB_DATABASE=pathfinder
```

Configure local pathfinder to be able to login to MongoDB (since the new-app mongo will create random user/password in an OpenShift secret)
```
./print-mongo-creds.sh
```
update the file "pathfinder/src/main/resources/config/application-dev.yml" with the mongo database user credentials.


Then port forward to 9191 so that the local pathfinder can find the remote mongo (this script keeps dropping out so you many need to restart it from time to time).
```
./pathfinder-server-port-forward.sh
```


Startup Pathfinder backend 
```
cd /<path to local /pathfinder repo>
./run-local.sh
```

Startup Pathfinder-UI locally
```
cd /<path to local /pathfinder-ui repo>
./run-local.sh
```

Validate pathfinder is running by hitting  http:localhost:8043/pathfinder-ui


# How to contribute code

* In Github, fork the [https://github.com/redhat-cop/pathfinder](pathfinder) or [https://github.com/redhat-cop/pathfinder-ui](pathfinder-ui) project into your github account.
* Clone your forked project to your local machine
* Make changes, commit and push to your forked Github repository
* In Github, click the "Create Pull Request" and select your changes you want to contribute to the core pathfinder/pathfinder-ui project
* A member of the core project will review the contribution and include it


