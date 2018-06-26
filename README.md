# Pathfinder - Setup REMOTE DEV environment on OSE (incl. minishift)


The following commands will create a new project and deploy a mongo, pathfinder-server and pathfinder-ui instance into your environment
```
oc new-project <your-new-project-name>
./deploy.sh
```

If you deployed on Red Hat's IT Open PaaS, then your url for the UI will be [http://pathfinder-ui-<your-new-project-name>.int.open.paas.redhat.com/pathfinder-ui](http://pathfinder-ui-<your-new-project-name>.int.open.paas.redhat.com/pathfinder-ui)


# Pathfinder - Setup LOCAL DEV environment (with Mongo on OSE)


Deploy a MongoDB on OSE (incl. minishift)
```
oc new-project <your-new-project-name>
oc new-app --template=mongodb-persistent --param=MONGODB_DATABASE=pathfinder
```

Startup Pathfinder and/or Pathfinder-UI locally
```
./run-local.sh
```


Configure local project to be able to login to MongoDB (since the new-app mongo will create random user/password in the OSE secret)
```
./print-mongo-creds.sh
```
update the file "src/main/resources/config/application-dev.yml" with the mongo database user credentials.

Then port forward to post 9191 so that the local pathfinder can find the remote mongo.
```
./pathfinder-port-forward.sh
```


# Pathfinder - how to contribute code

* In Github, fork the [https://github.com/redhat-cop/pathfinder](pathfinder) or [https://github.com/redhat-cop/pathfinder-ui](pathfinder-ui) project into your github account.
* Clone your forked project to your local machine
* Make changes, commit and push to your forked Github repository
* In Github, click the "Create Pull Request" and select your changes you want to contribute to the core pathfinder/pathfinder-ui project
* A member of the core project will review the contribution and include it



# Below here is older instructions that need to be verified with Noel



# pathfinder
This application was generated using JHipster 4.14.0, you can find documentation and help at [http://www.jhipster.tech/documentation-archive/v4.14.0](http://www.jhipster.tech/documentation-archive/v4.14.0).

# Building the application
## Create Quay Push secret
```
oc create secret docker-registry quayhub --docker-server=quay.io --docker-username=username --docker-password=password --docker-email=user@gmail.com
```

## Add the secret to the build for push and pull
```
oc set build-secret --push bc/pathfinderapp quayhub
oc set build-secret --pull bc/pathfinderapp quayhub
oc set build-secret --push bc/pathfinderbinary quayhub
oc set build-secret --pull bc/pathfinderbinary quayhub

```

## Add the secret to the default SA to pull the image
```
oc secrets link default quayhub --for=pull
```

## To pull in the image once built and pushed
```
oc tag quay.io/noeloc/pathfinder pathfinderapp:latest
```

## Create the Buildconfigs -  One for source builds another for binary builds
```
oc process -f pathfinder-build-template.yaml|oc create -f-
```

# Deploying the application
## Give the default SA access to the view resources - needed for spring clooud kubernetes
```
oc policy add-role-to-user view system:serviceaccount:$(oc project -q):default -n $(oc project -q)
```

## Create the mongodb database
```
oc process -n openshift template/mongodb-persistent -p MONGODB_DATABASE=pathfinder|oc create -f-
```

## Deploy the application
```
oc process -f pathfinder-template.yaml| oc create -f-
```



