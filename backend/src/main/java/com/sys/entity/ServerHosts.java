package com.sys.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServerHosts
{
    private ArrayList<Hosts> hosts;
    private String name;

    public ServerHosts()
    {

    }

    public ServerHosts(String s)
    {
        JSONObject json=new JSONObject(s);
        this.name=json.getString("name");
        this.hosts=new ArrayList<>();
        JSONArray jsonArray=json.getJSONArray("hosts");
        for(int i=0;i<jsonArray.length();i++)
        {
            Hosts tmp=new Hosts(jsonArray.getJSONObject(i).toString());
            hosts.add(tmp);
        }
    }

    public ArrayList<Hosts> getHosts()
    {
        return hosts;
    }

    public void setHosts(ArrayList<Hosts> hosts)
    {
        this.hosts=hosts;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name=name;
    }
}
