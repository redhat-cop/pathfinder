package com.redhat.pathfinder;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;

public class InitServlet extends HttpServlet {
	
  public static void main(String[] args) throws ServletException, ParseException, IOException{

  }
  
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    Map<String,String> settings=new HashMap<>();
    
    try{
    	settings.put("PATHFINDER_SERVER", StringUtils.isNotEmpty(Controller.getProperty("PATHFINDER_SERVER"))?Controller.getProperty("PATHFINDER_SERVER"):"NOT SET");
    	settings.put("PATHFINDER_SELF", StringUtils.isNotEmpty(Controller.getProperty("PATHFINDER_SELF"))?Controller.getProperty("PATHFINDER_SELF"):"NOT SET");
    	int len=0;
    	for(Entry<String, String> e:settings.entrySet())
    	  len=Math.max(len, e.getKey().length()+e.getValue().length()+5);
    	
    	System.out.println(String.format("%"+len+"s", " ").replaceAll(" ", "*"));
    	System.out.println("ENV Variables Configured:");
    	for(Entry<String, String> e:settings.entrySet())
    	  System.out.println("  "+e.getKey()+": "+e.getValue());
    	System.out.println(String.format("%"+len+"s", " ").replaceAll(" ", "*"));
    	
    }catch(Exception e){
    }
    
  }
  
  @Override
  public void destroy() {
    super.destroy();
  }

}