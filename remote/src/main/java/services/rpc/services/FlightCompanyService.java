package services.rpc.services;

import com.sys.entity.Flight;
import javafx.util.Pair;

import java.util.List;

public interface FlightCompanyService
{
    List<Flight> getFlight(String depart,String dest,String date);
    List<Flight> getFlightInCast();
    Pair<Flight,Integer> getFlight(String flightName);
    void decFlight(String flightName);
    void addFlight(Flight f);
}
