package com.example.demo.controller;

import com.twilio.Twilio;
import com.example.demo.entity.MyCall;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;


import static org.hibernate.internal.util.io.StreamCopier.BUFFER_SIZE;

public class StatsCounter {
    public static String SID = "AC846e08c607dd26c9c7a0ff5c0a6cba67";
    public static String auth_token = "e318557b50dbd6f2fdf1b589c245dddb";
    private List<MyCall> list;
    private Double AvgDuration = 0.0;
    private Double AvgQueue = 0.0;
    private HashMap<Integer, HashMap<String, Integer>> activity = new HashMap<Integer, HashMap<String, Integer>>();
    private HashMap<String, Integer> cathegories = new HashMap<String, Integer>();
    private Double FailureRate = 0.0;
    private Double SuccessRate = 0.0;
    private String b64 = "";
    private String b65 = "";

    public StatsCounter(List<MyCall> list) throws IOException {
        Twilio.init(SID, auth_token);
        this.list = list;
        for (int i = 0; i <= 23; i = i + 1) {
            HashMap<String, Integer> c = new HashMap<String, Integer>();
            for (MyCall call : list){
                if (!c.containsKey(call.getClientNumber())) {
                    c.put(call.getClientNumber(), 0);
                }
            }
            activity.put(i, c);
        }
        cathegories.putIfAbsent("completed", 0);
        list.forEach(x -> {
            this.AvgDuration += Double.parseDouble(x.getDuration());
            this.AvgQueue += Double.parseDouble(x.getQueueTime());
            int hour = Integer.parseInt(x.getStarTime().substring(11,13));
            this.activity.get(hour).replace(x.getClientNumber(),activity.get(hour).get(x.getClientNumber()) +1);
            if (!cathegories.containsKey(x.getStatus())){
                cathegories.put(x.getStatus(),0);
            }
            this.cathegories.replace(x.getStatus(), cathegories.get(x.getStatus()) +1);

        });
        AvgDuration = AvgDuration/list.size();
        AvgQueue = AvgQueue/list.size();
        FailureRate = 0.0;
        if(list.size()!=0){
            FailureRate = 100.00 - (double)(100*cathegories.get("completed") / list.size());
        }
        SuccessRate = 100.00 - FailureRate;
        DefaultPieDataset dataset = new DefaultPieDataset();
        cathegories.keySet().forEach(x -> {
            dataset.setValue(x, cathegories.get(x));
        });
        JFreeChart chart = ChartFactory.createPieChart("Calls by their status", dataset);
        BufferedImage img = chart.createBufferedImage(300,300);
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        ImageIO.write(img, "png", out);
        b64 = "data:image/png;base64,"+Base64.getEncoder().encodeToString(out.toByteArray());
        DefaultCategoryDataset cat = new DefaultCategoryDataset();
        activity.keySet().forEach(x -> {
            activity.get(x).keySet().forEach(y -> {
                cat.setValue(activity.get(x).get(y), y, x);
            });

        });
        JFreeChart chart2 = ChartFactory.createBarChart("Activity by hours",
                "Hour",
                "Number of Calls",
                cat,
                PlotOrientation.VERTICAL,true,true,false
                );

        BufferedImage img2 = chart2.createBufferedImage(1200,500);
        ByteArrayOutputStream out2 = new ByteArrayOutputStream(BUFFER_SIZE);
        ImageIO.write(img2, "png", out2);
        b65 = "data:image/png;base64,"+Base64.getEncoder().encodeToString(out2.toByteArray());
    }

    public Integer getCount(String name){
        cathegories.putIfAbsent(name, 0);
        return cathegories.get(name);
    }

    public Integer getCountcompleted(){
        return getCount("completed");
    }
    public Integer getCountnoanswer(){
        return getCount("no-answer");
    }
    public Integer getCountbusy(){
        return getCount("busy");
    }
    public Integer getCountqueued(){
        return getCount("queued");
    }
    public Integer getCountinprogress(){
        return getCount("in-progress");
    }
    public List<MyCall> getList() {
        return list;
    }

    public Double getAvgDuration() {
        return AvgDuration;
    }

    public void setAvgDuration(Double avgDuration) {
        AvgDuration = avgDuration;
    }

    public Double getAvgQueue() {
        return AvgQueue;
    }

    public void setAvgQueue(Double avgQueue) {
        AvgQueue = avgQueue;
    }

    public HashMap<Integer, HashMap<String, Integer>> getActivity() {
        return activity;
    }

    public void setActivity(HashMap<Integer, HashMap<String, Integer>> activity) {
        this.activity = activity;
    }

    public HashMap<String, Integer> getCathegories() {
        return cathegories;
    }

    public void setCathegories(HashMap<String, Integer> cathegories) {
        this.cathegories = cathegories;
    }

    public Double getFailureRate() {
        return FailureRate;
    }

    public void setFailureRate(Double failureRate) {
        FailureRate = failureRate;
    }

    public Double getSuccessRate() {
        return SuccessRate;
    }

    public void setSuccessRate(Double successRate) {
        SuccessRate = successRate;
    }

    public String getB64() {
        return b64;
    }

    public String getB65() {
        return b65;
    }

    public void setB65(String b65) {
        this.b65 = b65;
    }

    public void setB64(String b64) {
        this.b64 = b64;
    }
}
