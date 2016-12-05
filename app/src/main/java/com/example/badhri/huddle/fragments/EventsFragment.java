package com.example.badhri.huddle.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.badhri.huddle.R;
import com.example.badhri.huddle.adapters.EventsAdapter;
import com.example.badhri.huddle.models.UserNonParse;
import com.example.badhri.huddle.parseModels.Attendees;
import com.example.badhri.huddle.parseModels.Events;
import com.example.badhri.huddle.utils.DividerItemDecoration;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by badhri on 11/12/16.
 */

public class EventsFragment extends Fragment {
    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    ArrayList<Events> mEvents;
    private Unbinder unbinder;

    LinearLayoutManager linearLayoutManager;
    EventsAdapter adapter;
    private OnCompleteEventClick mListener;


    private static final String TAG = "";
    private UserNonParse user;

    public void setUser(UserNonParse user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_events, parent, false); //not attached yet
        unbinder = ButterKnife.bind(this, v);
        adapter = new EventsAdapter(getActivity(), mEvents);
        rvEvents.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvEvents.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                linearLayoutManager.getOrientation());
        rvEvents.addItemDecoration(dividerItemDecoration);
        rvEvents.setItemAnimator(new DefaultItemAnimator());

        /*rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentid, int totalItemsCount) {
                if (currentid == 0) {
                    populateTimeline(-1, 0);
                } else {
                    Tweet last = mTweets.get(mTweets.size()-1);
                    loadMore(last.getUid());
                    log.d(TAG, "max_id:" + Long.toString(last.getUid()));
                }
            }
        });*/

        setEventClickHandler();
        addEvents();
        setUpSwipe();
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);
        return v;
    }

    private void setEventClickHandler() {
        // i was unable to set a getAdapter to the DashboardActivity to apply the listener there
        adapter.setOnItemClickListener(new EventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("DEBUG", "position" + position);
                mListener.onEventPress(position, mEvents.get(position));
            }
        });
    }



    public interface OnCompleteEventClick{
        public abstract void onEventPress(int tabIndex, Events events);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCompleteEventClick) {
            this.mListener = (OnCompleteEventClick) context;
        }
    }

    private void addEvents() {
        // Construct query to execute
        ParseQuery<Attendees> query = ParseQuery.getQuery(Attendees.class);
        // Configure limit and sort order
        //query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        query.whereEqualTo("status", filter);
        if (user != null) {
            query.whereEqualTo("user", user.getParseId());
        }
        // get the latest 500 messages, order will show up newest to oldest of this group
        //query.orderByDescending("startTime");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        mEvents.clear();
        query.findInBackground(new FindCallback<Attendees>() {
            public void done(List<Attendees> attendees, ParseException e) {
                if (e == null) {
                    for (Attendees attendee : attendees) {
                        ParseQuery<Events> query = ParseQuery.getQuery(Events.class);
                        query.getInBackground(attendee.getEvent(), new GetCallback<Events>() {
                            public void done(Events object, ParseException e) {
                                if (e == null) {
                                    // object will be your game score
                                    mEvents.add(object);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    // something went wrong
                                }
                            }
                        });
                        //mEvents.clear();

                    }// update adapter
                    // Scroll to the bottom of the list on initial load
                    /*if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }*/
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });

    }


    public static final String ARG_FILTER = "ARG_FILTER";

    public static EventsFragment newInstance(String filter) {
        Bundle args = new Bundle();
        args.putString(ARG_FILTER, filter);
        EventsFragment fragment = new EventsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String filter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvents = new ArrayList<>();
        filter = getArguments().getString(ARG_FILTER);
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    private void setUpSwipe() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadRecent();
                swipeContainer.setRefreshing(false);

            }
        });
    }

    private void loadRecent() {
        adapter.clear();
        addEvents();
    }


/*

    protected void postDelayed(final long since_id, final long max_id) {
        Handler handler = new Handler();

        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                populateTimeline(since_id, max_id);
            }
        };
        handler.postDelayed(runnableCode, 60000);
    }

    protected Boolean isNetworkAvailable() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }*/
}
