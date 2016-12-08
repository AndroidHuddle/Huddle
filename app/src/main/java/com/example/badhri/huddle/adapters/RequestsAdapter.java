package com.example.badhri.huddle.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.parseModels.FriendRequest;
import com.example.badhri.huddle.parseModels.Friends;
import com.example.badhri.huddle.utils.FriendRequestContainer;
import com.example.badhri.huddle.utils.ParsePushHelper;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static io.fabric.sdk.android.Fabric.TAG;

/**
 * Created by badhri on 11/28/16.
 */

public class RequestsAdapter extends
        RecyclerView.Adapter<RequestsAdapter.ViewHolder> {


    private List<FriendRequestContainer> mRequests;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public RequestsAdapter(Context context, List<FriendRequestContainer> requests) {
        mRequests = requests;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View userView = inflater.inflate(R.layout.item_rv_request, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(userView);
        return viewHolder;
    }

    private void disableBtn(RequestsAdapter.ViewHolder holder, String status) {
        holder.btnReject.setVisibility(View.GONE);
        holder.btnAccept.setVisibility(View.GONE);
        holder.tvStatus.setVisibility(View.VISIBLE);
        holder.tvStatus.setText(status);
    }

    private void addFriend(final FriendRequest request, final RequestsAdapter.ViewHolder holder) {
        Friends friends = new Friends();
        friends.setUserId(request.getFromUser());
        friends.setFriendId(request.getToUser());
        friends.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(getContext(), "You have a new Friend",
                            Toast.LENGTH_LONG).show();
                    request.setStatus("accepted");
                    request.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Updating request failed");
                            }
                        }
                    });
                    disableBtn(holder,"accepted");

                } else {
                    Toast.makeText(getContext(), "Error while accepting friend request. Try again",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    @Override
    public void onBindViewHolder(final RequestsAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        final FriendRequestContainer request = mRequests.get(position);

        holder.tvName.setText(request.user.getUsername());
        holder.tvStatus.setText(request.friendRequest.getStatus());
        if (request.incoming == false || !request.friendRequest.getStatus().contentEquals("pending")) {
            Log.i(TAG, "status" + request.friendRequest.getStatus());
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnReject.setVisibility(View.GONE);
        } else {
            holder.tvStatus.setVisibility(View.GONE);
            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFriend(request.friendRequest, holder);
                    ParsePushHelper.pushToUser(Long.toString(request.user.getPhoneNumber()), "Your are now freinds with", "Friend Request");
                }
            });
            holder.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    request.friendRequest.setStatus("rejected");
                    request.friendRequest.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Updating request failed");
                            } else {
                                disableBtn(holder,"rejected");
                            }
                        }
                    });
                }
            });
        }


    }

    public void add(FriendRequestContainer request) {
        mRequests.add(request);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRequests.size();
    }


    // view holder, will most likely need to separate if decide to create heterogenous views
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUserName)
        TextView tvName;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.btnAccept)
        Button btnAccept;
        @BindView(R.id.btnReject)
        Button btnReject;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
