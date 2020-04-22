package com.lamzone.mareu;

import android.app.Activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Meeting extends Activity {

    private String place;

    private String topic;

    private int hour;

    private int minutes;

    private String participants;

    private int annee;

    private int mois;

    private int jour;

    private String date;

    public Meeting(String place, String topic, int annee, int mois, int jour, int hour, int minutes, String participants) throws MeetingException {

        if(place.equals("") || topic.equals("") || participants.equals("")) {
            throw new MeetingException();
        }
        else {
            this.place = place;
            this.topic = topic;
            this.annee = annee;
            this.mois = mois;
            this.jour = jour;
            this.hour = hour;
            this.minutes = minutes;
            this.participants = participants;
        }
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
        date = jour+"/"+"0"+mois+"/"+annee;
        return date;
    }
}
