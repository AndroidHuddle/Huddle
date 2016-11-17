package com.example.badhri.huddle.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.parseModels.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victorhom on 11/13/16.
 */
public class UsersAdapter extends
        RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    // Store a member variable for the contacts
    // User is the yelp entities right now; should actually use our own User model
    private List<User> mUsers;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public UsersAdapter(Context context, List<User> users) {
        mUsers = users;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View userView = inflater.inflate(R.layout.item_rv_user, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(userView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        User user = mUsers.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.tvName;
        // get the user name

        // hardcoded at the moment - instead, get the name from the user object
        textView.setText("User name");
    }

    public void add(User u) {
        mUsers.add(u);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    // view holder, will most likely need to separate if decide to create heterogenous views
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_name)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvName.setText("Longname Longerlastname");
        }
    }
}
