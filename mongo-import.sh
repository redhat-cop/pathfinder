#!/bin/bash

mongo localhost:27017/pathfinder --eval 'db.createCollection("customer")'
mongoimport --db pathfinder --collection 'customer' --file mongo-customer.json
mongo localhost:27017/pathfinder --eval 'db.createCollection("applications")'
mongoimport --db pathfinder --collection 'applications' --file mongo-applications.json
mongo localhost:27017/pathfinder --eval 'db.createCollection("assessments")'
mongoimport --db pathfinder --collection 'assessments' --file mongo-assessments.json
mongo localhost:27017/pathfinder --eval 'db.createCollection("member")'
mongoimport --db pathfinder --collection 'member' --file mongo-member.json
mongo localhost:27017/pathfinder --eval 'db.createCollection("reviews")'
mongoimport --db pathfinder --collection 'reviews' --file mongo-reviews.json


#for file in mongo-*.json; do
# c=${file#*mongo-};
# c=${c%.json};
# echo "mongo localhost:27017/pathfinder --eval 'db.createCollection(\"${c}\")'";
# echo "mongoimport --db pathfinder --collection '${c}' --file ${file}";
## mongo localhost:27017/pathfinder --eval 'db.createCollection(\"${c}\")';
## mongoimport --db pathfinder --collection '${c}' --file ${file};
#done




