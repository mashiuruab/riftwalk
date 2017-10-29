package com.uab.riftwalk.damage;

import com.google.gson.Gson;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DEventReducer extends Reducer<Text, MapWritable, Text, Text> {
    private Gson gson = new Gson();

    @Override
    protected void reduce(Text characterId, Iterable<MapWritable> values, Context context)
            throws IOException, InterruptedException {

        double totalDoneDamage = 0;
        double totalTakenDamage = 0;

        for(MapWritable valueMap : values) {
            DoubleWritable doneDamage = (DoubleWritable) valueMap.get(DEventMapper.DONE);
            DoubleWritable takenDamage = (DoubleWritable) valueMap.get(DEventMapper.TAKEN);

            totalDoneDamage += doneDamage.get();
            totalTakenDamage += takenDamage.get();
        }

        double heal = Math.abs(totalDoneDamage - totalTakenDamage);

        Map<String, Double> outMap = new HashMap<String, Double>();
        outMap.put(DEventMapper.DONE.toString(), totalDoneDamage);
        outMap.put(DEventMapper.TAKEN.toString(), totalTakenDamage);
        outMap.put("heal", heal);

        context.write(characterId, new Text(gson.toJson(outMap)));
    }
}
