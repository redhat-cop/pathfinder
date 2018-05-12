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



