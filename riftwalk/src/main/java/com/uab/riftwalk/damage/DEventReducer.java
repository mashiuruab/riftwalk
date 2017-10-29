package com.uab.riftwalk.damage;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class DEventReducer extends Reducer<Text, MapWritable, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<MapWritable> values, Context context)
            throws IOException, InterruptedException {
        super.reduce(key, values, context);
    }
}
