package com.uab.riftwalk.winrate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CWRMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private Gson gson = new Gson();
    Type type = new TypeToken<Map<String, Object>>(){}.getType();

    private static final String PLAYERS_KEY = "players";

    public static final String WINNER_KEY = "winner";
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
                infoMap.put(WINNER_KEY, String.valueOf(Boolean.FALSE));

                characterInfoMap.put(player.getChampionID(), infoMap);
            }

            Map<String,  String> infoMap =  characterInfoMap.get(player.getChampionID());

            if (winnerTeamId.equals(player.getTeamID())) {
                infoMap.put(WINNER_KEY, String.valueOf(Boolean.TRUE));
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

            if (!Boolean.valueOf(entryValue.get(WINNER_KEY))) {
                continue;
            }

            IntWritable participation = new IntWritable(Integer.valueOf(
                    entryValue.get(PARTICIPATION_KEY)));

            context.write(characterId, participation);
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


    private Map<String, Object> get(Text json) {
        return gson.fromJson(json.toString(), type);
    }
}
