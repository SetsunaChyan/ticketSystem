package services.rpc.services;

import com.sys.entity.Flight;
import javafx.util.Pair;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.*;

public class BankServiceImpl implements BankService
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
    public Pair<Boolean, String> pay(int money)
    {
        Jedis jedis=new Jedis(getRedisIP());
        int lst=Integer.parseInt(jedis.get("balance"));
        if(lst<money) return new Pair<>(false,"余额不足，您的余额："+lst);
        jedis.set("balance",String.valueOf(lst-money));
        return new Pair<>(true,"支付成功，您的余额："+(lst-money));
    }
}
