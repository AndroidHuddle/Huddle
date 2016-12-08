package com.example.badhri.huddle.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.badhri.huddle.HuddleApplication;
import com.example.badhri.huddle.R;
import com.example.badhri.huddle.parseModels.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link UserHeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHeaderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_OBJECTID = "userObjectId";

    // TODO: Rename and change types of parameters
    private String userObjectId;

    private Unbinder unbinder;

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @BindView(R.id.rlUserHeader)
    RelativeLayout rlUserHeader;

    //private OnFragmentInteractionListener mListener;

    public UserHeaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userObjectId objectID of the user
     * @return A new instance of fragment UserHeaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHeaderFragment newInstance(String userObjectId) {
        UserHeaderFragment fragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_OBJECTID, userObjectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userObjectId = getArguments().getString(ARG_USER_OBJECTID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_header, container, false);
        unbinder = ButterKnife.bind(this, v);

        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo("objectId", userObjectId);
        query.findInBackground(new FindCallback<User>() {
            public void done(List<User> user, ParseException e) {
                if (e == null) {
                    tvUserName.setText(user.get(0).getUsername());
                    tvStatus.setText(user.get(0).getStatus());
                    ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
                    // generate random color
                    // generate color based on a key (same key returns the same color), useful for list/grid views
                    int color = generator.getColor(user.get(0).getUsername());
                    int colorB = generator.getColor(user.get(0).getUsername().substring(1,2));
                    // declare the builder object once.
                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(Color.WHITE)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(100) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()
                            .buildRoundRect(user.get(0).getUsername().substring(0,1).toUpperCase(), colorB, 160);
                    ivProfileImage.setImageDrawable(drawable);
                    rlUserHeader.setBackgroundColor(color);
                } else {
                    Log.d(HuddleApplication.TAG, "Fetching user object failed");
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
