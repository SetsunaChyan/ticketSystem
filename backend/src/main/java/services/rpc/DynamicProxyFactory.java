package services.rpc;

import javafx.util.Pair;

import java.lang.reflect.*;

// 动态代理类
public class DynamicProxyFactory
{
    public static <T> T getProxy(final Class<T> classType,final Pair<String,Integer> host)
    {
        InvocationHandler handler=(proxy,method,args)->
        {
            Connector connector=null;
            RemoteCall call;

            connector=new Connector(host.getKey(),host.getValue());
            call=new RemoteCall(classType.getName(),method.getName(),method.getParameterTypes(),args);
            connector.send(call);
            call=(RemoteCall)connector.receive();
            connector.close();
            return call.getResult();
        };
        return (T)Proxy.newProxyInstance(classType.getClassLoader(),new Class<?>[]{classType},handler);
    }
}

