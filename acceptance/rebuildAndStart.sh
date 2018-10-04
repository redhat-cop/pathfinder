cd ..; mvn clean install -Dspring.profiles.active=dev,swagger -Pwar; cd -; mvn clean package cargo:run -Pmongo,pathfinder -Dspring.profiles.active=dev,swagger
