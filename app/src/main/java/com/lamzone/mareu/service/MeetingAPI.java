package com.lamzone.mareu.service;

import com.lamzone.mareu.Meeting;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

public interface MeetingAPI {

    public List<Meeting> getMeetings();
    public void deleteMeeting(Meeting meeting);
    public void createMeeting(Meeting meeting);

}
