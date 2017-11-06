package com.uab.riftwalk.probability.gold;

import com.google.gson.Gson;
import com.uab.riftwalk.model.DataJsonMapper;
import com.uab.riftwalk.model.EarnedEvent;
import com.uab.riftwalk.winrate.Player;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoldMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private Gson gson = new Gson();

    private static final IntWritable ONE = new IntWritable(1);
    public static final Text GOLD_MINUTE_5 = new Text("GOLD_5");
    public static final Text GOLD_MINUTE_10 = new Text("GOLD_10");
    public static final Text GOLD_MINUTE_15 = new Text("GOLD_15");

    public static final Text EXP_MINUTE_5 = new Text("EXP_5");
    public static final Text EXP_MINUTE_10 = new Text("EXP_10");
    public static final Text EXP_MINUTE_15 = new Text("EXP_15");

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        DataJsonMapper jsonMapper = get(value);
        String winnerTeam = jsonMapper.getWinner();

        List<Player> playerList = jsonMapper.getPlayers();
        Map<Integer, Double> winnerGoldCharacterMap = new HashMap<Integer, Double>();
        Map<Integer, Double> loserGoldCharacterMap = new HashMap<Integer, Double>();

        Map<Integer, Double> winnerExpCharacterMap = new HashMap<Integer, Double>();
        Map<Integer, Double> loserExpCharacterMap = new HashMap<Integer, Double>();

        for(Player player : playerList) {
            if (player.getTeamID().equals(winnerTeam)) {
                winnerGoldCharacterMap.put(Integer.parseInt(player.getChampionID()), 0.0);
                winnerExpCharacterMap.put(Integer.parseInt(player.getChampionID()), 0.0);
            } else {
                loserGoldCharacterMap.put(Integer.parseInt(player.getChampionID()), 0.0);
                loserExpCharacterMap.put(Integer.parseInt(player.getChampionID()), 0.0);
            }
        }

        List<EarnedEvent> goldEventList = jsonMapper.getData().getGoldEarnedEvents();
        List<EarnedEvent> experienceEventList  = jsonMapper.getData().getExperienceEarnedEvents();

        for(EarnedEvent gold : goldEventList) {
            if (winnerGoldCharacterMap.containsKey(gold.getUnitID())
                    && winnerGoldCharacterMap.get(gold.getUnitID()) < gold.getTime()) {

                winnerGoldCharacterMap.put(gold.getUnitID(), gold.getTime());
            } else  if (loserGoldCharacterMap.containsKey(gold.getUnitID())
                    && loserGoldCharacterMap.get(gold.getUnitID()) < gold.getTime()) {

                loserGoldCharacterMap.put(gold.getUnitID(), gold.getTime());
            }
        }

        Double winnerTeamsTime = 0.0;
        Double loserTeamsTime = 0.0;

        for(Map.Entry<Integer, Double> entry : winnerGoldCharacterMap.entrySet()) {
            winnerTeamsTime += entry.getValue();
        }

        for (Map.Entry<Integer, Double> entry : loserGoldCharacterMap.entrySet()) {
            loserTeamsTime += entry.getValue();
        }

        Double timeDifference = winnerTeamsTime - loserTeamsTime;
        if (timeDifference >= 5) {
            context.write(GOLD_MINUTE_5, ONE);
        }

        if (timeDifference >= 10) {
            context.write(GOLD_MINUTE_10, ONE);
        }

        if (timeDifference >= 15) {
            context.write(GOLD_MINUTE_15, ONE);
        }

        for(EarnedEvent experience : experienceEventList) {
            if (winnerExpCharacterMap.containsKey(experience.getUnitID())
                    && winnerExpCharacterMap.get(experience.getUnitID()) < experience.getTime()) {

                winnerExpCharacterMap.put(experience.getUnitID(), experience.getTime());
            } else  if (loserExpCharacterMap.containsKey(experience.getUnitID())
                    && loserExpCharacterMap.get(experience.getUnitID()) < experience.getTime()) {

                loserExpCharacterMap.put(experience.getUnitID(), experience.getTime());
            }
        }

        winnerTeamsTime = 0.0;
        loserTeamsTime = 0.0;

        for(Map.Entry<Integer, Double>  entry : winnerExpCharacterMap.entrySet()) {
            winnerTeamsTime += entry.getValue();
        }

        for(Map.Entry<Integer, Double> entry : loserExpCharacterMap.entrySet()) {
            loserTeamsTime += entry.getValue();
        }

        timeDifference = winnerTeamsTime - loserTeamsTime;

        if (timeDifference >= 5) {
            context.write(EXP_MINUTE_5, ONE);
        }

        if (timeDifference >= 10) {
            context.write(EXP_MINUTE_10, ONE);
        }

        if (timeDifference >= 15) {
            context.write(EXP_MINUTE_15, ONE);
        }
    }

    private DataJsonMapper get(Text json) {
        return gson.fromJson(json.toString(), DataJsonMapper.class);
    }
}
