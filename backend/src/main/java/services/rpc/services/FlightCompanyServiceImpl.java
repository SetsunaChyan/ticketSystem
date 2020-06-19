package services.rpc.services;

import com.sys.entity.Flight;
import javafx.util.Pair;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import services.rpc.DynamicProxyFactory;

import java.util.ArrayList;
import java.util.List;

public class FlightCompanyServiceImpl implements FlightCompanyService
{
    private final static String JedisIP="47.115.54.167";
    private final static String BankServiceIP="localhost";
    //private final static String BankServiceIP="47.115.54.167";
    private final static int BankServicePort=23333;

    @Override
    public List<Flight> getFlight(String depart,String dest,String date)
    {
        Jedis jedis=new Jedis(JedisIP);
        List<String> list=jedis.lrange("flights",0,-1);
        List<Flight> ret=new ArrayList<>();
        for(String it:list)
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
    public Pair<Boolean, String> buyFlight(String flightName)
    {
        BankService service=DynamicProxyFactory.getProxy(BankService.class,BankServiceIP,BankServicePort);

        Jedis jedis=new Jedis(JedisIP);
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
        Pair<Boolean,String> ret=service.pay(Integer.parseInt(ob.getPrice()));
        if(ret.getKey())
        {
            jedis.rpush("flightsInCast",new JSONObject(ob).toString());
            ob.setRest(String.valueOf(Integer.parseInt(ob.getRest())-1));
            jedis.lset("flights",idx,new JSONObject(ob).toString());
        }
        return ret;
    }

    @Override
    public List<Flight> getFlightInCast()
    {
        Jedis jedis=new Jedis(JedisIP);
        List<String> list=jedis.lrange("flightsInCast",0,-1);
        List<Flight> ret=new ArrayList<>();
        for(String it:list)
            ret.add(new Flight(it));
        return ret;
    }

    @Override
    public void addFlight(Flight f)
    {
        Jedis jedis=new Jedis(JedisIP);
        jedis.rpush("flights",new JSONObject(f).toString());
    }
}
