import static java.util.EnumSet.allOf;

import java.net.URI;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.servlet.ServletContainer;

import com.google.inject.servlet.GuiceFilter;

import example.jersey.Main;

public class EmbeddedJetty {

    private Server server;

    public void start() throws Exception {

        server = new Server(8080);

        WebAppContext bb = new WebAppContext();
        bb.setServer(server);

        bb.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

        ServletHolder holder = bb.addServlet(ServletContainer.class, "/*");
        holder.setInitParameter("javax.ws.rs.Application", "example.jersey.MyApplication");
        bb.addEventListener(new Main());
        bb.addServlet(holder, "/*");
        bb.setContextPath("/");
        bb.setWar("src/main/webapp");
        server.setHandler(bb);
        
        System.out.println(">>> STARTING EMBEDDED JETTY SERVER");
        server.start();
        
    }
    
    public static void main(String[] args) throws Exception {
	    EmbeddedJetty server = new EmbeddedJetty();
	    server.start();
	    System.in.read();
	    server.stop();
    }
    
    public void stop() throws Exception{
        server.stop();
    }
    
    public URI getBaseUri(){
        return server.getURI();
    }
    
}