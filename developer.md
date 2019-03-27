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