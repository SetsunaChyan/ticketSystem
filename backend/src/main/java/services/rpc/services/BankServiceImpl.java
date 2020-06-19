package services.rpc.services;

import com.sys.entity.Flight;
import javafx.util.Pair;
import redis.clients.jedis.Jedis;

import java.util.List;

public class BankServiceImpl implements BankService
{
    private final static String JedisIP="47.115.54.167";

    @Override
    public Pair<Boolean, String> pay(int money)
    {
        Jedis jedis=new Jedis(JedisIP);
        int lst=Integer.parseInt(jedis.get("balance"));
        if(lst<money) return new Pair<>(false,"余额不足");
        jedis.set("balance",String.valueOf(lst-money));
        return new Pair<>(true,"支付成功");
    }
}
