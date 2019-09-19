package com.example.ffive.seg2105ffiveproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceProvider extends Account {

    private List<String> presentServices;


    // Initialise availabilities map
    HashMap<String, String> availabilities;

    ServiceProvider(String username, String password){
        super(username, password, "ServiceProvider");
        presentServices = new ArrayList<String>();

        // Populate availabilities HashMap
        availabilities = new HashMap<String, String>();
        availabilities.put("Monday", "Not Available");
        availabilities.put("Tuesday", "Not Available");
        availabilities.put("Wednesday", "Not Available");
        availabilities.put("Thursday", "Not Available");
        availabilities.put("Friday", "Not Available");
        availabilities.put("Saturday", "Not Available");
        availabilities.put("Sunday", "Not Available");
    }

    public void addService(String newService){
        presentServices.add(newService);
    }
    public List<String> getServices(){
        return presentServices;
    }
    public boolean deleteService(String service){
        boolean deleted = false;
        for(int i = 0; i < presentServices.size(); i++){
            if(presentServices.get(i).equals(service)){
                presentServices.remove(i);
                deleted = true;
                break;
            }
        }
        return deleted;
    }
    public boolean offersService(String service){
        boolean offers = false;
        for(int i = 0; i < presentServices.size(); i++){
            if(presentServices.get(i).equals(service)){
                offers = true;
                break;
            }
        }
        return offers;
    }
    public String dayAvailability(String day){
        return availabilities.get(day);
    }
    public String setAvailability(String day, String startTime, String endTime){
        String time = startTime + " to " + endTime;
        availabilities.put(day, time);
        return time;
    }
}