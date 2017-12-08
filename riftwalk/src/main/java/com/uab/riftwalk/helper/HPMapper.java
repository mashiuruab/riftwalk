package com.uab.riftwalk.helper;

import com.google.gson.Gson;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class HPMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    private Gson gson = new Gson();

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] splitted = value.toString().split("\\s+");

        if (splitted.length != 2) {
            return;
        }

        Text characterId = new Text(splitted[0]);
        DomainModel model = get(splitted[1]);

        /*context.write(characterId, new DoubleWritable(model.getPatch()));*/
        /*context.write(characterId, new DoubleWritable(model.getMmr()));*/
        context.write(characterId, new DoubleWritable(model.getRegion()));
    }

    private DomainModel get(String json) {
        return gson.fromJson(json, DomainModel.class);
    }
}
