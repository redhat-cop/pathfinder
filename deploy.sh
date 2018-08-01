#!/bin/sh

PROJECT_NAME=`oc project -q`
NAMESPACE=$PROJECT_NAME


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

sleep 10

# pathfinder-server cleanup (need to do some funky scripting to determine if this needs to execute first)
#oc delete -f local-pathfinder-server-build.yaml
#oc delete -f local-pathfinder-server-deployment.yaml
#oc delete all --selector="app=pathfinder-server"

# pathfinder-server build
oc process --param=APPLICATION_NAME=pathfinder-server -f pathfinder-server-build.yaml | oc apply -f-

# pathfinder-server deployments
oc process --param=NAMESPACE=$NAMESPACE -f pathfinder-server-deployment.yaml | oc apply -f-

#cat pathfinder-server-deployment.yaml pathfinder-server-deployment.yaml | oc process --param=NAMESPACE=$NAMESPACE --param=APPLICATION_NAME=pathfinder-server | oc apply -f-

sleep 30

# pathfinder-ui cleanup (need to do some funky scripting to determine if this needs to execute first)
#oc delete -f local-pathfinder-ui-build.yaml
#oc delete -f local-pathfinder-ui-deployment.yaml
#oc delete all --selector="app=pathfinder-ui"



# pathfinder-ui build
#oc get routes --template='{{range .items}}{{if eq .metadata.name "pathfinder-server"}}{{.spec.host}}{{end}}{{end}}'

PATHFINDER_SERVER=http://`oc get routes --template='{{range .items}}{{if eq .metadata.name "pathfinder-server"}}{{.spec.host}}{{end}}{{end}}'`

echo "PATHFINDER_SERVER   =$PATHFINDER_SERVER" 

oc process --param=APPLICATION_NAME=pathfinder-ui -f pathfinder-ui-build.yaml | oc apply -f-

# pathfinder-ui deployments
oc process --param=NAMESPACE=$NAMESPACE --param=APPLICATION_NAME=pathfinder-ui --param=SERVER_APPLICATION_NAME=pathfinder-server --param=PATHFINDER_SERVER=$PATHFINDER_SERVER -f pathfinder-ui-deployment.yaml | oc apply -f-

