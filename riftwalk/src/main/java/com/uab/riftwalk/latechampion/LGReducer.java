package com.uab.riftwalk.latechampion;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class LGReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
    @Override
    protected void reduce(Text characterId, Iterable<DoubleWritable> values, Context context)
            throws IOException, InterruptedException {
        double totalBuffRatio  =  0;
        double totalGame =  0;

        for(DoubleWritable ratio  : values) {
            totalBuffRatio += ratio.get();
            totalGame++;
        }

        double calculatedRatio = totalBuffRatio / totalGame;

        context.write(characterId,  new DoubleWritable(calculatedRatio));
    }
}
