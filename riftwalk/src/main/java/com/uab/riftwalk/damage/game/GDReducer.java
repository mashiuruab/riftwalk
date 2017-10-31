package com.uab.riftwalk.damage.game;

import com.google.gson.Gson;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class GDReducer extends Reducer<Text, MapWritable, Text, Text> {
    private Gson gson = new Gson();

    @Override
    protected void reduce(Text gameId, Iterable<MapWritable> values, Context context)
            throws IOException, InterruptedException {

        Map<String, List<Double>> outMap =  new HashMap<String, List<Double>>();
        List<Double> doneList = new ArrayList<Double>();
        List<Double> takenList = new ArrayList<Double>();
        List<Double> healList = new ArrayList<Double>();

        for(MapWritable infoMap : values) {
            DoubleArrayWritable dList = (DoubleArrayWritable) infoMap.get(GDMapper.DONE_LIST);
            DoubleArrayWritable tList = (DoubleArrayWritable) infoMap.get(GDMapper.TAKEN_LIST);
            DoubleArrayWritable hList = (DoubleArrayWritable) infoMap.get(GDMapper.HEAL_LIST);

            populateList(dList.get(), doneList);
            populateList(tList.get(), takenList);
            populateList(hList.get(), healList);
        }

        Collections.sort(doneList, Collections.<Double>reverseOrder());
        Collections.sort(takenList, Collections.<Double>reverseOrder());
        Collections.sort(healList, Collections.<Double>reverseOrder());

        outMap.put(GDMapper.DONE_LIST.toString(), doneList);
        outMap.put(GDMapper.TAKEN_LIST.toString(), takenList);
        outMap.put(GDMapper.HEAL_LIST.toString(), healList);

        context.write(gameId,  new Text(gson.toJson(outMap)));
    }

    private void populateList(Writable[] dwList, List<Double>  dList) {
        for(Writable writableValue : dwList) {
            DoubleWritable doubleVlaue =  (DoubleWritable) writableValue;
            dList.add(doubleVlaue.get());
        }
    }
}
