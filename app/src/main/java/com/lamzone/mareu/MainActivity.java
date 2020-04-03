package com.lamzone.mareu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lamzone.mareu.DI.DI;
import com.lamzone.mareu.service.MeetingAPI;
import com.lamzone.mareu.view.MeetingAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.view.MotionEvent.AXIS_X;
import static android.view.MotionEvent.AXIS_Y;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addMeeting;

    private MeetingAdapter mAdapter;
    private List<Meeting> mMeetings;
    private RecyclerView recyclerView;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMeetings.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMeetings.clear();
        mMeetings.addAll(DI.getMeetingApiService().getMeetings());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMeetings = DI.getMeetingApiService().getMeetings();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpRecyclerView();

        addMeeting = findViewById(R.id.add_meeting_button);
        addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addMeetingActivity = new Intent(MainActivity.this, AddMeeting.class);
                startActivity(addMeetingActivity);
            }
        });
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.Recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new MeetingAdapter(mMeetings);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.trier_lieu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                invalidateOptionsMenu();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trier_jour:
                sortDay();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.trier_mois:
                sortMonth();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.trier_annee:
                sortYear();
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sortDay() {
        Collections.sort(mMeetings, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return Integer.valueOf(o1.getJour()).compareTo(Integer.valueOf(o2.getJour()));
            }
        });
    }

    private void sortMonth() {
        Collections.sort(mMeetings, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return Integer.valueOf(o1.getMois()).compareTo(Integer.valueOf(o2.getMois()));
            }
        });
    }

    private void sortYear() {
        Collections.sort(mMeetings, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return Integer.valueOf(o1.getAnnee()).compareTo(Integer.valueOf(o2.getAnnee()));
            }
        });
    }

    @Subscribe
    public void onDeleteNeighbour(DeleteMeetingEvent event) {
        DI.getMeetingApiService().deleteMeeting(event.meeting);
        onResume();
    }
}
