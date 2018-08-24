export PATHFINDER_SERVER=http://pathtest-pathfinder.6923.rh-us-east-1.openshiftapps.com
mvn clean package -DskipTests jetty:run -Djetty.port=8083
