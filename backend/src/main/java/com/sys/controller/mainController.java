package com.sys.controller;

import com.sys.entity.Flight;
import com.sys.response.responseBase;
import com.sys.response.responseBuilder;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import services.rpc.DynamicProxyFactory;
import services.rpc.services.BankService;
import services.rpc.services.FlightCompanyService;

import java.io.*;
import java.net.*;
import java.util.Random;

@RestController
public class mainController
{
    static final Random RND=new Random();
    static final int Timeout=100;

    private static boolean ping(String ip,int port)
    {
        Socket socket=new Socket();
        try
        {
            socket.connect(new InetSocketAddress(ip,port),Timeout);
        }
        catch(SocketTimeoutException|ConnectException e)
        {
            return false;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static Pair<String,Integer> getHost(String serviceName)
    {
        String fileName=System.getProperty("user.dir")+File.separator+"config"+File.separator+"serviceHostConfig.json";
        try
        {
            FileReader fileReader=new FileReader(fileName);
            Reader reader=new InputStreamReader(new FileInputStream(fileName),"utf-8");
            int ch=0;
            StringBuffer sb=new StringBuffer();
            while((ch=reader.read())!=-1) sb.append((char)ch);
            fileReader.close();
            reader.close();
            JSONObject job=new JSONObject(sb.toString());
            JSONArray v=job.getJSONArray(serviceName+"Host");
            int len=v.length();
            int cur, idx=Math.abs(RND.nextInt())%len;
            cur=idx;
            do
            {
                JSONObject obj=v.getJSONObject(cur);
                if(ping(obj.getString("ip"),obj.getInt("port")))
                    return new Pair<>(obj.getString("ip"),obj.getInt("port"));
                cur=(cur+1)%len;
            }while(cur!=idx);
            return null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/getFlight")
    public <T> responseBase<T> getFlight(@RequestParam("depart") String depart,
                                         @RequestParam("dest") String dest,
                                         @RequestParam("date") String date)
    {
        FlightCompanyService flightCompanyService=DynamicProxyFactory.getProxy(FlightCompanyService.class,getHost("FlightCompanyService"));
        return (responseBase<T>)responseBuilder.success(flightCompanyService.getFlight(depart,dest,date));
    }

    @RequestMapping("/getFlightInCart")
    public <T> responseBase<T> getFlightInCast()
    {
        FlightCompanyService flightCompanyService=DynamicProxyFactory.getProxy(FlightCompanyService.class,getHost("FlightCompanyService"));
        return (responseBase<T>)responseBuilder.success(flightCompanyService.getFlightInCast());
    }

    @RequestMapping("/buyFlight")
    public <T> responseBase<T> buyFlight(@RequestParam("flightID") String flightID)
    {
        FlightCompanyService flightCompanyService=DynamicProxyFactory.getProxy(FlightCompanyService.class,getHost("FlightCompanyService"));
        BankService bankService=DynamicProxyFactory.getProxy(BankService.class,getHost("BankService"));

        Pair<Flight, Integer> ret=flightCompanyService.getFlight(flightID);

        Flight ob=ret.getKey();
        int price=Integer.parseInt(ob.getPrice());

        Pair<Boolean, String> pavement=bankService.pay(price);

        if(pavement.getKey()) flightCompanyService.decFlight(flightID);

        return (responseBase<T>)responseBuilder.success(pavement);
    }
}
