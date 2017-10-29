package com.uab.riftwalk.damage;

import com.google.gson.Gson;
import com.uab.riftwalk.model.DamageEvent;
import com.uab.riftwalk.model.DamageJsonMapper;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DEventMapper extends Mapper<LongWritable, Text, Text, MapWritable> {
    private Gson gson = new Gson();

    public static Text DONE = new Text("done");
    public static Text TAKEN = new Text("taken");

    @Override
    protected void map(LongWritable key, Text json, Context context)
            throws IOException, InterruptedException {
        DamageJsonMapper jsonMapper = get(json);

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

        for(Map.Entry<String, Map<String, Double>> entry : championDamageMap.entrySet()) {
            Text characterId = new Text(entry.getKey());
            Map<String, Double> valueMap = entry.getValue();

            MapWritable outWritable = new MapWritable();

            for(Map.Entry<String, Double> valueEntry : valueMap.entrySet()) {
                String doneOrTaken = valueEntry.getKey();
                Double damageValue = valueEntry.getValue();

                if (doneOrTaken.equals(DONE.toString())) {
                    outWritable.put(DONE, new DoubleWritable(damageValue));
                } else if (doneOrTaken.equals(TAKEN.toString())) {
                    outWritable.put(TAKEN, new DoubleWritable(damageValue));
                }
            }

            context.write(characterId, outWritable);
        }
    }

    private DamageJsonMapper get(Text json) {
        return gson.fromJson(json.toString(), DamageJsonMapper.class);
    }
}
