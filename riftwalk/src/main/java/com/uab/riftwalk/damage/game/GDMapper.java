package com.uab.riftwalk.damage.game;

import com.google.gson.Gson;
import com.uab.riftwalk.model.DamageEvent;
import com.uab.riftwalk.model.DataJsonMapper;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GDMapper extends Mapper<LongWritable, Text, Text, MapWritable> {
    private Gson gson = new Gson();
    public static Text DONE = new Text("done");
    public static Text TAKEN = new Text("taken");
    public static Text HEAL = new Text("heal");

    public static Text DONE_LIST = new Text("doneList");
    public static Text TAKEN_LIST = new Text("takenList");
    public static Text HEAL_LIST = new Text("healList");

    @Override
    protected void map(LongWritable key, Text json, Context context)
            throws IOException, InterruptedException {
        DataJsonMapper jsonMapper = get(json);
        String gameId = jsonMapper.getGameId();

        List<DamageEvent> damageEvents = jsonMapper.getData().getDamageEvents();

        Map<String, Map<String, Double>> championDamageMap =
                new HashMap<String, Map<String, Double>>();

        for(DamageEvent damageEvent : damageEvents) {
            String dealerIdStr = String.valueOf(damageEvent.getDealerUnitID());
            String receiverIdStr = String.valueOf(damageEvent.getReceiverUnitID());

            if (championDamageMap.containsKey(dealerIdStr)) {
                Map<String, Double> damageMap = championDamageMap.get(dealerIdStr);
                double doneUpdate = damageMap.get(DONE.toString()) + damageEvent.getDamage();
                damageMap.put(DONE.toString(), doneUpdate);
            } else {
                Map<String, Double> damageMap = new HashMap<String, Double>();
                damageMap.put(DONE.toString(), damageEvent.getDamage());
                damageMap.put(TAKEN.toString(), 0.0);
                championDamageMap.put(dealerIdStr,damageMap);
            }

            if(championDamageMap.containsKey(receiverIdStr)) {
                Map<String, Double> damageMap = championDamageMap.get(receiverIdStr);
                double receiveUpdate = damageMap.get(TAKEN.toString()) + damageEvent.getDamage();
                damageMap.put(TAKEN.toString(), receiveUpdate);
            } else {
                Map<String, Double> damageMap = new HashMap<String, Double>();
                damageMap.put(DONE.toString(), 0.0);
                damageMap.put(TAKEN.toString(), damageEvent.getDamage());

                championDamageMap.put(receiverIdStr, damageMap);
            }
        }

        int numberOfCharacters =  championDamageMap.size();

        DoubleWritable[] doneList = new DoubleWritable[numberOfCharacters];
        DoubleWritable[] takenList =  new DoubleWritable[numberOfCharacters];
        DoubleWritable[] healList = new DoubleWritable[numberOfCharacters];

        int characterIdCounter = 0;

        for(Map.Entry<String, Map<String, Double>> entry : championDamageMap.entrySet()) {
            Map<String, Double> valueMap = entry.getValue();

            DoubleWritable doneValueWrt = new DoubleWritable(valueMap.get(DONE.toString()));
            DoubleWritable takenValueWrt = new DoubleWritable(valueMap.get(TAKEN.toString()));
            DoubleWritable healValueWrt = new DoubleWritable(valueMap.get(DONE.toString())
                    -  valueMap.get(TAKEN.toString()));


            doneList[characterIdCounter] = doneValueWrt;
            takenList[characterIdCounter] = takenValueWrt;
            healList[characterIdCounter] = healValueWrt;

            characterIdCounter++;
        }

        MapWritable outWritable = new MapWritable();
        outWritable.put(DONE_LIST, new DoubleArrayWritable(doneList));
        outWritable.put(TAKEN_LIST,new DoubleArrayWritable(takenList));
        outWritable.put(HEAL_LIST, new DoubleArrayWritable(healList));

        context.write(new Text(gameId), outWritable);
    }

    private DataJsonMapper get(Text json) {
        return gson.fromJson(json.toString(), DataJsonMapper.class);
    }
}
