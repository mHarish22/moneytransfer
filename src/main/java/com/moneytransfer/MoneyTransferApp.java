package com.moneytransfer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/***
 * @author mHarish
 * This is a simple application to start a server to host REST API.
 * The API are used for creating accounts and performing basic operations on the account like
 *  deposit/withdraw and money tranfer.
 *
 */
public class MoneyTransferApp {
    public static void main(String[] args) {
        Server jettyServer = new Server();
        try {
            int portNumber= 8085;
            if(args.length > 0) portNumber = Integer.parseInt(args[0]);

           jettyServer =  startServer(portNumber);
            jettyServer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jettyServer.destroy();
        }
    }

    public static Server startServer(int portNum) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(portNum);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.packages",
                "com.moneytransfer.business");
        jettyServer.start();
        return jettyServer;
    }
}

