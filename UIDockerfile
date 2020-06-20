FROM registry.access.redhat.com/jboss-webserver-3/webserver31-tomcat8-openshift
MAINTAINER noconnor@redhat.com
ARG BRANCH=master
ARG COMMITHASH=0
ARG COMMITMESSAGE=NA
LABEL PATHFINDER_BRANCH=${BRANCH}
LABEL PATHFINDER_COMMITHASH=${COMMITHASH}
LABEL PATHFINDER_COMMITMSG=${COMMITMESSAGE}
ADD pathfinder-ui/target/root.war /opt/webserver/webapps/ROOT.war