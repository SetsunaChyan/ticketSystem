package services.rpc.server;

import services.rpc.Server;
import services.rpc.services.FlightCompanyServiceImpl;

public class FlightCompanyServer
{
    private final static String CLASS_PATH="services.rpc.services.";
    private final static int port=23334;

    public static void main(String args[]) throws Exception
    {
        Server server=new Server(port);
        server.register(CLASS_PATH+"FlightCompanyService",new FlightCompanyServiceImpl());
        new Thread(server).start();
    }
}
