export PATHFINDER_SERVER=http://localhost:8080
export PATHFINDER_SELF=http://localhost:8083/pathfinder-ui
mvn clean package -DskipTests jetty:run -Djetty.port=8083
