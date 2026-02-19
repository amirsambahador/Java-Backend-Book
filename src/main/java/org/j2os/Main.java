import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

void main() throws Exception {
    QueuedThreadPool threadPool = new QueuedThreadPool(200);
    Server server = new Server(threadPool);
    ServerConnector connector = new ServerConnector(server);
    connector.setPort(8081);
    server.addConnector(connector);
    ResourceConfig config = new ResourceConfig().packages("org.j2os.api");
    ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(config));
    ContextHandlerCollection contexts = new ContextHandlerCollection();
    ServletContextHandler apiContext = new ServletContextHandler();
    apiContext.addServlet(jerseyServlet, "/*");
    contexts.addHandler(apiContext);
    server.setHandler(contexts);
    server.start();
}




















































































