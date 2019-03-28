package com.redhat.pathfinder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.apache.tomcat.util.scan.StandardJarScanner;
//import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
//import org.eclipse.jetty.jsp.JettyJspServlet;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.ServerConnector;
//import org.eclipse.jetty.servlet.DefaultServlet;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
//import org.eclipse.jetty.util.component.AbstractLifeCycle;
//import org.eclipse.jetty.webapp.Configuration;

public class Main{
//  private static final Logger LOG=Logger.getLogger(Main.class.getName());
//  private static final String WEBROOT="target/pathfinder-ui";
//  private static final String CONTEXT="pathfinder-ui";
//  private static final int PORT=9090;
//  
//  private Server server;
//
//  public static void main(String[] args) throws Exception{
//    Main main=new Main();
//    main.start(CONTEXT, PORT, WEBROOT);
//    main.waitForInterrupt();
//  }
//  
//  public void start(String ctx, int port, String warPath) throws Exception{
//    server=new Server();
//    ServerConnector connector=new ServerConnector(server);
//    connector.setPort(port);
//    server.addConnector(connector);
//
//    // Add annotation scanning
//    Configuration.ClassList classlist=Configuration.ClassList.setServerDefault(server);
//    classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");
//
//    // Servlet context
//    ServletContextHandler servletContextHandler=new ServletContextHandler(ServletContextHandler.SESSIONS);
//    servletContextHandler.setContextPath("/"+ctx);
//    servletContextHandler.setResourceBase(new URI(warPath).toASCIIString());
//
//    // essential for <%= jsp code %> processing
//    enableEmbeddedJspSupport(servletContextHandler);
//
//
//    ServletHolder holderDefault=new ServletHolder("default", DefaultServlet.class);
//    holderDefault.setInitParameter("resourceBase", new URI(warPath).toASCIIString());
//    holderDefault.setInitParameter("dirAllowed", "true");
//    servletContextHandler.addServlet(holderDefault, "/");
//    server.setHandler(servletContextHandler);
//
//    server.start();
//
//    if (LOG.isLoggable(Level.FINE)) {
//      LOG.fine(server.dump());
//    }
//  }
//
//  private void enableEmbeddedJspSupport(ServletContextHandler servletContextHandler) throws IOException{
//    File tempDir=new File(System.getProperty("java.io.tmpdir"));
//    File scratchDir=new File(tempDir.toString(), "embedded-jetty-jsp");
//
//    if (!scratchDir.exists()) {
//      if (!scratchDir.mkdirs()) {
//        throw new IOException("Unable to create scratch directory: " + scratchDir);
//      }
//    }
//    servletContextHandler.setAttribute("javax.servlet.context.tempdir", scratchDir);
//
//    servletContextHandler.setClassLoader(new URLClassLoader(new URL[0], this.getClass().getClassLoader()));
//
//    // Manually call JettyJasperInitializer on context startup
//    servletContextHandler.addBean(new JspStarter(servletContextHandler));
//
//    // Create / Register JSP Servlet (must be named "jsp" per spec)
//    ServletHolder holderJsp=new ServletHolder("jsp", JettyJspServlet.class);
//    holderJsp.setInitOrder(0);
//    holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
//    holderJsp.setInitParameter("fork", "false");
//    holderJsp.setInitParameter("xpoweredBy", "false");
//    holderJsp.setInitParameter("compilerTargetVM", "1.8");
//    holderJsp.setInitParameter("compilerSourceVM", "1.8");
//    holderJsp.setInitParameter("keepgenerated", "true");
//    servletContextHandler.addServlet(holderJsp, "*.jsp");
//  }
//
//  public void stop() throws Exception{
//    server.stop();
//  }
//
//  public void waitForInterrupt() throws InterruptedException{
//    server.join();
//  }
//  
//  public static class JspStarter extends AbstractLifeCycle implements ServletContextHandler.ServletContainerInitializerCaller{
//    JettyJasperInitializer sci;
//    ServletContextHandler context;
//
//    public JspStarter(ServletContextHandler context){
//      this.sci=new JettyJasperInitializer();
//      this.context=context;
//      this.context.setAttribute("org.apache.tomcat.JarScanner", new StandardJarScanner());
//    }
//
//    @Override
//    protected void doStart() throws Exception{
//      ClassLoader old=Thread.currentThread().getContextClassLoader();
//      Thread.currentThread().setContextClassLoader(context.getClassLoader());
//      try{
//        sci.onStartup(null, context.getServletContext());
//        super.doStart();
//      }finally{
//        Thread.currentThread().setContextClassLoader(old);
//      }
//    }
//  }
}