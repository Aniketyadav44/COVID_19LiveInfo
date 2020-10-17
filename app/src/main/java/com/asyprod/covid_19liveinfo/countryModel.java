package com.asyprod.covid_19liveinfo;

public class countryModel {

    String flagUrl;
    String countryName;
    String cases;
    String todayCases;
    String active;
    String deaths;
    String todayDeaths;

    public countryModel(String flagUrl, String countryName, String cases, String todayCases, String active, String deaths, String todayDeaths) {
        this.flagUrl = flagUrl;
        this.countryName = countryName;
        this.cases = cases;
        this.todayCases = todayCases;
        this.active = active;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(String todayDeaths) {
        this.todayDeaths = todayDeaths;
    }
}
