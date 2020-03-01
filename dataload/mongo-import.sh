#!/bin/sh -x

dbh="localhost:27017" 

if [ "$1" != "" ]; then
    dbh=$1
else
    dbh="localhost:27017" 
fi

echo "Connecting to MongoDb @ " ${dbh}
while true; do
    read -p "Do you wish to import data into PathFinder (all existing data will be lost) ?" yn
    case $yn in
        [Yy]* ) break;;
        [Nn]* ) exit;;
        * ) echo "Please answer yes or no.";;
    esac
done

oc get secret mongodb --template='{{range $k,$v := .data}}{{$z := base64decode $v}}{{printf "export %s=%s\n" $k $z}}{{end}}'|sed  's/-//g' > /tmp/mongocreds.sh
source /tmp/mongocreds.sh

mongoimport --host ${dbh} --db ${databasename} --username=${databaseuser} --password=${databasepassword} --collection applications --drop --file mongo-applications.json
mongoimport --host ${dbh} --db ${databasename} --username=${databaseuser} --password=${databasepassword} --collection assessments --drop --file mongo-assessments.json
mongoimport --host ${dbh} --db ${databasename} --username=${databaseuser} --password=${databasepassword} --collection customer --drop --file mongo-customer.json
mongoimport --host ${dbh} --db ${databasename} --username=${databaseuser} --password=${databasepassword} --collection member --drop --file mongo-member.json
mongoimport --host ${dbh} --db ${databasename} --username=${databaseuser} --password=${databasepassword} --collection reviews --drop --file mongo-reviews.json

rm -f /tmp/mongocreds.sh

