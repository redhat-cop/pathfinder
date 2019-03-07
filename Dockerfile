FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest
ADD pathfinder-server/target/pathfinder-server-1.0.2-SNAPSHOT.jar /deployments/app.jar