package com.uab.riftwalk.damage.game.topbottom;

import com.google.gson.Gson;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TBReducer extends Reducer<Text, DoubleWritable, Text, Text> {
    private Gson gson = new Gson();

    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context)
            throws IOException, InterruptedException {
        List<Double> cList = new ArrayList<Double>();

        for(DoubleWritable value : values) {
            cList.add(value.get());
        }

        String  listJson = gson.toJson(cList);

        context.write(key, new Text(listJson));
    }
}
