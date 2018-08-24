package com.redhat.pathfinder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.redhat.pathfinder.charts.Chart2Json;
import com.redhat.pathfinder.charts.DataSet2;

import groovy.lang.Tuple;
import groovy.lang.Tuple2;
import io.restassured.http.Header;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;


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
    System.out.println("Request for System.getenv("+name+")='"+System.getenv(name)+"'");
    
    
//    if (null!=properties.getProperty(name)){
//      return properties.getProperty(name);
//    }else{
    if (null==System.getenv(name) && name.equals("PATHFINDER_SERVER")){
      System.out.println("DEFAULTING SERVER TO: 'http://localhost:8080' because no environment variable '"+name+"' was found");
      return "http://localhost:8080";
    }
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
  

  @GET
  @Path("/logout")
  public Response logout(@Context HttpServletRequest request, @Context HttpServletResponse response) throws URISyntaxException, IOException{
    request.getSession().invalidate();
    return Response.status(302).location(new URI("../index.jsp")).build();
  }

  @POST
  @Path("/login")
  public Response login(@Context HttpServletRequest request, @Context HttpServletResponse response) throws URISyntaxException, IOException{
    
    System.out.println("Controller::login() called");
//    HttpSession session=request.getSession();
    
    String uri=IOUtils.toString(request.getInputStream());
    
    System.out.println("Controller::login() payload = "+uri); //username=&password=
    
    final Map<String, String> keyValues=Splitter.on('&').trimResults().withKeyValueSeparator("=").split(uri);
    
    System.out.println("Controller::login():: username="+keyValues.get("username") +", password="+keyValues.get("password"));

    io.restassured.response.Response loginResp = given()
        .body("{\"username\":\""+keyValues.get("username")+"\",\"password\":\""+keyValues.get("password")+"\"}")
        .post(getProperty("PATHFINDER_SERVER")+"/auth");
    
    if (loginResp.statusCode()!=200){
      String error="Username and/or password is unknown or incorrect"; // would grab the text from server side but spring wraps some debug info in there so until we can strip that we cant give details of failure
      return Response.status(302).location(new URI("../index.jsp?error="+URLEncoder.encode(error, "UTF-8"))).build();
    }
    
    System.out.println("Controller:login():: loginResp.asString() = "+loginResp.asString());
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
  
  /* called from "viewAssessment.jsp" to be displayed on the datatable */
//  @GET
//  @Path("/customers/{customerId}/applications/{appId}/assessments/{assessmentId}/viewAssessmentSummary")
//  public Response viewAssessmentSummary(@PathParam("customerId") String customerId, @PathParam("appId") String appId, @PathParam("assessmentId") String assessmentId) throws JsonGenerationException, JsonMappingException, IOException{
//    
//    //parse the application-survey.js file into a json object structure
//    //get the answers from the assessment
//    //match the two as output to the datatable onscreen
//    
//    mjson.Json x=getSurvey();
//    
//    //MOCKED CODE, when the colors are put into the surveyjs source this can be removed
//    Random r=new Random();
//    String[] ratingsCfg=new String[]{"UNKNOWN","RED","AMBER","AMBER","GREEN","GREEN","GREEN","GREEN"};
//    
//    List<ApplicationAssessmentSummary> result=new ArrayList<ApplicationAssessmentSummary>();
//    for(mjson.Json p:x.asJsonList()){
//      for(mjson.Json q:p.at("questions").asJsonList()){
//        
//        String answerText="";
//        String answerRating="";
//        
//        if (q.at("type").asString().equals("radiogroup")){
//          List<String> answers=new ArrayList<String>();
//          for(mjson.Json a:q.at("choices").asJsonList())
//            answers.add(a.asString());
//          
//          //fix these mocked answers
//          int randomIndex=0 + r.nextInt((answers.size()-1 - 0) + 1);
//          String answerIdx=answers.get(randomIndex).split("\\|")[0];
//          if (answerIdx.contains("-")) answerIdx=answerIdx.split("-")[0];
//          answerText=answers.get(randomIndex).split("\\|")[1];
//          //
//          
//          answerRating=ratingsCfg[Integer.parseInt(answerIdx)];
//          
//          result.add(new ApplicationAssessmentSummary(q.at("title").asString(), answerText, answerRating));
//        }else if (q.at("type").asString().equals("rating")){
//          // leave this out since it's things like "Select the app..."
//        }
//      }
//    }
//    return Response.status(200).entity(Json.newObjectMapper(true).writeValueAsString(result)).build();
//  }
  
  
//  private mjson.Json tmpSurveyCache=null;
//  private mjson.Json getSurvey() throws JsonGenerationException, JsonMappingException, IOException{
////    if (tmpSurveyCache==null){
//      String raw=IOUtils.toString(new URL("http://pathfinder-frontend-vft-dashboard.int.open.paas.redhat.com/pathfinder-ui/assets/js/application-survey.js").openStream());
//      int start=raw.indexOf("pages: [{")+7;
//      int end=raw.indexOf("}],")+2;
//      String x=raw.substring(start, end);
//      System.out.println(x);
////      x="[]";
//      return mjson.Json.read(x);
////      tmpSurveyCache=mjson.Json.read(raw.substring(start, end));
////    }
////    return tmpSurveyCache;
//  }
  
  
}
