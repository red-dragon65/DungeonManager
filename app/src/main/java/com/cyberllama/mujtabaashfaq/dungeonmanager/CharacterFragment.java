package com.cyberllama.mujtabaashfaq.dungeonmanager;


import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.ViewTreeObserver;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.view.MotionEvent;
import android.content.Context;
import android.graphics.Point;
import android.os.SystemClock;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Bundle;
import android.view.View;

import java.util.List;


public class CharacterFragment extends Fragment {

    //Variable to save variables.
    private SharedPreferences rowSaveData;

    //Variable for recycler view.
    private RecyclerAdapter mAdapter;

    //Interface is implemented by MainActivity.
    private charListener activityCommander;

    //Variable to hold width of reference progress bar.
    int portion = 0;

    //Variable to hold X location of reference progress bar.
    float start;

    //Variable that adjusts where button click location is.
    float screenYLoc;

    //Variable for auto click.
    private Handler handler = new Handler();


    /**
     * Implemented interface to MainActivity
     */

    public interface charListener {
        void startDialog();
    }

    /**
     * Called whenever fragment is attached
     */
    @Override
    public void onAttach(Context cont) {
        super.onAttach(cont);

        //Sets activity commander.
        try {
            activityCommander = (charListener) cont;
        } catch (ClassCastException e) {
            throw new ClassCastException(cont.toString());
        }
    }



    /**
     * Creates View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_character, container, false);

        //Gets reference progress bar.
        final ProgressBar bar = (ProgressBar) v.findViewById(R.id.refBar);

        //Gets width of reference progress bar.
        if (portion == 0) {
            //Properly gets variables of reference progress bar.
            ViewTreeObserver vto = bar.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    portion = bar.getWidth();
                    start = bar.getX();
                    //Pass width and X loc to recycler view.
                    mAdapter.updateProgBar(start, portion);

                    //Prevents shit from hitting the fan.
                    ViewTreeObserver obs = bar.getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this);
                }
            });
        }


        //Gets row data to be set to RecyclerAdapter.
        //Gets save object.
        rowSaveData = getActivity().getSharedPreferences("SavedRows", Context.MODE_PRIVATE);

        //Holds unparsed save data.
        String saveData = rowSaveData.getString("Data", "");

        //Holds row items.
        List<RowItem> holdData = new ArrayList<>();

        //Makes rowItem from parsed save data.
        if (!saveData.equals("")) {
            for (String data : saveData.split(":")) {

                RowItem item = new RowItem();

                item.toObject(data);

                holdData.add(item);
            }
        }


        //Get RecyclerView reference.
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.character_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //Stops flashing on card update.
        mRecyclerView.getItemAnimator().setChangeDuration(0);

        // Set layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(getContext(), holdData);
        mRecyclerView.setAdapter(mAdapter);

        //Custom circle button.
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.materialButton);

        //Sets circle button listener.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //Starts dialog through main activity.
                activityCommander.startDialog();

            }
        });

        //Creates handler to check for hold listener.
        handler.removeCallbacks(handlerRunnable);
        handler.postDelayed(handlerRunnable, 1000);

        return v;
    }


    /**
     * Row item sent from dialog. Set to recycler view.
     */
    public void setData(RowItem data) {
        //Adds data to recycler view.
        mAdapter.insert(data);

        Toast.makeText(getContext(), "New Character Added", Toast.LENGTH_SHORT).show();
    }


    /**
     * Save data when paused.
     */
    @Override
    public void onPause() {

        //Get save data editor.
        SharedPreferences.Editor editor = rowSaveData.edit();

        //Hold row item data from recycler.
        List<RowItem> list = mAdapter.getData();

        //Holds All rowItem as a big string.
        String saveData = "";

        //Convert row items to string.
        for (RowItem data : list) {
            saveData += data.toString() + ":";
        }

        //Save data.
        editor.putString("Data", saveData);
        editor.apply();

        //Stops the runnable.
        handler.removeCallbacks(handlerRunnable);

        super.onPause();
    }


    /**
     * Sets user values to recycler view.
     */
    @Override
    public void onResume() {
        //Stops previous handler calls.
        handler.removeCallbacks(handlerRunnable);
        //Starts handler.
        handler.postDelayed(handlerRunnable, 1000);

        //Refresh user preferences.
        mAdapter.refreshPrefs();
        //Refresh variables for reference progress bar.(In case user rotates screen)
        mAdapter.updateProgBar(start, portion);
        super.onResume();
    }


    /**
     * Method that handles auto clicking.
     */
    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            //Set click rate.
            handler.postDelayed(this, 200);

            //Get y screen adjustment value.
            Point point = getPointOfView(getView());
            screenYLoc = point.y;

            //Auto clicks.
            if (mAdapter.checker == 0 || mAdapter.checker == 12) {

                long downTime = SystemClock.uptimeMillis();
                long eventTime = SystemClock.uptimeMillis();

                MotionEvent motionEvent = MotionEvent.obtain(
                        downTime,
                        eventTime,
                        MotionEvent.ACTION_DOWN,
                        mAdapter.xLoc,//x
                        (mAdapter.yLoc - screenYLoc),//y
                        0
                );
                getView().dispatchTouchEvent(motionEvent);

                long doTime = SystemClock.uptimeMillis();
                long eveTime = SystemClock.uptimeMillis();

                MotionEvent event = MotionEvent.obtain(
                        doTime,
                        eveTime,
                        MotionEvent.ACTION_UP,
                        mAdapter.xLoc,//x
                        (mAdapter.yLoc - screenYLoc),//y
                        0
                );
                getView().dispatchTouchEvent(event);
            }
        }
    };


    /**
     * Method that returns the location of the view relative to the screen.
     * Used to adjust for notification and action bar.
     */
    private Point getPointOfView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Point(location[0], location[1]);
    }

}
