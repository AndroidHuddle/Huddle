package com.example.badhri.huddle.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.models.EventActivityType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victorhom on 11/21/16.
 */
public class EventActivityTypeAdapter extends RecyclerView.Adapter<EventActivityTypeAdapter.ViewHolder> {

    private List<EventActivityType> mEventActivityTypes;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public EventActivityTypeAdapter(Context context, List<EventActivityType> eventActivityTypes) {
        mEventActivityTypes = eventActivityTypes;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public EventActivityTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View eventActivityTypeView = inflater.inflate(R.layout.item_rv_event_activity_type, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventActivityTypeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventActivityTypeAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        EventActivityType eventActivityType = mEventActivityTypes.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.tvEventActivityTypeName;
        textView.setText(eventActivityType.getEventType());
    }

    @Override
    public int getItemCount() {
        return mEventActivityTypes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_event_activity_type_name)
        TextView tvEventActivityTypeName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
