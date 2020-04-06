package com.lamzone.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.lamzone.mareu.DI.DI;
import com.lamzone.mareu.service.MeetingAPI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMeeting extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.meetingPlace)
    TextInputEditText placeInput;
    @BindView(R.id.meetingTopic)
    TextInputEditText topicInput;
    @BindView(R.id.meetingParticipants)
    TextInputEditText participantsInput;

    private Meeting mMeeting;
    private MeetingAPI mMeetingAPI;

    private int hour;
    private int minutes;
    private int annees;
    private int mois;
    private int jour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        ButterKnife.bind(this);
        mMeetingAPI = DI.getMeetingApiService();

    }

    public void showTimePickerDialog(View v) {
        DialogFragment timepicker = new TimePickerFragment();
        timepicker.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hour = hourOfDay;
        minutes = minute;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        annees = year;
        mois = month+1;
        jour = dayOfMonth;
    }

    @OnClick(R.id.valider)
    public void addMeeting() {

        mMeeting = new Meeting(placeInput.getText().toString(), topicInput.getText().toString(), annees, mois, jour, hour, minutes, participantsInput.getText().toString());
        mMeetingAPI.createMeeting(mMeeting);
        finish();
    }
}
