package services.rpc.server;

import services.rpc.Server;
import services.rpc.services.BankServiceImpl;
import services.rpc.services.FlightCompanyServiceImpl;

public class BankServer
{
    private final static String CLASS_PATH="services.rpc.services.";
    private final static int port=23333;

    public static void main(String args[]) throws Exception
    {
        Server server=new Server(port);
        server.register(CLASS_PATH+"BankService",new BankServiceImpl());
        new Thread(server).start();
    }
}
