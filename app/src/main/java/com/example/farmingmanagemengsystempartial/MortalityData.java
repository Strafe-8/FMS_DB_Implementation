package com.example.farmingmanagemengsystempartial;

public class MortalityData {

        private String date, deaths, sick, notes;

        public MortalityData(String date, String deaths, String sick, String notes) {
            this.date = date;
            this.deaths = deaths;
            this.sick = sick;
            this.notes = notes;
        }

        public String getDate() {
            return date;
        }

        public String getDeaths() {
            return deaths;
        }

        public String getSick() {
            return sick;
        }

        public String getNotes() {
            return notes;
        }
}

