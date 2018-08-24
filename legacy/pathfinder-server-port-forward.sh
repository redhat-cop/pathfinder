POD=`oc get pods | grep Running | grep mongodb | awk '{print $1}'`
oc port-forward $POD 9191:27017
