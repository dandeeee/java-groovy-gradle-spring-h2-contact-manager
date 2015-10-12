import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;


public class Launcher {
    private static final Logger LOG =  LoggerFactory.getLogger("root");

    static final String WAR_PATH  = "./src/main/webapp";
    static final String RESOURCE_PATH  = "./src/main/webapp";
    static final int DEFAULT_PORT= 9090;

    private int port;
    private Server server;
    private URI serverURI;

    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis();

        Launcher launcher = new Launcher(DEFAULT_PORT);
        launcher.startWebApp();

        long endTime = System.currentTimeMillis();

        LOG.info("Application started in @" + (endTime - startTime) + "ms.");
        LOG.info("Server URI: " + launcher.serverURI);

        launcher.waitForInterrupt();
    }

    public Launcher(int port){
        this.port = port;
        this.server = new Server();
    }

    public void startWebApp() throws Exception {
        WebAppContext webAppContext = createWebAppContext();

        ServerConnector connector = this.createConnector();
        this.server.addConnector(connector);

        server.setHandler(webAppContext);
        this.server.start();
        this.serverURI = getServerUri(connector);
    }

    private ServerConnector createConnector() {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        return connector;
    }

    /**
     * Setup the basic application "context" for this application at "/"
     * This is also known as the handler tree (in jetty speak)
     */
    private WebAppContext createWebAppContext() {
        WebAppContext context = new WebAppContext();

        context.setContextPath("/");
        context.setWar(WAR_PATH);

        enableJSPSupport(context);

        unlockFSChanging(context);
//        context.addServlet(defaultServletHolder(RESOURCE_PATH), "/");

        return context;
    }

    /**
     * Create Default Servlet (must be named "default")
     */
    private ServletHolder defaultServletHolder(String resourceBase) {
        ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
        holderDefault.setInitParameter("resourceBase", resourceBase);
        holderDefault.setInitParameter("dirAllowed", "true");
        return holderDefault;
    }

    /**
     *
     */
    private void enableJSPSupport(WebAppContext context) {
        context.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers());
        context.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        context.addBean(new ServletContainerInitializersStarter(context), true);
        context.setClassLoader(getUrlClassLoader());
        context.addServlet(jspServletHolder(), "*.jsp");
    }

    /**
     * Ensure the jsp engine is initialized correctly
     */
    private List<ContainerInitializer> jspInitializers() {
        JettyJasperInitializer sci = new JettyJasperInitializer();
        ContainerInitializer initializer = new ContainerInitializer(sci, null);
        List<ContainerInitializer> initializers = new ArrayList<ContainerInitializer>();
        initializers.add(initializer);
        return initializers;
    }

    /**
     * Set Classloader of Context to be sane (needed for JSTL)
     * JSP requires a non-System classloader, this simply wraps the
     * embedded System classloader in a way that makes it suitable
     * for JSP to use
     */
    private ClassLoader getUrlClassLoader() {
        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
        return jspClassLoader;
    }

    /**
     * Create JSP Servlet (must be named "jsp")
     */
    private ServletHolder jspServletHolder() {
        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        holderJsp.setInitParameter("fork", "false");
        holderJsp.setInitParameter("xpoweredBy", "false");
        holderJsp.setInitParameter("compilerTargetVM", "1.7");
        holderJsp.setInitParameter("compilerSourceVM", "1.7");
        holderJsp.setInitParameter("keepgenerated", "true");
        return holderJsp;
    }


    /**
     * Establish the Server URI
     */
    private URI getServerUri(ServerConnector connector) throws URISyntaxException {
        String scheme = "http";
        for (ConnectionFactory connectFactory : connector.getConnectionFactories()) {
            if (connectFactory.getProtocol().equals("SSL-http")) {
                scheme = "https";
            }
        }
        String host = connector.getHost();
        if (host == null) {
            host = "localhost";
        }
        int port = connector.getLocalPort();
        serverURI = new URI(String.format("%s://%s:%d/", scheme, host, port));

        return serverURI;
    }

    /**
     * Fix for Windows, so Jetty doesn't lock files
     */
    private void unlockFSChanging(WebAppContext ctx) {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            ctx.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        }
    }


    /**
     * Cause server to keep running until it receives a Interrupt.
     * Interrupt Signal, or SIGINT (Unix Signal), is typically seen as a result of a kill -TERM {pid} or Ctrl+C
     */
    public void waitForInterrupt() throws InterruptedException {
        server.join();
    }



    /**
     * Establish Scratch directory for the servlet context (used by JSP compilation)
     */
    private File getScratchDir() throws IOException
    {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");

        if (!scratchDir.exists())
        {
            if (!scratchDir.mkdirs())
            {
                throw new IOException("Unable to create scratch directory: " + scratchDir);
            }
        }
        return scratchDir;
    }

}
