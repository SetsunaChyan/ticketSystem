package services.rpc.services;

import com.sys.entity.Flight;
import javafx.util.Pair;

import java.util.List;


public interface FlightCompanyService
{
    List<Flight> getFlight(String depart,String dest,String date);
    Pair<Boolean,String> buyFlight(String flightName);
    List<Flight> getFlightInCast();
    void addFlight(Flight f);
}
