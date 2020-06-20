# the endpoint that Jetty calls through to is defined in the file src/main/webapp/WEB-INF/override-web.xml
#
#        <servlet-class>org.mitre.dsmiley.httpproxy.ProxyServlet</servlet-class>
#         <init-param>
#             <param-name>targetUri</param-name>
#             <param-value>http://localhost:8080/api</param-value>
#         </init-param>
export PATHFINDER_SERVER=http://localhost:8080
export PATHFINDER_SELF=http://localhost:8083
mvn clean package -DdisableTracking=true -DskipTests -Djetty.port=8083 -DPATHFINDER_SERVER=http://localhost:8080 jetty:run
