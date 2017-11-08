package com.uab.riftwalk.winrate.prmcalculation;

import com.google.gson.Gson;
import com.uab.riftwalk.model.JsonMapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PRMMapper extends Mapper<LongWritable, Text, Text, Text> {
    private Gson  gson = new Gson();

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] jsonInfo  =  value.toString().split("\\s+");

        if(jsonInfo.length != 2) {
            System.err.println("json not splitted properly");
            return;
        }

        Text characterId  = new Text(jsonInfo[0]);
        DataModelContainer container =  get(jsonInfo[1]);

        /*This  1 / denominator is need to done  for a error  in previous pass*/
        Double all = 1 / container.getAll().get(0).getWinRate();
        Double regionWiseWinRate = getWinrate(container.getRegion());
        Double patchWiseWinRate = getWinrate(container.getPatch());
        Double mmrWiseWinRate = getWinrate(container.getMmr());

        Map<String, Double> outMap = new HashMap<String, Double>();
        outMap.put("all", all);
        outMap.put(JsonMapper.REGION_KEY,  regionWiseWinRate);
        outMap.put(JsonMapper.MMR_KEY,  mmrWiseWinRate);
        outMap.put(JsonMapper.PATCH_KEY, patchWiseWinRate);

        Text outText = new Text(gson.toJson(outMap));

        context.write(characterId, outText);
    }

    private Double getWinrate(List<DataModel> modelList) {
        Double winrateSum = 0.0;
        Double totalBucket = 0.0;

        for (DataModel model : modelList) {
            winrateSum += 1 / model.getWinRate();
            totalBucket++;
        }

        if (winrateSum ==  0 || totalBucket == 0) {
            return 0.0;
        }

        return winrateSum / totalBucket;
    }

    private DataModelContainer get(String jsonString) {
        return gson.fromJson(jsonString, DataModelContainer.class);
    }
}
