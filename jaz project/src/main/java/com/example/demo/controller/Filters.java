package com.example.demo.controller;

import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.CallReader;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Filters {
    public static String SID = "AC846e08c607dd26c9c7a0ff5c0a6cba67";
    public static String auth_token = "e318557b50dbd6f2fdf1b589c245dddb";
    public CallReader reader = new CallReader();
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    public Date StartDateFrom;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    public Date EndDateTo;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    public Date EndDateFrom;
    public String ClientNumber;
    public String CustomerNumber;
    public String Status;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date StartDateTo;

    public Filters(Date startDateFrom, Date startDateTo, Date endDateTo, Date endDateFrom, String clientNumber, String customerNumber, String status) {
        Twilio.init(SID, auth_token);
        StartDateFrom = startDateFrom;
        StartDateTo = startDateTo;
        EndDateTo = endDateTo;
        EndDateFrom = endDateFrom;
        ClientNumber = clientNumber;
        CustomerNumber = customerNumber;
        Status = status;
    }

    public void setStartDateFrom(Date startDateFrom) {
        if (startDateFrom.toString()!="") {
            StartDateFrom = startDateFrom;
        }
    }

    public void setStartDateTo(Date startDateTo) {
        if (startDateTo.toString()!="") {
            StartDateTo = startDateTo;
        }
    }

    public void setEndDateTo(Date endDateTo) {
        if (endDateTo.toString()!="") {
            EndDateTo = endDateTo;
        }
    }

    public void setEndDateFrom(Date endDateFrom) {
        if (endDateFrom.toString()!="") {
            EndDateFrom = endDateFrom;
        }
    }

    public void setClientNumber(String clientNumber) {
        if (clientNumber!="") {
            ClientNumber = clientNumber;
        }
    }

    public void setCustomerNumber(String customerNumber) {
        if (customerNumber!="") {
            CustomerNumber = customerNumber;
        }
    }

    public void setStatus(String status) {
        if (status!="") {
            Status = status;
        }
    }

    public Date getStartDateFrom() {
        return StartDateFrom;
    }

    public Date getStartDateTo() {
        return StartDateTo;
    }

    public Date getEndDateTo() {
        return EndDateTo;
    }

    public Date getEndDateFrom() {
        return EndDateFrom;
    }

    public String getClientNumber() {
        return ClientNumber;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public String getStatus() {
        return Status;
    }



    public void ssetStartDateFrom() {
        if (StartDateFrom != null){
            Instant i = StartDateFrom.toInstant();
            ZonedDateTime time = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());
            reader.setStartTimeAfter(time);
        }
    }

    public void ssetStartDateTo() {
        if (StartDateTo != null){
            Instant i = StartDateTo.toInstant();
            ZonedDateTime time = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());
            reader.setStartTimeBefore(time);
        }
    }

    public void ssetEndDateTo() {
        if (EndDateTo != null){
            Instant i = EndDateTo.toInstant();
            ZonedDateTime time = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());
            reader.setEndTimeBefore(time);
        }
    }

    public void ssetEndDateFrom() {
        if (EndDateFrom != null){
            Instant i = StartDateFrom.toInstant();
            ZonedDateTime time = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());
            reader.setStartTimeAfter(time);
        }
    }

    public void ssetClientNumber() {
        if (ClientNumber != null){
            reader.setTo(ClientNumber);
        }
    }

    public void ssetCustomerNumber() {
        if (CustomerNumber != null){
            reader.setFrom(this.CustomerNumber);
        }
    }

    public void ssetStatus() {
        if (Objects.equals(this.Status, "Busy")){
            reader.setStatus(Call.Status.BUSY);
        }
        else if (Objects.equals(this.Status, "No-answer")){
            reader.setStatus(Call.Status.NO_ANSWER);
        }
        else if (Objects.equals(this.Status, "Completed")){
            reader.setStatus(Call.Status.COMPLETED);
        }
        else if (Objects.equals(this.Status, "In Progress")){
            reader.setStatus(Call.Status.IN_PROGRESS);
        }
    }

    public ResourceSet<Call> applyAll() throws InvocationTargetException, IllegalAccessException {
        Method[] methods = Filters.class.getDeclaredMethods();
        for(Method method : methods){
            if (method.getName().contains("sset")&&!method.getName().contains("lam")){
                method.invoke(this);
            }
        }
        return reader.read();
    }
}
