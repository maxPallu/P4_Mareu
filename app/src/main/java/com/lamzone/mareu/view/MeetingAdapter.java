package com.lamzone.mareu.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.DI.DI;
import com.lamzone.mareu.DeleteMeetingEvent;
import com.lamzone.mareu.Meeting;
import com.lamzone.mareu.R;
import com.lamzone.mareu.service.MeetingAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder> implements Filterable {

    private List<Meeting> mMeetings;
    private List<Meeting> mMeetingsFiltered;
    private MeetingAPI mAPI;

    public MeetingAdapter(List<Meeting> meetings) {
        super();
        this.mMeetings = meetings;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.meeting_cell, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.display(mMeetings.get(position));
        Meeting meeting = mMeetings.get(position);

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteMeetingEvent(meeting));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
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
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Meeting meeting : mMeetingsFiltered) {
                        if (meeting.getPlace().toLowerCase().contains(filterPattern)) {
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
                notifyDataSetChanged();
            }
        };
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView meetingPlace;
        private TextView meetingTopic;
        private TextView meetingParticipants;
        private TextView meetingHour;
        private TextView meetingDate;
        @BindView(R.id.item_list_delete_button)
        public ImageButton mDeleteButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            meetingPlace = itemView.findViewById(R.id.meeting_place);
            meetingTopic = itemView.findViewById(R.id.meeting_topic);
            meetingParticipants = itemView.findViewById(R.id.meeting_participants);
            meetingDate = itemView.findViewById(R.id.meeting_date);
            meetingHour = itemView.findViewById(R.id.meeting_hour);
        }

        void display(Meeting meeting) {
            meetingPlace.setText(meeting.getPlace());
            meetingTopic.setText(" - " +meeting.getTopic());
            meetingDate.setText(" - "+meeting.getJour()+"/"+meeting.getMois()+"/"+meeting.getAnnee());
            meetingHour.setText(" - "+meeting.getHour()+"H"+meeting.getMinutes());
            meetingParticipants.setText(meeting.getParticipants());

        }
    }
}
