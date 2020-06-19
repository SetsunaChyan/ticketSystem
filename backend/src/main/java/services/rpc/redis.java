package services.rpc;

import com.sys.entity.Flight;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

public class redis
{
    private final static String JedisIP="47.115.54.167";

    public static void main(String args[])
    {
        Jedis jedis=new Jedis(JedisIP);
        jedis.del("flights");
        jedis.del("balance");
        jedis.del("flightsInCast");
        Flight f=new Flight();
        f.setFlightID("SH-001");
        f.setFlightCompany("春秋航空");
        f.setDeparture("上海");
        f.setDepartureTime("2020-06-19 08:12:23");
        f.setArrival("北京");
        f.setArrivalTime("2020-06-19-12:33:22");
        f.setPrice("114514");
        f.setRest("1919810");
        jedis.rpush("flights",new JSONObject(f).toString());
        f.setFlightID("BJ-001");
        f.setDeparture("北京");
        f.setArrival("上海");
        f.setPrice("8");
        jedis.rpush("flights",new JSONObject(f).toString());
        f.setArrival("2020-06-18-12:33:22");
        f.setFlightID("HK-001");
        f.setDeparture("香港");
        f.setArrival("纽约");
        jedis.rpush("flights",new JSONObject(f).toString());
        f.setFlightID("SH-002");
        f.setDeparture("上海");
        f.setRest("2");
        jedis.rpush("flights",new JSONObject(f).toString());
        jedis.set("balance","1145140000");
    }
}
