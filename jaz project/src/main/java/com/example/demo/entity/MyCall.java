package com.example.demo.entity;

import java.time.ZonedDateTime;
import java.util.Objects;

public class MyCall {
    public String CustomerNumber;
    public String ClientNumber;
    public String GreenwayNumber;
    public String Duration;
    public String QueueTime;
    public String StarTime;
    public String EndTime;
    public String Status;
    public String Source;

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public String getClientNumber() {
        return ClientNumber;
    }

    public String getGreenwayNumber() {
        return GreenwayNumber;
    }

    public String getDuration() {
        return Duration;
    }

    public String getQueueTime() {
        return QueueTime;
    }

    public String getStarTime() {
        return StarTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public String getStatus() {
        return Status;
    }

    public String getSource() {
        return Source;
    }

    public MyCall(String customerNumber, String clientNumber, String greenwayNumber, String duration, String queueTime, ZonedDateTime starTime, ZonedDateTime endTime, String status) {
        CustomerNumber = customerNumber;
        ClientNumber = clientNumber;
        GreenwayNumber = greenwayNumber;
        Duration = duration;
        QueueTime = queueTime;
        StarTime = starTime.toString().replace("T"," ").replace("Z","");
        if (endTime!=null) {
            EndTime = endTime.toString().replace("T", " ").replace("Z", "");
        }
        else {
            EndTime = "in progress...";
        }
        Status = status;
        Source = "https://upload.wikimedia.org/wikipedia/commons/0/0e/Ski_trail_rating_symbol-green_circle.svg";
        if (Objects.equals(status, "no-answer") || Objects.equals(status, "busy")){
            Source = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/LACMTA_Circle_Red_Line.svg/768px-LACMTA_Circle_Red_Line.svg.png";
        }
        if (Objects.equals(status, "in-progress")){
            Source = "https://images.squarespace-cdn.com/content/v1/553f30aee4b0abb691b9c149/1557202379148-VJWBLQJC1W2NJJ58L2A1/Chatbot.gif";
        }
    }
}
