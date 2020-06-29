package com.sys.controller;

import com.sys.entity.Flight;
import com.sys.manager.mainManager;
import com.sys.response.responseBase;
import com.sys.response.responseBuilder;
import javafx.util.Pair;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import services.rpc.DynamicProxyFactory;
import services.rpc.services.BankService;
import services.rpc.services.FlightCompanyService;

import java.util.ArrayList;

@RestController
public class mainController
{
    @RequestMapping("/getFlight")
    public <T> responseBase<T> getFlight(@RequestParam("depart") String depart,
                                         @RequestParam("dest") String dest,
                                         @RequestParam("date") String date)
    {
        FlightCompanyService flightCompanyService=DynamicProxyFactory.getProxy(FlightCompanyService.class,mainManager.getHost("FlightCompanyService","S"));
        return (responseBase<T>)responseBuilder.success(mainManager.getFlight(depart,dest,date));
    }

    @RequestMapping("/getFlightInCart")
    public <T> responseBase<T> getFlightInCast()
    {
        return (responseBase<T>)responseBuilder.success(mainManager.getFlightInCast());
    }

    @RequestMapping("/buyFlight")
    public <T> responseBase<T> buyFlight(@RequestParam("flightID") String fid,
                                         @RequestParam("flightCompany") String fc,
                                         @RequestParam("payMethod") String pm)
    {
        FlightCompanyService flightCompanyService=DynamicProxyFactory.getProxy(FlightCompanyService.class,mainManager.getHost("FlightCompanyService",fc));
        BankService bankService=DynamicProxyFactory.getProxy(BankService.class,mainManager.getHost("BankService",pm));

        Pair<Flight, Integer> ret=flightCompanyService.getFlight(fid);

        Flight ob=ret.getKey();
        int price=Integer.parseInt(ob.getPrice());

        Pair<Boolean, String> pavement=bankService.pay(price);

        if(pavement.getKey()) flightCompanyService.decFlight(fid);

        return (responseBase<T>)responseBuilder.success(pavement);
    }
}
