package com.uab.riftwalk.winrate.filter;

import com.google.gson.Gson;
import com.uab.riftwalk.model.JsonMapper;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonReducer extends Reducer<Text, MapWritable, Text, Text> {
    private static final String ALL_KEY = "all";

    private Gson gson = new Gson();
    private Text PARTICIPATION_WRITABLE = new Text(CommonMapper.PARTICIPATION_KEY);
    private Text REGION_WRITABLE = new Text(JsonMapper.REGION_KEY);
    private Text MMR_WRITABLE = new Text(JsonMapper.MMR_KEY);
    private Text PATCH_WRITABLE = new Text(JsonMapper.PATCH_KEY);

    @Override
    protected void reduce(Text characterId, Iterable<MapWritable> values, Context context)
            throws IOException, InterruptedException {

        Map<String, List<WinParticipation>> outMap =
                new HashMap<String, List<WinParticipation>>();
        outMap.put(ALL_KEY, new ArrayList<WinParticipation>());
        outMap.put(JsonMapper.REGION_KEY, new ArrayList<WinParticipation>());
        outMap.put(JsonMapper.PATCH_KEY, new ArrayList<WinParticipation>());
        outMap.put(JsonMapper.MMR_KEY, new ArrayList<WinParticipation>());

        Map<String, WinParticipation> regionMap = new HashMap<String, WinParticipation>();
        Map<String, WinParticipation> mmrMap = new HashMap<String, WinParticipation>();
        Map<String, WinParticipation> patchMap = new HashMap<String, WinParticipation>();
        Map<String, WinParticipation> allMap = new HashMap<String, WinParticipation>();

        WinParticipation allContainer = new WinParticipation(ALL_KEY);
        allMap.put(ALL_KEY, allContainer);

        for(MapWritable item : values) {
            Text participation = (Text) item.get(PARTICIPATION_WRITABLE);
            Text region = (Text) item.get(REGION_WRITABLE);
            Text mmr =  (Text) item.get(MMR_WRITABLE);
            Text patch = (Text) item.get(PATCH_WRITABLE);


            allContainer.incrementTotalWin();
            allContainer.addTotalParticipation(Double.valueOf(participation.toString()));

            WinParticipation regionContainer;
            WinParticipation mmrContainer;
            WinParticipation patchContainer;

            if (regionMap.containsKey(region.toString())) {
                regionContainer = regionMap.get(region.toString());
                regionContainer.incrementTotalWin();
                regionContainer.addTotalParticipation(Double.valueOf(participation.toString()));
            } else {
                regionContainer = new WinParticipation(region.toString());
                regionContainer.incrementTotalWin();
                regionContainer.addTotalParticipation(Double.valueOf(participation.toString()));
                regionMap.put(region.toString(), regionContainer);
            }

            if (mmrMap.containsKey(mmr.toString())) {
                mmrContainer = mmrMap.get(mmr.toString());
                mmrContainer.incrementTotalWin();
                mmrContainer.addTotalParticipation(Double.valueOf(participation.toString()));
            } else {
                mmrContainer = new WinParticipation(mmr.toString());
                mmrContainer.incrementTotalWin();
                mmrContainer.addTotalParticipation(Double.valueOf(participation.toString()));
                mmrMap.put(mmr.toString(), mmrContainer);
            }

            if (patchMap.containsKey(patch.toString())) {
                patchContainer = patchMap.get(patch.toString());
                patchContainer.incrementTotalWin();
                patchContainer.addTotalParticipation(Double.valueOf(participation.toString()));
            } else {
                patchContainer = new WinParticipation(patch.toString());
                patchContainer.incrementTotalWin();
                patchContainer.addTotalParticipation(Double.valueOf(participation.toString()));
                patchMap.put(patch.toString(), patchContainer);
            }

        }


        outMap.get(ALL_KEY).addAll(allMap.values());
        outMap.get(JsonMapper.REGION_KEY).addAll(regionMap.values());
        outMap.get(JsonMapper.MMR_KEY).addAll(mmrMap.values());
        outMap.get(JsonMapper.PATCH_KEY).addAll(patchMap.values());

        for(Map.Entry<String, List<WinParticipation>> entry : outMap.entrySet()) {
            for(WinParticipation target : entry.getValue()) {
                target.calculateWinRate();
            }
        }

        context.write(characterId, new Text(gson.toJson(outMap)));
    }
}
