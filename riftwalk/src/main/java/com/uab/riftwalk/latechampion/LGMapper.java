package com.uab.riftwalk.latechampion;

import com.google.gson.Gson;
import com.uab.riftwalk.model.BuffEvent;
import com.uab.riftwalk.model.DataJsonMapper;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LGMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    private Gson gson = new Gson();

    @Override
    protected void map(LongWritable lineNumber, Text json, Context context)
            throws IOException, InterruptedException {
        DataJsonMapper dataJsonMapper = get(json);
        Double gameLength = dataJsonMapper.getLength();

        List<BuffEvent> buffGainedEvents = dataJsonMapper.getData().getBuffGainedEvents();
        List<BuffEvent> buffLostEvents = dataJsonMapper.getData().getBuffLostEvents();

        Map<Integer, Double> characterBuffMap = new HashMap<Integer, Double>();

        for(BuffEvent gainedEvent : buffGainedEvents) {
            Integer characterId = gainedEvent.getUnitID();
            Double buffGained = gainedEvent.getBuffName();

            if (characterBuffMap.containsKey(characterId)) {
                Double updateBuff = characterBuffMap.get(characterId) + buffGained;
                characterBuffMap.put(characterId,  updateBuff);
            } else {
                characterBuffMap.put(characterId, buffGained);
            }
        }

        for(BuffEvent lostEvent : buffLostEvents) {
            Integer characterId = lostEvent.getUnitID();
            Double buffLost = lostEvent.getBuffName();

            if (characterBuffMap.containsKey(characterId)) {
                Double updateBuff = characterBuffMap.get(characterId) - buffLost;
                characterBuffMap.put(characterId, updateBuff);
            } else {
                characterBuffMap.put(characterId,  -buffLost);
            }
        }

        for (Map.Entry<Integer, Double> entry : characterBuffMap.entrySet()) {
            Text characterWritable = new Text(entry.getKey().toString());
            Double buffRatio =  entry.getValue()  /  gameLength;
            DoubleWritable buffRatioWritable = new DoubleWritable(buffRatio);

            context.write(characterWritable, buffRatioWritable);
        }

    }

    private DataJsonMapper get(Text json) {
        return gson.fromJson(json.toString(), DataJsonMapper.class);
    }
}
