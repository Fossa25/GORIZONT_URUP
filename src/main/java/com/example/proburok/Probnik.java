package com.example.proburok;

public class Probnik {
    private String virobotka;
    private Double plohad;

    public Probnik(String virobotka, Double plohad) {
        this.virobotka = virobotka;
        this.plohad = plohad;
    }


    public String getVirobotka() {
        return virobotka;
    }

    public Double getPlohad() {
        return plohad;
    }
}
