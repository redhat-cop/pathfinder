#!/bin/bash

for file in mongo-*.json; do
 c=${file#*mongo-};
 c=${c%.json};
 mongoimport--db pathfinder --collection '${c}' --file ${file};
done
