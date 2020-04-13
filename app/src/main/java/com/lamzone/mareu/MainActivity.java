package com.lamzone.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lamzone.mareu.DI.DI;
import com.lamzone.mareu.view.MeetingAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, Filterable {

    private FloatingActionButton addMeeting;

    private MeetingAdapter mAdapter;
    private AddMeeting mAddMeeting;
    private List<Meeting> mMeetings;
    private List<Meeting> mMeetingsFiltered;
    private RecyclerView recyclerView;

    private int annees;
    private int mois;
    private int jour;

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
        DI.getMeetingApiService().getMeetings().clear();
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

    @Subscribe
    public void onDeleteNeighbour(DeleteMeetingEvent event) {
        DI.getMeetingApiService().deleteMeeting(event.meeting);
        onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mMeetings.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trier_date:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        annees = year;
        mois = month;
        jour = dayOfMonth;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.set(annees, mois ,jour);
        Date date = c.getTime();
        String dateToCompare = df.format(date);
        getFilter().filter(dateToCompare);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Meeting> filteredList = new ArrayList<>();
                mMeetingsFiltered = DI.getMeetingApiService().getMeetings();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(mMeetingsFiltered);
                } else {
                    String filterPattern = constraint.toString().trim();

                    for (Meeting meeting : mMeetingsFiltered) {
                        if (meeting.getDate().contains(filterPattern)) {
                            filteredList.add(meeting);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mMeetings.clear();
                mMeetings.addAll((List) results.values);
                mAdapter.notifyDataSetChanged();
            }
        };
    }
}
