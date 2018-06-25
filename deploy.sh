#!/bin/sh

NAMESPACE=`oc project -q`

#echo "PROJECT_NAME    =$PROJECT_NAME"
#echo "APPLICATION_NAME=$APPLICATION_NAME"
#echo "NAMESPACE       =$NAMESPACE"

#oc login https://192.168.42.190:8443

#if [ "`oc projects | grep $PROJECT_NAME`" != "" ]; then
#  oc delete project $PROJECT_NAME
#fi

#while [ x$`oc projects | grep $PROJECT_NAME` != x ]; do
# sleep 1
#done

#oc new-project "$PROJECT_NAME" >/dev/null


oc new-app --template=mongodb-persistent --param=MONGODB_DATABASE=pathfinder

sleep 5

# pathfinder-server cleanup (need to do some funky scripting to determine if this needs to execute first)
#oc delete -f local-pathfinder-server-build.yaml
#oc delete -f local-pathfinder-server-deployment.yaml
#oc delete all --selector="app=pathfinder-server"

# pathfinder-server build
oc create -f pathfinder-server-build.yaml
oc new-app --template=pathfinder-server-build --param=APPLICATION_NAME=pathfinder-server

# pathfinder-server deployments
oc create -f pathfinder-server-deployment.yaml
oc new-app --template=pathfinder-server-deployment --param=NAMESPACE=$NAMESPACE --param=APPLICATION_NAME=pathfinder-server


sleep 20

# pathfinder-ui cleanup (need to do some funky scripting to determine if this needs to execute first)
#oc delete -f local-pathfinder-ui-build.yaml
#oc delete -f local-pathfinder-ui-deployment.yaml
#oc delete all --selector="app=pathfinder-ui"

# pathfinder-ui build
oc create -f pathfinder-ui-build.yaml
oc new-app --template=pathfinder-ui-build --param=APPLICATION_NAME=pathfinder-ui

# pathfinder-ui deployments
oc create -f pathfinder-ui-deployment.yaml
oc new-app --template=pathfinder-ui-deployment --param=NAMESPACE=$NAMESPACE --param=APPLICATION_NAME=pathfinder-ui --param=SERVER_APPLICATION_NAME=pathfinder-server

