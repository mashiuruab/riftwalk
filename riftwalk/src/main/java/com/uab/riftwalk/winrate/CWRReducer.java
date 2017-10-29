package com.uab.riftwalk.winrate;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class CWRReducer extends Reducer<Text, IntWritable, Text, DoubleWritable> {

    @Override
    protected void reduce(Text characterId, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        double wonGame  =  0;
        double participation = 0;

        for(IntWritable value : values) {
            wonGame++;
            int participationValue = value.get();
            participation += participationValue;
        }

        if (wonGame == 0 || participation == 0) {
            return;
        }

        double winRate = wonGame / participation;

        context.write(characterId, new DoubleWritable(winRate));
    }
}
