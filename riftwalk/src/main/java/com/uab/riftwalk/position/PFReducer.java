package com.uab.riftwalk.position;

import com.google.gson.Gson;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PFReducer extends Reducer<Text, MapWritable, Text, Text> {
    private Gson gson  = new Gson();

    @Override
    protected void reduce(Text characterId, Iterable<MapWritable> values, Context context)
            throws IOException, InterruptedException {
        double tsDistance = 0;
        double tsGame = 0;

        double tmDistance = 0;
        double tmGame = 0;

        double teDistance = 0;
        double teGame = 0;

        for(MapWritable timeMap : values) {
            if (timeMap.containsKey(PFMapper.START_KEY)) {
                Double distance = ((DoubleWritable) timeMap.get(PFMapper.START_KEY)).get();
                tsDistance += distance;
                tsGame++;
            }

            if(timeMap.containsKey(PFMapper.MIDDLE_KEY)) {
                Double distance = ((DoubleWritable) timeMap.get(PFMapper.MIDDLE_KEY)).get();
                tmDistance += distance;
                tmGame++;
            }

            if (timeMap.containsKey(PFMapper.END_KEY)) {
                Double distance = ((DoubleWritable) timeMap.get(PFMapper.END_KEY)).get();
                teDistance += distance;
                teGame++;
            }
        }

        double tsRate = tsDistance / tsGame;
        double tmRate = tmDistance / tmGame;
        double teRate = teDistance / teGame;

        Map<String, Double> outMap = new HashMap<String, Double>();
        outMap.put(PFMapper.START_KEY.toString(), tsRate);
        outMap.put(PFMapper.MIDDLE_KEY.toString(), tmRate);
        outMap.put(PFMapper.END_KEY.toString(), teRate);

        Text outJSon = new Text(gson.toJson(outMap));
        context.write(characterId,  outJSon);
    }

}
