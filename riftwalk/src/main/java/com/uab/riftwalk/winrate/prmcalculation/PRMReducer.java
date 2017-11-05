package com.uab.riftwalk.winrate.prmcalculation;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PRMReducer extends Reducer<Text,Text, Text, Text> {
    @Override
    protected void reduce(Text characterId, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        /*it should be only one  value in iterable */
        String outString  = "";
        for(Text json : values) {
            outString += json.toString();
        }

        context.write(characterId,  new Text(outString));
    }
}
