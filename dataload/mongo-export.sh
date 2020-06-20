#!/bin/sh -x

dbh="localhost:27017" 

if [ "$1" != "" ]; then
    dbh=$1
else
    dbh="localhost:27017" 
fi

echo "Connecting to MongoDb @ " ${dbh}

oc get secret mongodb --template='{{range $k,$v := .data}}{{$z := base64decode $v}}{{printf "export %s=%s\n" $k $z}}{{end}}'|sed  's/-//g' > /tmp/mongocreds.sh
source /tmp/mongocreds.sh
mongoexport --host ${dbh} --db ${databasename} --username=${databaseuser} --password=${databasepassword} --collection applications --out mongo-applications.json
mongoexport --host ${dbh} --db ${databasename} --username=${databaseuser} --password=${databasepassword} --collection assessments --out mongo-assessments.json
mongoexport --host ${dbh} --db ${databasename} --username=${databaseuser} --password=${databasepassword} --collection customer --out mongo-customer.json
mongoexport --host ${dbh} --db ${databasename} --username=${databaseuser} --password=${databasepassword} --collection member --out mongo-member.json
mongoexport --host ${dbh} --db ${databasename} --username=${databaseuser} --password=${databasepassword} --collection reviews --out mongo-reviews.json

