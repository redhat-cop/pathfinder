#!/bin/sh
tmp_dir=$(mktemp -d -t pathfindermongo-XXXXXXXXXX)
echo $tmp_dir
sed -e 's|PATHFINDERTEMPDIR|'$tmp_dir'|g' mongod-template.conf > mongod.conf
mongod --config ./mongod.conf &
MONGO_PID=$!
#mongoimport -v --host 127.0.0.1 --port 38017 --db pathfinder --collection member --file mongo-member.json
mongoimport -v --host 127.0.0.1 --port 38017 --db pathfinder --collection customer --file mongo-customer.json
mongoimport -v --host 127.0.0.1 --port 38017 --db pathfinder --collection applications --file mongo-applications.json
mongoimport -v --host 127.0.0.1 --port 38017 --db pathfinder --collection assessments --file mongo-assessments.json
mongoimport -v --host 127.0.0.1 --port 38017 --db pathfinder --collection reviews --file mongo-reviews.json
read -p "Press enter to exit MongoDB process"
kill -9 $MONGO_PID
rm -rf $tmp_dir