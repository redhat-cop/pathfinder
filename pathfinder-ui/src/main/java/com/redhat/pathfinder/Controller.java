package com.redhat.pathfinder;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import groovy.lang.Tuple2;
import io.restassured.specification.RequestSpecification;


@Path("/pathfinder/")
public class Controller{
  
//  static Properties properties=null;
  public static String getProperty(String name) throws IOException{
//    if (null==properties){
//      properties = new Properties();
//      properties.load(Controller.class.getClassLoader().getResourceAsStream("pathfinder-ui.properties"));
//    }
//    System.out.println("request for property '"+name+"'");
//    System.out.println(" - System.getProperty("+name+")='"+System.getProperty(name)+"'");
    
    
//    if (null!=properties.getProperty(name)){
//      return properties.getProperty(name);
//    }else{
    if (name.equals("PATHFINDER_SERVER")) {
    	String result=null;
    	if (null!=System.getProperty("pathfinder.server")) result=System.getProperty("pathfinder.server");
    	if (null!=System.getProperty("PATHFINDER_SERVER")) result=System.getProperty("PATHFINDER_SERVER");
    	if (null!=System.getenv(name)) result=System.getenv(name);
    	if (null!=result) {
    		System.out.println("Request for System.getenv("+name+")='"+result+"'");
    		return result;
    	}
        System.out.println("DEFAULTING SERVER TO: 'http://localhost:8080' because no environment variable '"+name+"' was found");
        return "http://localhost:8080";
    }
    System.out.println("Request for System.getenv("+name+")='"+System.getenv(name)+"'");
    
    //if (null==System.getenv(name) && name.equals("PATHFINDER_SERVER")){
    //  System.out.println("DEFAULTING SERVER TO: 'http://localhost:8080' because no environment variable '"+name+"' was found");
    //  return "http://localhost:8080";
    //}
    return System.getenv(name);
//    }
  }
  
  class ApplicationAssessmentSummary{
    String question;
    String answer;
    String rating;
    public ApplicationAssessmentSummary(String q, String a, String r){
      this.question=q;
      this.answer=a;
      this.rating=r;
    }
    public String getQuestion(){return question;}
    public String getAnswer(){return answer;}
    public String getRating(){return rating;}
  }
  
  
  
  
  @SuppressWarnings("rawtypes")
  public static void main(String[] asd) throws Exception{
    
    List<ArrayList<String>> apps=Lists.newArrayList(
      Lists.newArrayList("G","G","G","G","G","G"),
      Lists.newArrayList("G","G","G","A","A","A"),
      Lists.newArrayList("A","A","A","A","A","A"),
      Lists.newArrayList("G","A","A","A","A","R"),
      Lists.newArrayList("G","A","A","R","R","R")
    );
    
    double increment=1-(1/apps.get(0).size());
    
    for(List<String> app:apps){
      double score=0;
      Collections.sort(app, new Comparator<String>(){
        public int compare(String o1, String o2){
          return "R".equals(o1)?-1:0;
        }
      });
      double spreadRatio=1;
      for(String r:app){
        if ("R".equals(r)) spreadRatio=spreadRatio*0.4;
        if ("A".equals(r)) spreadRatio=spreadRatio*0.8;
        score+=increment*(1*spreadRatio);
      }
      System.out.println(score);
    }
    
    
    
    if (true) return;
    
    Tuple2 admin=new Tuple2<String,String>("admin", "admin");
    Tuple2 mallen=new Tuple2<String,String>("mallen", "123");
    Tuple2 user=admin;
    
    
    String jwtToken=null;
    if (null!=user){
      io.restassured.response.Response loginResp = given()
        .body("{\"username\":\""+user.getFirst()+"\",\"password\":\""+user.getSecond()+"\"}")
        .post("http://localhost:8080/auth");
  
      System.out.println("Login():: statusCode = "+loginResp.getStatusCode());
      System.out.println("Login():: response = "+loginResp.asString());
      jwtToken=mjson.Json.read(loginResp.asString()).at("token").asString();
      System.out.println("Login():: jwtToken="+jwtToken);
    }
//    if (!loginResp.asString().contains("token")){
//      System.out.println("Login()::ERROR:: headers = "+loginResp.getHeaders());
//      System.err.println("Login()::ERROR:: response = "+loginResp.asString());
//    }else{
    RequestSpecification req=given();
    if (null!=user)
      req.header("Authorization", "Bearer "+jwtToken);
    io.restassured.response.Response customersResp=req.get("http://localhost:8080/api/pathfinder/customers/");
    
      System.out.println("Customers():: statusCode = "+customersResp.getStatusCode());
      System.out.println("Customers():: response = "+customersResp.asString());
      System.out.println("Customers():: headers = "+customersResp.getHeaders());
      
//    }
     
    
  }
  public Response getApps(){
    MongoCredential credential = MongoCredential.createCredential("userS1K", "pathfinder", "JBf2ibxFbqYAmAv0".toCharArray());
    MongoClient c = new MongoClient(new ServerAddress("localhost", 9191), Arrays.asList(credential));
    MongoDatabase db = c.getDatabase("pathfinder");
    
    CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry());
    final DocumentCodec codec = new DocumentCodec(codecRegistry, new BsonTypeClassMap());

    for (String name : db.listCollectionNames()) {
      System.out.println("CollectionName: "+name);
    }
    for(Document d:db.getCollection("assessments").find()){
      System.out.println(d.toJson(codec));
    }
    
//    for(Document d:db.getCollection("customer").find()){
//      System.out.println(d.toJson(codec));
////      Class.forName(className)
////      try{
////        Object x=Json.newObjectMapper(true).readValue(d.toJson(codec), Class.forName(d.getString("_class")));
////        System.out.println("X = "+x);
////      }catch (Exception e){
////        e.printStackTrace();
////      }
////      d.get("_class");
//    }
    
//    String mongoURI=String.format("mongodb://%s:%s@%s:%s/%s", "userS1K","JBf2ibxFbqYAmAv0","localhost","9191","pathfinder");
//    MongoClient c=new MongoClient(mongoURI);
//    MongoDatabase db=c.getDatabase("pathfinder");
    
    c.close();
    return null;
  }
  

//  @GET
//  @Path("/customers/export")
//  public Response export(@Context HttpServletRequest request, @Context HttpServletResponse response) throws URISyntaxException, IOException{
////  	response.setHeader(javax.ws.rs.core.HttpHeaders.CONTENT_DISPOSITION, "attachment");
////  	response.setHeader("Content-Type", "application/json");
//  	
//  	String token=request.getParameter("_t");
//  	
//  	String custIds=request.getParameter("ids");
//  	String url=getProperty("PATHFINDER_SERVER")+"/api/pathfinder/customers/export?ids="+custIds+"&_t="+token;
//  	System.out.println("request to: "+url);
//  	
//  	io.restassured.response.Response resp = given().get(url);
//  	
//  	String responseBody=resp.getBody().asString();
//  	System.out.println("response was: "+responseBody);
//  	
////  	response.getWriter().println(Json.newObjectMapper(true).writeValueAsString(responseBody));
//  	
//  	return Response.status(200)
////  			.header("Content-Type", "application/json")
//  			.header(javax.ws.rs.core.HttpHeaders.CONTENT_DISPOSITION, "attachment;")
////  			.entity(Json.newObjectMapper(true).writeValueAsString(responseBody))
//  			.entity(responseBody)
//  			.build();
//  }
  
  @GET
  @Path("/logout")
  public Response logout(@Context HttpServletRequest request, @Context HttpServletResponse response) throws URISyntaxException, IOException{
    request.getSession().invalidate();
    return Response.status(302).location(new URI("../index.jsp")).build();
  }
  
  private String maskPasswords(String input) {
  	return input.replaceAll("password=.+&", "password=****&");
  }
  
  void log(String s){
  	System.out.println(s);
  	System.err.println(s);
  }
  
  @POST
  @Path("/login")
  public Response login(@Context HttpServletRequest request, @Context HttpServletResponse response) throws URISyntaxException, IOException{
    
    System.out.println("Controller::login() called");
    
    String uri=IOUtils.toString(request.getInputStream());
    
//    log("Controller::login() payload = "+maskPasswords(uri)); //username=&password=
    
    final Map<String, String> keyValues=Splitter.on('&').trimResults().withKeyValueSeparator("=").split(uri);
    
//    System.out.println("Controller::login():: username="+keyValues.get("username") +", password="+keyValues.get("password"));
    log("Controller::login():: username="+keyValues.get("username") +", password=****");

    log("Controller::login():: Auth url (POST) = "+getProperty("PATHFINDER_SERVER")+"/auth");
    
    io.restassured.response.Response loginResp = given()
        .body("{\"username\":\""+keyValues.get("username")+"\",\"password\":\""+keyValues.get("password")+"\"}")
        .post(getProperty("PATHFINDER_SERVER")+"/auth");
    
    if (loginResp.statusCode()!=200){
    	log("Controller:login():: ERROR1 loginResp(code="+loginResp.statusCode()+").asString() = "+loginResp.asString());
    	log("Controller:login():: 3 OUT/ERROR loginResp(code="+loginResp.statusCode()+").asString() = "+loginResp.asString());
      String error="Username and/or password is unknown or incorrect"; // would grab the text from server side but spring wraps some debug info in there so until we can strip that we cant give details of failure
      return Response.status(302).location(new URI("../index.jsp?error="+URLEncoder.encode(error, "UTF-8"))).build();
    }
    
    log("Controller:login():: 2 loginResp(code="+loginResp.statusCode()+").asString() = "+loginResp.asString());
    mjson.Json jsonResp=mjson.Json.read(loginResp.asString());
    String jwtToken=jsonResp.at("token").asString();
    String username=jsonResp.at("username").asString();
    String displayName=jsonResp.at("displayName").asString();
    
    System.out.println("Controller::login():: jwt json response="+jsonResp.toString(99999999));
    System.out.println("Controller::login():: jwtToken="+jwtToken);
    
    
    request.getSession().setAttribute("x-access-token", jwtToken);
    request.getSession().setAttribute("x-username", username);
    request.getSession().setAttribute("x-displayName", displayName);
    
    return Response.status(302).location(new URI("../manageCustomers.jsp")).header("x-access-token", jwtToken).build();
  }
  
  @POST
  @Path("/logout")
  public Response logout(@Context HttpServletRequest request, @Context HttpServletResponse response, @Context HttpSession session) throws URISyntaxException{
    session.removeAttribute("username");
    session.invalidate();
    // TODO: and invalidate it on the server end too!
    return Response.status(302).location(new URI("/index.jsp")).build();
  }
  
  
}
