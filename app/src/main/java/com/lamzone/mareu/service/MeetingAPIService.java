package com.lamzone.mareu.service;

import android.widget.Filter;
import android.widget.Filterable;

import com.lamzone.mareu.DI.DI;
import com.lamzone.mareu.Meeting;
import com.lamzone.mareu.view.MeetingAdapter;

import java.util.ArrayList;
import java.util.List;
public class MeetingAPIService implements MeetingAPI {

    private List<Meeting> meetings = new ArrayList<>();
    private MeetingAdapter mAdapter;

    @Override
    public List<Meeting> getMeetings() {
        return new ArrayList<>(meetings);
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    @Override
    public void createMeeting(Meeting meeting) {
        meetings.add(meeting);
    }
}
