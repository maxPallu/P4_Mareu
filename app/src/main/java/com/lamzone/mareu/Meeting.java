package com.lamzone.mareu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Meeting {

    private String place;

    private String topic;

    private int hour;

    private int minutes;

    private String participants;

    private int annee;

    private int mois;

    private int jour;

    public Meeting(String place, String topic, int annee, int mois, int jour, int hour, int minutes, String participants) {

        this.place = place;
        this.topic = topic;
        this.annee = annee;
        this.mois = mois;
        this.jour = jour;
        this.hour = hour;
        this.minutes = minutes;
        this.participants = participants;
    }

    public int getAnnee() {
        return annee;
    }

    public String getPlace() {
        return place;
    }

    public String getTopic() {
        return topic;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getParticipants() {
        return participants;
    }

    public int getMois() {
        return mois;
    }

    public int getJour() {
        return jour;
    }

    public String getDate() {
        Date meeting_date = new Date(annee, mois, jour);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String meetingDate = df.format(meeting_date);
        return meetingDate;
    }
}