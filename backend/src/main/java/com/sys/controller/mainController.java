package com.sys.controller;

import com.sys.entity.Flight;
import com.sys.response.responseBase;
import com.sys.response.responseBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import services.rpc.DynamicProxyFactory;
import services.rpc.services.BankService;
import services.rpc.services.BankServiceImpl;
import services.rpc.services.FlightCompanyService;
import services.rpc.services.FlightCompanyServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
public class mainController
{
    static final String FlightCompanyServiceIP="localhost";
    //static final String FlightCompanyServiceIP="47.115.54.167";
    static final int FlightCompanyServicePort=23334;
    static final FlightCompanyService service=DynamicProxyFactory.getProxy(FlightCompanyService.class,FlightCompanyServiceIP,FlightCompanyServicePort);

    @RequestMapping("/getFlight")
    public <T> responseBase<T> getFlight(@RequestParam("depart") String depart,
                                     @RequestParam("dest") String dest,
                                     @RequestParam("date") String date)
    {
        return (responseBase<T>)responseBuilder.success(service.getFlight(depart,dest,date));
    }

    @RequestMapping("/getFlightInCart")
    public <T> responseBase<T> getFlightInCast()
    {
        return (responseBase<T>)responseBuilder.success(service.getFlightInCast());
    }

    @RequestMapping("/buyFlight")
    public <T> responseBase<T> buyFlight(@RequestParam("flightID") String flightID)
    {
        System.out.println(flightID);
        return (responseBase<T>)responseBuilder.success(service.buyFlight(flightID));
    }
}
