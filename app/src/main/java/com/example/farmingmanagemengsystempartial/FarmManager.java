package com.example.farmingmanagemengsystempartial;

import java.util.HashMap;

public class FarmManager {
    private static FarmManager instance;
    private HashMap<String, FarmData> farms; // Mapping farm name to its data
    private FarmManager() {
        farms = new HashMap<>();
    }

//    public String getFarmName(){
//        return farmName;
//    }

    public static synchronized FarmManager getInstance() {
        if (instance == null) {
            instance = new FarmManager();
        }
        return instance;
    }

    //uhhhhhhhhh
    public static class FarmData{
        String farmName;
        public FarmData(String name){
            this.farmName = name;
        }
    }


    public HashMap<String, FarmData> getFarms() {
        return farms;
    }

    public void addFarm(String farmName) {
        farms.put(farmName, new FarmData(farmName));
    }

//    public static class FarmData {
//        int numberOfChickens;
//        float averageWeight;
//
//        public FarmData(int numberOfChickens, float averageWeight) {
//            this.numberOfChickens = numberOfChickens;
//            this.averageWeight = averageWeight;
//        }
//    }
}