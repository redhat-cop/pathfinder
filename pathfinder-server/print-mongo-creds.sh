#!/usr/bin/env bash
PROJECT=`oc project -q`
USER=`oc get secret mongodb -o yaml | grep " database-user" | awk '{print $2}' | base64 -d`
PASS=`oc get secret mongodb -o yaml | grep " database-password" | awk '{print $2}' | base64 -d`

echo "Project              : $PROJECT"
echo "Mongo - Database User: $USER"
echo "Mongo - Database User: $PASS"
