package com.uab.riftwalk.position;

import com.google.gson.Gson;
import com.uab.riftwalk.model.DataJsonMapper;
import com.uab.riftwalk.model.position.PUnit;
import com.uab.riftwalk.model.position.PositionFrames;
import com.uab.riftwalk.model.position.PositionXY;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PFMapper extends Mapper<LongWritable, Text, Text, MapWritable> {
    private Gson gson = new Gson();

    public static final Text START_KEY = new Text("start");
    public static final Text MIDDLE_KEY = new Text("middle");
    public static final Text END_KEY =  new Text("end");

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        DataJsonMapper jsonMapper = get(value);

        if (jsonMapper.getLength() < 20) {
            return;
        }

        List<PositionFrames> positionFramesList = jsonMapper.getData().getPositionFrames();

        PositionFrames startPositionInFrame = positionFramesList.get(1);
        PositionFrames middlePositionInFrame =
                positionFramesList.get(positionFramesList.size() / 2);
        PositionFrames endPositionInFrame =
                positionFramesList.get(positionFramesList.size() - 2);

        Map<Integer,  Map<String, Double>>  characterPositionMap  =
                new HashMap<Integer, Map<String, Double>>();

        for (PUnit playerPosition : startPositionInFrame.getPlayerPositions()) {
            if (!characterPositionMap.containsKey(playerPosition.getUnitID())) {
                Map<String, Double> positionMap = new HashMap<String, Double>();
                characterPositionMap.put(playerPosition.getUnitID(), positionMap);
            }

            characterPositionMap.get(playerPosition.getUnitID())
                    .put(START_KEY.toString(), distance(playerPosition.getPosition()));
        }

        for (PUnit playerPosition : middlePositionInFrame.getPlayerPositions()) {
            if (!characterPositionMap.containsKey(playerPosition.getUnitID())) {
                Map<String, Double> positionMap = new HashMap<String, Double>();
                characterPositionMap.put(playerPosition.getUnitID(), positionMap);
            }

            characterPositionMap.get(playerPosition.getUnitID())
                    .put(MIDDLE_KEY.toString(), distance(playerPosition.getPosition()));
        }

        for (PUnit playerPosition : endPositionInFrame.getPlayerPositions()) {
            if (!characterPositionMap.containsKey(playerPosition.getUnitID())) {
                Map<String, Double> positionMap = new HashMap<String, Double>();
                characterPositionMap.put(playerPosition.getUnitID(), positionMap);
            }

            characterPositionMap.get(playerPosition.getUnitID())
                    .put(END_KEY.toString(), distance(playerPosition.getPosition()));
        }

        for(Map.Entry<Integer, Map<String, Double>> entry : characterPositionMap.entrySet()) {
            Text characterId = new Text(String.valueOf(entry.getKey()));

            MapWritable outWritable = new MapWritable();

            for(Map.Entry<String, Double> timeEntry : entry.getValue().entrySet()) {
                String keyString = timeEntry.getKey();
                DoubleWritable distance = new DoubleWritable(timeEntry.getValue());

                if (keyString.equals(START_KEY.toString())) {
                    outWritable.put(START_KEY, distance);
                } else if(keyString.equals(MIDDLE_KEY.toString())) {
                    outWritable.put(MIDDLE_KEY,  distance);
                } else if(keyString.equals(END_KEY.toString())) {
                    outWritable.put(END_KEY, distance);
                }
            }

            context.write(characterId, outWritable);
        }
    }

    private Double distance(PositionXY positionXY) {
        int x = positionXY.getX();
        int y = positionXY.getY();
        double sum = x * x + y * y;
        return Math.sqrt(sum);
    }

    private DataJsonMapper get(Text json) {
        return gson.fromJson(json.toString(), DataJsonMapper.class);
    }
}
