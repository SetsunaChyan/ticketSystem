package com.sys.entity;

import org.json.JSONObject;

import java.io.Serializable;

public class Flight implements Serializable
{
    private String flightID;
    private String flightCompany;
    private String departure;
    private String departureTime;
    private String arrival;
    private String arrivalTime;
    private String price;
    private String rest;

    public Flight()
    {

    }

    public Flight(String s)
    {
        JSONObject json=new JSONObject(s);
        flightID=(String)json.get("flightID");
        flightCompany=(String)json.get("flightCompany");
        departure=(String)json.get("departure");
        departureTime=(String)json.get("departureTime");
        arrival=(String)json.get("arrival");
        arrivalTime=(String)json.get("arrivalTime");
        price=(String)json.get("price");
        rest=(String)json.get("rest");
    }

    public String getFlightID()
    {
        return flightID;
    }

    public void setFlightID(String flightID)
    {
        this.flightID=flightID;
    }

    public String getFlightCompany()
    {
        return flightCompany;
    }

    public void setFlightCompany(String flightCompany)
    {
        this.flightCompany=flightCompany;
    }

    public String getDeparture()
    {
        return departure;
    }

    public void setDeparture(String departure)
    {
        this.departure=departure;
    }

    public String getDepartureTime()
    {
        return departureTime;
    }

    public void setDepartureTime(String departureTime)
    {
        this.departureTime=departureTime;
    }

    public String getArrival()
    {
        return arrival;
    }

    public void setArrival(String arrival)
    {
        this.arrival=arrival;
    }

    public String getArrivalTime()
    {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime)
    {
        this.arrivalTime=arrivalTime;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price=price;
    }

    public String getRest()
    {
        return rest;
    }

    public void setRest(String rest)
    {
        this.rest=rest;
    }
}
