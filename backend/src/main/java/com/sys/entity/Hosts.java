package com.sys.entity;

import org.json.JSONObject;

public class Hosts
{
    private String ip;
    private int port;

    public Hosts()
    {

    }

    public Hosts(String s)
    {
        JSONObject json=new JSONObject(s);
        this.ip=json.getString("ip");
        this.port=json.getInt("port");
    }


    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip=ip;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port=port;
    }
}
