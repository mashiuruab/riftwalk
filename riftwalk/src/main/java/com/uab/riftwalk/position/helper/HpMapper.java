package com.uab.riftwalk.position.helper;

import com.google.gson.Gson;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class HpMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    private Gson gson = new Gson();

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] info =  value.toString().split("\\s+");

        if (info.length != 2) {
            System.err.println("Splitting Error");
        }

        String characterId = info[0];
        TmpModel tmpModel = get(info[1]);

        /*context.write(new Text(characterId), new DoubleWritable(tmpModel.getStart()));*/

        /*context.write(new Text(characterId), new DoubleWritable(tmpModel.getMiddle()));*/

        context.write(new Text(characterId), new DoubleWritable(tmpModel.getEnd()));
    }

    private TmpModel get(String  jsonString) {
        return gson.fromJson(jsonString, TmpModel.class);
    }
}
