package com.uab.riftwalk.winrate.prmcalculation;


import java.util.List;

public class DataModelContainer {
    private List<DataModel> all;
    private List<DataModel> mmr;
    private List<DataModel> patch;
    private List<DataModel> region;

    public List<DataModel> getAll() {
        return all;
    }

    public List<DataModel> getMmr() {
        return mmr;
    }

    public List<DataModel> getPatch() {
        return patch;
    }

    public List<DataModel> getRegion() {
        return region;
    }
}
