package com.lamzone.mareu;

import com.lamzone.mareu.DI.DI;
import com.lamzone.mareu.service.MeetingAPI;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Tests_Unitaires {

    public MeetingAPI mAPI;
    Meeting meeting = new Meeting("Test", " ", 2020, 03, 31, 17, 20, " ");

    @Before
    public void setup() {
        mAPI = DI.getMeetingApiService();
        mAPI.createMeeting(meeting);
    }

    @Test
    public void addMeetingWithSuccess() {
        assertTrue(mAPI.getMeetings().contains(meeting));
    }

    @Test
    public void deleteMeetingWithSuccess() {
        mAPI.deleteMeeting(meeting);
        assertFalse(mAPI.getMeetings().contains(meeting));
    }

    @Test
    public void filterByPlace() {

    }

    @Test
    public void filterByDate() {

    }
}
