package com.example.badhri.huddle.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.badhri.huddle.R;
import com.example.badhri.huddle.parseModels.Events;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by badhri on 11/12/16.
 */

public class EventsAdapter extends
        RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private static final String DATE_FORMAT = "d MMMM yyyy";
    private static final String TIME_FORMAT = "hh:mm a, z";

    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        @BindView(R.id.locationImage)
        public ImageView locationImage;

        @BindView(R.id.tvTitle)
        public TextView tvTiltle;

        @BindView(R.id.tvStartTime)
        public TextView tvStartTime;

//        @BindView(R.id.tvEndTime)
//        public TextView tvEndTime;

        @BindView(R.id.tvVenue)
        public TextView tvVenue;

        @BindView(R.id.frameAttenders)
        public FrameLayout frameAttenders;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // Store a member variable for the contacts
    private List<Events> mEvents;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public EventsAdapter(Context context, List<Events> events) {
        mEvents = events;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.events_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    //FriendListFragment friendListFragment = FriendListFragment.newInstance(userObjectId);
    //frameAttenders

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(EventsAdapter.ViewHolder viewHolder, int position) {
        Date date;
        String dateStr;
        String sDate = "";

        // Get the data model based on position
        Events event = mEvents.get(position);
        if (event.getImageUrl() != null && event.getImageUrl().length() > 0) {
            Glide.with(mContext)
                    .load(event.getImageUrl())
                    .fitCenter()
                    .placeholder(R.drawable.placeholder)
                    .into(viewHolder.locationImage);
        } else {
            // ContextCompat.getColor(mContext, R.color.colorPrimaryDark)
            viewHolder.locationImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.placeholder));
        }


        // Set item views based on your views and data model
        TextView tvTitle = viewHolder.tvTiltle;
        tvTitle.setText(event.getEventName());
        TextView tvStartTime  = viewHolder.tvStartTime;

        if (event.getEventStartTime() != null && event.getStartDate() != null) {
            sDate = event.getStartDate() + " |  " + event.getEventStartTime();
        }
        if (event.getStartTime() != null) {
            date = event.getStartTime();
        } else {
            date = new Date();
        }
        dateStr = com.example.badhri.huddle.utils.Utilities.formatDate(date, DATE_FORMAT) + " at " + com.example.badhri.huddle.utils.Utilities.formatDate(date, TIME_FORMAT);

        if (sDate.length() > 0) {
            tvStartTime.setText(sDate);
        } else{
            tvStartTime.setText(dateStr);
        }



//        TextView tvEndTime  = viewHolder.tvEndTime;

        if (event.getEndTime() != null) {
            date =  event.getEndTime();
        } else {
            date = new Date();
        }
        dateStr = com.example.badhri.huddle.utils.Utilities.formatDate(date, DATE_FORMAT) + " at " + com.example.badhri.huddle.utils.Utilities.formatDate(date, TIME_FORMAT);
//        tvEndTime.setText(dateStr);

        TextView tvVenue  = viewHolder.tvVenue;
        tvVenue.setText(event.getVenue());

        View parent = (View)viewHolder.tvVenue.getParent();
        parent.setTag(position);


        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (listener != null) {
                    int position = (int)v.getTag();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(v, position);
                    }
                }
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public void clear() {
        mEvents.clear();
    }
}

