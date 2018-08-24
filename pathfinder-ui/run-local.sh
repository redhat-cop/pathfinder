export PATHFINDER_SERVER=http://localhost:8080
mvn clean package -DskipTests jetty:run -Djetty.port=8083
