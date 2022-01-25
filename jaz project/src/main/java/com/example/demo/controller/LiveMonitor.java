package com.example.demo.controller;

import com.twilio.base.ResourceSet;

import com.twilio.rest.api.v2010.account.Call;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import com.example.demo.entity.MyCall;
import com.twilio.Twilio;


public class LiveMonitor {
    public static String SID = "AC846e08c607dd26c9c7a0ff5c0a6cba67";
    public static String auth_token = "e318557b50dbd6f2fdf1b589c245dddb";
    public static HashMap<String, String> alreadydone = new HashMap<String, String>();
    public static HashMap<String, String> ourClients = new HashMap<String, String>();
    public static HashMap<String, String> ourClientsTalking = new HashMap<String, String>();

    public static HashMap<String, String> getOurClients() {
        return ourClients;
    }

    public static List<MyCall> getDateCalls(){
        Twilio.init(SID, auth_token);
        ZonedDateTime time = ZonedDateTime.now().minusDays(1);
        ResourceSet<Call> list = Call.reader().setStartTimeAfter(time).read();
        List<MyCall> mycalls = new ArrayList<>();
        for (Call call : list) {
            String duration = call.getDuration();
            if (call.getParentCallSid() != null && call.getTo().contains("client")) {
                if (!ourClients.containsKey(call.getTo())){
                    ourClients.put(call.getTo(), "https://upload.wikimedia.org/wikipedia/commons/0/0e/Ski_trail_rating_symbol-green_circle.svg");
                }
                if (call.getStatus().toString() == "in-progress"
                        || call.getStatus().toString() == "queued" ){
                    ourClients.replace(call.getTo(),"https://images.squarespace-cdn.com/content/v1/553f30aee4b0abb691b9c149/1557202379148-VJWBLQJC1W2NJJ58L2A1/Chatbot.gif");
                    ourClientsTalking.put(call.getTo(),call.getParentCallSid());
                }
                if (call.getStatus().toString() == "in-progress"
                        || !alreadydone.containsKey(call.getParentCallSid()) ) {
                    ZonedDateTime start = Call.fetcher(call.getParentCallSid()).fetch().getStartTime();
                    String queue = String.valueOf(Duration.between(start,call.getStartTime()).toSeconds());
                    alreadydone.put(call.getParentCallSid(), queue);
                }
                if (!Objects.equals(call.getStatus().toString(), "completed" )
                        &&!Objects.equals(call.getStatus().toString(), "busy" )
                        &&!Objects.equals(call.getStatus().toString(), "no-answer" )
                        && Objects.equals(duration, "0")){
                    duration = String.valueOf(Duration.between(call.getStartTime(), ZonedDateTime.now()).toSeconds());
                }
                mycalls.add(new MyCall(call.getFrom(),
                        call.getTo(),
                        call.getForwardedFrom(),
                        duration,
                        alreadydone.get(call.getParentCallSid()),
                        call.getStartTime(),
                        call.getEndTime(),
                        call.getStatus().toString()
                ));
                //MyCall(String customerNumber, String clientNumber, String greenwayNumber, String duration, String queueTime, String starTime, String endTime)
            }
        }
        return mycalls;
    }

    public static List<MyCall> getDateCalls(ResourceSet<Call> list){
        Twilio.init(SID, auth_token);
        List<MyCall> mycalls = new ArrayList<>();
        for (Call call : list) {
            String duration = call.getDuration();
            if (call.getParentCallSid() != null && call.getTo().contains("client")) {
                if (!ourClients.containsKey(call.getTo())){
                    ourClients.put(call.getTo(), "https://upload.wikimedia.org/wikipedia/commons/0/0e/Ski_trail_rating_symbol-green_circle.svg");
                }
                if (call.getStatus().toString() == "in-progress"
                        || call.getStatus().toString() == "queued" ){
                    ourClients.replace(call.getTo(),"https://images.squarespace-cdn.com/content/v1/553f30aee4b0abb691b9c149/1557202379148-VJWBLQJC1W2NJJ58L2A1/Chatbot.gif");
                    ourClientsTalking.put(call.getTo(),call.getParentCallSid());
                }
                if (ourClientsTalking.containsKey(call.getTo()) && ourClientsTalking.get(call.getTo())==call.getParentCallSid()
                ){
                    String st = Call.fetcher(call.getParentCallSid()).fetch().getStatus().toString();
                    if (st != "in-progress") {
                        ourClients.replace(call.getTo(), "https://upload.wikimedia.org/wikipedia/commons/0/0e/Ski_trail_rating_symbol-green_circle.svg");
                        ourClientsTalking.remove(call.getTo());
                    }
                }
                if (call.getStatus().toString() == "in-progress"
                        || !alreadydone.containsKey(call.getParentCallSid()) ) {
                    ZonedDateTime start = Call.fetcher(call.getParentCallSid()).fetch().getStartTime();
                    String queue = String.valueOf(Duration.between(start,call.getStartTime()).toSeconds());
                    alreadydone.put(call.getParentCallSid(), queue);
                }
                if (!Objects.equals(call.getStatus().toString(), "completed" )
                        &&!Objects.equals(call.getStatus().toString(), "busy" )
                        &&!Objects.equals(call.getStatus().toString(), "no-answer" )
                        && Objects.equals(duration, "0")){
                    duration = String.valueOf(Duration.between(call.getStartTime(), ZonedDateTime.now()).toSeconds());
                }
                mycalls.add(new MyCall(call.getFrom(),
                        call.getTo(),
                        call.getForwardedFrom(),
                        duration,
                        alreadydone.get(call.getParentCallSid()),
                        call.getStartTime(),
                        call.getEndTime(),
                        call.getStatus().toString()
                ));
                //MyCall(String customerNumber, String clientNumber, String greenwayNumber, String duration, String queueTime, String starTime, String endTime)
            }
        }
        return mycalls;
    }

}
