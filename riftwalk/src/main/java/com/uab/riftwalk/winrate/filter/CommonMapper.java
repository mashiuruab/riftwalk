package com.uab.riftwalk.winrate.filter;

import com.google.gson.Gson;
import com.uab.riftwalk.winrate.JsonMapper;
import com.uab.riftwalk.winrate.Player;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonMapper extends Mapper<LongWritable, Text, Text, MapWritable> {
    private Gson gson = new Gson();

    public static final String PARTICIPATION_KEY = "participation";

    @Override
    protected void map(LongWritable key, Text json, Context context)
            throws IOException, InterruptedException {
        JsonMapper mapperObject = getJsonMapper(json);


        String winnerTeamId =  mapperObject.getWinner();

        if (StringUtils.isEmpty(winnerTeamId)) {
            System.out.println(">>>>>>>>>>>>>>> winner team id not found >>>>>>>>>");
            return;
        }

        List<Player> playerList = mapperObject.getPlayers();


        Map<String, Map<String, String>> characterInfoMap =
                new HashMap<String, Map<String, String>>();

        for(Player player : playerList) {
            if (!characterInfoMap.containsKey(player.getChampionID())) {
                Map<String, String> infoMap =  new HashMap<String, String>();
                infoMap.put(JsonMapper.WINNER_KEY, String.valueOf(Boolean.FALSE));
                infoMap.put(JsonMapper.REGION_KEY, mapperObject.getRegion());
                infoMap.put(JsonMapper.MMR_KEY, mapperObject.getMmr());
                infoMap.put(JsonMapper.PATCH_KEY, String.valueOf(mapperObject.getPatch()));

                characterInfoMap.put(player.getChampionID(), infoMap);
            }

            Map<String,  String> infoMap =  characterInfoMap.get(player.getChampionID());

            if (winnerTeamId.equals(player.getTeamID())) {
                infoMap.put(JsonMapper.WINNER_KEY, String.valueOf(Boolean.TRUE));
            }

            if(infoMap.containsKey(PARTICIPATION_KEY)) {
                int participation = Integer.valueOf(infoMap.get(PARTICIPATION_KEY)) + 1;
                infoMap.put(PARTICIPATION_KEY, String.valueOf(participation));
            } else {
                infoMap.put(PARTICIPATION_KEY, String.valueOf(1));
            }

        }

        for(Map.Entry<String, Map<String, String>> entry : characterInfoMap.entrySet()) {
            Text characterId = new Text(entry.getKey());
            Map<String, String> entryValue = entry.getValue();

            if (!Boolean.valueOf(entryValue.get(JsonMapper.WINNER_KEY))) {
                continue;
            }

            MapWritable outWritable = new MapWritable();

            for(Map.Entry<String, String> outEntry : entryValue.entrySet()) {
                Text keyWritable = new Text(outEntry.getKey());
                Text valueWritable = new Text(outEntry.getValue());

                outWritable.put(keyWritable, valueWritable);
            }

            context.write(characterId, outWritable);
        }
    }

    private JsonMapper getJsonMapper(Text json) {
        JsonMapper jsonMapper;

        try {
            jsonMapper = gson.fromJson(json.toString(), JsonMapper.class);
        } catch (Exception e) {
            System.out.println(String.format("player list json %s", json.toString()));
            throw new RuntimeException(e);
        }

        return jsonMapper;
    }
}
