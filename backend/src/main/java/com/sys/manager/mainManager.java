package com.sys.manager;

import com.sys.entity.Flight;
import com.sys.entity.Hosts;
import com.sys.entity.ServerHosts;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import services.rpc.DynamicProxyFactory;
import services.rpc.services.FlightCompanyService;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class mainManager
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

    private static JSONObject readJSON(String name) throws IOException
    {
        String fileName=System.getProperty("user.dir")+File.separator+"config"+File.separator+name;
        FileReader fileReader=new FileReader(fileName);
        Reader reader=new InputStreamReader(new FileInputStream(fileName),"utf-8");
        int ch=0;
        StringBuffer sb=new StringBuffer();
        while((ch=reader.read())!=-1) sb.append((char)ch);
        fileReader.close();
        reader.close();
        return new JSONObject(sb.toString());
    }

    public static Pair<String, Integer> getHost(String serviceName,String hostName)
    {
        try
        {
            JSONObject job=readJSON("serviceHostConfig.json");
            JSONArray hosts=job.getJSONArray(serviceName+"Host");
            ArrayList<Hosts> v=new ArrayList<>();
            for(int i=0;i<hosts.length();i++)
            {
                ServerHosts sh=new ServerHosts(hosts.getJSONObject(i).toString());
                if(sh.getName().equals(hostName))
                {
                    v=sh.getHosts();
                    break;
                }
            }
            int len=v.size();
            if(len==0) return null;
            int cur, idx=Math.abs(RND.nextInt())%len;
            cur=idx;
            do
            {
                Hosts tmp=v.get(cur);
                if(ping(tmp.getIp(),tmp.getPort()))
                    return new Pair<>(tmp.getIp(),tmp.getPort());
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

    private static List<String> getFlightCompony()
    {
        List<String> ret=new ArrayList<>();
        try
        {
            JSONObject job=readJSON("serviceHostConfig.json");
            JSONArray hosts=job.getJSONArray("FlightCompanyServiceHost");
            for(int i=0;i<hosts.length();i++)
            {
                ServerHosts sh=new ServerHosts(hosts.getJSONObject(i).toString());
                ret.add(sh.getName());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return ret;
    }

    private static String getRedisIP()
    {
        String fileName=System.getProperty("user.dir")+File.separator+"config"+File.separator+"redisConfig.json";
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
            return job.getString("RedisIP");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "localhost";
        }
    }

    public static List<Flight> getFlight(String depart,String dest,String date)
    {
        List<Flight> ret=new ArrayList<>();
        List<String> flightCompany=getFlightCompony();
        for(String it: flightCompany)
        {
            Pair<String,Integer> ip=mainManager.getHost("FlightCompanyService",it);
            if(ip!=null)
            {
                FlightCompanyService flightCompanyService=DynamicProxyFactory.getProxy(FlightCompanyService.class,ip);
                List<Flight> tmp=flightCompanyService.getFlight(depart,dest,date);
                for(Flight itt: tmp) ret.add(itt);
            }
        }
        return ret;
    }

    public static List<Flight> getFlightInCast()
    {
        Jedis jedis=new Jedis(getRedisIP());
        List<String> list=jedis.lrange("flightsInCast",0,-1);
        List<Flight> ret=new ArrayList<>();
        for(String it: list)
            ret.add(new Flight(it));
        return ret;
    }
}
