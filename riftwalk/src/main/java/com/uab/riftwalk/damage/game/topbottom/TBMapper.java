package com.uab.riftwalk.damage.game.topbottom;

import com.google.gson.Gson;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;

public class TBMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    private Gson gson = new Gson();

    public Text TOP_DONE =  new Text("top-done");
    public Text SECOND_TOP_DONE =  new Text("2nd-top-done");
    public Text BOTTOM_DONE =  new Text("bottom-done");
    public Text SECOND_BOTTOM_DONE =  new Text("2nd-bottom-done");

    public Text TOP_TAKEN =  new Text("top-taken");
    public Text SECOND_TOP_TAKEN =  new Text("2nd-top-taken");
    public Text BOTTOM_TAKEN =  new Text("bottom-taken");
    public Text SECOND_BOTTOM_TAKEN =  new Text("2nd-bottom-taken");

    public Text TOP_HEAL =  new Text("top-heal");
    public Text SECOND_TOP_HEAL =  new Text("2nd-top-heal");
    public Text BOTTOM_HEAL =  new Text("bottom-heal");
    public Text SECOND_BOTTOM_HEAL =  new Text("2nd-bottom-heal");


    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[]  jsonInfo =  value.toString().split("\\s+");

        if (jsonInfo.length != 2) {
            System.err.println("Error while json parsing");
            return;
        }

        Text gameId = new Text(jsonInfo[0]);
        DataModel dataModel = get(jsonInfo[1]);


        List<Double> doneList = dataModel.getDoneList();
        double topDone  = 0.0;
        double bottomDone = 0.0;
        double secondTopDone = 0.0;
        double secondBottomDone  = 0.0;

        if (doneList.size() == 3) {
            topDone  = doneList.get(0);
            bottomDone = doneList.get(doneList.size() - 1);

            secondTopDone = doneList.get(1);
            secondBottomDone = doneList.get(1);
        } else if (doneList.size() == 1) {
            topDone  = doneList.get(0);
            bottomDone = doneList.get(doneList.size() - 1);

            secondTopDone = doneList.get(0);
            secondBottomDone = doneList.get(0);
        } else if (doneList.isEmpty()) {
            topDone  = 0.0;
            bottomDone = 0.0;

            secondTopDone = 0.0;
            secondBottomDone = 0.0;
        } else {
            topDone  = doneList.get(0);
            bottomDone = doneList.get(doneList.size() - 1);

            secondTopDone =  doneList.get(1);
            secondBottomDone =  doneList.get(doneList.size() - 2);
        }

        List<Double> takenList = dataModel.getTakenList();
        double topTaken  = 0.0;
        double bottomTaken = 0.0;
        double secondTopTaken = 0.0;
        double secondBottomTaken  = 0.0;

        if (takenList.size() == 3) {
            topTaken  = takenList.get(0);
            bottomTaken = takenList.get(takenList.size() - 1);

            secondTopTaken = takenList.get(1);
            secondBottomTaken = takenList.get(1);

        } else if (takenList.size() == 1) {
            topTaken  = takenList.get(0);
            bottomTaken = takenList.get(0);

            secondTopTaken = takenList.get(0);
            secondBottomTaken = takenList.get(0);
        } else if (takenList.isEmpty()) {
            topTaken  = 0.0;
            bottomTaken = 0.0;

            secondTopTaken = 0.0;
            secondBottomTaken = 0.0;
        } else {
            topTaken  = takenList.get(0);
            bottomTaken = takenList.get(takenList.size() - 1);

            secondTopTaken =  takenList.get(1);
            secondBottomTaken =  takenList.get(takenList.size() - 2);
        }


        List<Double> healList = dataModel.getHealList();

        double topHeal  = 0.0;
        double bottomHeal = 0.0;
        double secondTopHeal = 0.0;
        double secondBottomHeal  = 0.0;

        if (healList.size() == 3) {
            topHeal  = healList.get(0);
            bottomHeal = healList.get(healList.size() - 1);

            secondTopHeal = healList.get(1);
            secondBottomHeal = healList.get(1);
        } else if (healList.size() == 1) {
            topHeal  = healList.get(0);
            bottomHeal = healList.get(0);

            secondTopHeal = healList.get(0);
            secondBottomHeal = healList.get(0);
        } else if (healList.isEmpty()) {
            topHeal  = 0.0;
            bottomHeal = 0.0;

            secondTopHeal = 0.0;
            secondBottomHeal = 0.0;
        } else {
            topHeal  = healList.get(0);
            bottomHeal = healList.get(healList.size() - 1);

            secondTopHeal =  healList.get(1);
            secondBottomHeal =  healList.get(healList.size() - 2);
        }

        context.write(TOP_DONE, new DoubleWritable(topDone));
        context.write(SECOND_TOP_DONE, new DoubleWritable(secondTopDone));
        context.write(BOTTOM_DONE, new DoubleWritable(bottomDone));
        context.write(SECOND_BOTTOM_DONE,  new DoubleWritable(secondBottomDone));

        context.write(TOP_TAKEN, new DoubleWritable(topTaken));
        context.write(SECOND_TOP_TAKEN, new DoubleWritable(secondTopTaken));
        context.write(BOTTOM_TAKEN, new DoubleWritable(bottomTaken));
        context.write(SECOND_BOTTOM_TAKEN, new DoubleWritable(secondBottomTaken));

        context.write(TOP_HEAL, new DoubleWritable(topHeal));
        context.write(SECOND_TOP_HEAL,  new DoubleWritable(secondTopHeal));
        context.write(BOTTOM_HEAL, new DoubleWritable(bottomHeal));
        context.write(SECOND_BOTTOM_HEAL,  new DoubleWritable(secondBottomHeal));

    }

    private DataModel get(String jsonString) {
        return gson.fromJson(jsonString, DataModel.class);
    }
}
