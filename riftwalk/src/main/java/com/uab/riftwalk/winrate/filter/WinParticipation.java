package com.uab.riftwalk.winrate.filter;

public class WinParticipation {
    private String unitName;
    private Double totalWin = 0.0;
    private Double totalParticipation = 0.0;
    private Double winRate;

    public WinParticipation(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Double getTotalWin() {
        return totalWin;
    }

    public void incrementTotalWin() {
        this.totalWin++;
    }

    public Double getTotalParticipation() {
        return totalParticipation;
    }

    public void addTotalParticipation(Double totalParticipation) {
        this.totalParticipation += totalParticipation;
    }

    public void calculateWinRate() {
        this.winRate = this.totalWin / this.totalParticipation;
    }
}
