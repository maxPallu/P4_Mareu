package com.lamzone.mareu;

import com.lamzone.mareu.DI.DI;
import com.lamzone.mareu.service.MeetingAPI;
import com.lamzone.mareu.service.MeetingAPIService;
import com.lamzone.mareu.view.MeetingAdapter;
import com.lamzone.mareu.MainActivity;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Tests_Unitaires {

    public MeetingAPI mAPI;
    List<Meeting> mMeetings = DI.getMeetingApiService().getMeetings();
    public MainActivity mActivity = new MainActivity();
    private MeetingAdapter mAdapter = new MeetingAdapter(mMeetings);
    Meeting meeting, meeting2;

    {
        try {
            meeting = new Meeting("Test", "Test", 2020, 03, 31, 17, 20, "Test");
            meeting2 = new Meeting("Salle 2", "Test", 2020, 04, 31, 17, 20, "Test");
        } catch (MeetingException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setup() {
        mAPI = DI.getMeetingApiService();
        mMeetings.add(meeting);
        mMeetings.add(meeting2);
    }

    @Test
    public void addMeetingWithSuccess() {
        assertTrue(mMeetings.contains(meeting));
    }

    @Test
    public void deleteMeetingWithSuccess() {
        mAPI.deleteMeeting(meeting);
        assertFalse(mAPI.getMeetings().contains(meeting));
    }

    @Test
    public void filterByPlace() {
        mAdapter.getFilter().filter("Test");
        assertFalse(mAPI.getMeetings().contains(meeting2));
    }

    @Test
    public void filterByDate() {
        mActivity.getFilter().filter("31/03/2020");
        assertFalse(mAPI.getMeetings().contains(meeting2));
    }
}
