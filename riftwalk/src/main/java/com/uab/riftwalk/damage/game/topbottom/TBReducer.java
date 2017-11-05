package com.uab.riftwalk.damage.game.topbottom;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class TBReducer extends Reducer<Text, DoubleWritable, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context)
            throws IOException, InterruptedException {

        if (!key.equals(TBMapper.SECOND_BOTTOM_HEAL)) {
            return;
        }

        for(DoubleWritable value : values) {
            context.write(key, new Text(value.toString()));
        }
    }
}
