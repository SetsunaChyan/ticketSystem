package services.rpc.services;

import com.sys.entity.Flight;
import javafx.util.Pair;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import services.rpc.DynamicProxyFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FlightCompanyServiceImpl implements FlightCompanyService
{
    private String getRedisIP()
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

    @Override
    public List<Flight> getFlight(String depart,String dest,String date)
    {
        Jedis jedis=new Jedis(getRedisIP());
        List<String> list=jedis.lrange("flights",0,-1);
        List<Flight> ret=new ArrayList<>();
        for(String it: list)
        {
            Flight f=new Flight(it);
            boolean ok=true;
            if(!depart.equals("")&&!f.getDeparture().equals(depart)) ok=false;
            if(!dest.equals("")&&!f.getArrival().equals(dest)) ok=false;
            if(!date.equals("")&&!f.getDepartureTime().contains(date)) ok=false;
            if(Integer.parseInt(f.getRest())<1) ok=false;
            if(ok) ret.add(f);
        }
        return ret;
    }

    @Override
    public Pair<Flight, Integer> getFlight(String flightName)
    {
        Jedis jedis=new Jedis(getRedisIP());
        List<String> list=jedis.lrange("flights",0,-1);
        Flight ob=new Flight();
        int idx=0;
        for(int i=0;i<list.size();i++)
        {
            String it=list.get(i);
            Flight f=new Flight(it);
            if(f.getFlightID().equals(flightName))
            {
                ob=f;
                idx=i;
            }
        }
        return new Pair<>(ob,idx);
    }

    @Override
    public void decFlight(String flightName)
    {
        Pair<Flight, Integer> ret=getFlight(flightName);
        Flight ob=ret.getKey();
        int idx=ret.getValue();

        Jedis jedis=new Jedis(getRedisIP());
        jedis.rpush("flightsInCast",new JSONObject(ob).toString());
        ob.setRest(String.valueOf(Integer.parseInt(ob.getRest())-1));
        jedis.lset("flights",idx,new JSONObject(ob).toString());
    }

    @Override
    public void addFlight(Flight f)
    {
        Jedis jedis=new Jedis(getRedisIP());
        jedis.rpush("flights",new JSONObject(f).toString());
    }
}
