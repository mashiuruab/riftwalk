package com.uab.riftwalk.probability.gold;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class GoldReducer extends Reducer<Text, IntWritable,  Text, DoubleWritable> {
    private static final Double TOTAL_GAME = 41518.0;
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        double totalCount = 0.0;

        for (IntWritable value : values) {
            totalCount++;
        }

        double winProbability = totalCount / TOTAL_GAME;

        context.write(key, new DoubleWritable(winProbability));
    }
}
