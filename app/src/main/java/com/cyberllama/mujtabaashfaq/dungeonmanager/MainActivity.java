package com.cyberllama.mujtabaashfaq.dungeonmanager;


import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.GravityCompat;
import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.widget.ListAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Intent;
import android.graphics.Color;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


/**
 * Class that holds content(swappable frags) and navigation drawer.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CharacterFragment.charListener, DialogClass.DataListener {

    //Save which fragment was opened.
    private SharedPreferences savedVals;

    //Holds frag id.
    int fragId;

    //Holds character view fragment.
    CharacterFragment charFrag = new CharacterFragment();

    //Holds nav drawer.
    DrawerLayout drawer;



    /**
     * Gen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set layout.
        setContentView(R.layout.main_activity);

        //Set custom app bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Create nav as drawer.
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Create nav list view.
        String[] data = {"Party", "Dice Generator"};
        ListAdapter navAdapter = new NavAdapter(this, android.R.layout.simple_list_item_1, data);
        final ListView characterListView = (ListView) findViewById(R.id.left_drawer);
        characterListView.setAdapter(navAdapter);

        //Nav list view listener.
        characterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Set fragment.
                customFrag((int) id, drawer);


                //Revert last selected item.
                characterListView.getChildAt(fragId).setBackgroundColor(Color.TRANSPARENT);


                //Update selected list view.
                view.setBackgroundColor(Color.parseColor("#ffbdbdbd"));

                fragId = (int) id;
            }
        });


        //Get/set saved frag.
        savedVals = getSharedPreferences("SavedVals", 0);
        fragId = savedVals.getInt("fragId", 0);
        //Set fragment.
        customFrag(fragId, drawer);
    }


    /**
     * Gen
     */
    protected void onPause() {

        //Save last frag opened.
        SharedPreferences.Editor editor = savedVals.edit();
        editor.putInt("fragId", fragId);
        editor.apply();

        super.onPause();
    }

    /**
     * Gen
     */
    protected void onResume() {
        super.onResume();
    }

    /**
     * Gen
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Gen
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Get item selected.
        int id = item.getItemId();

        //Start activity based on id selected.
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Handle nav close on back press.
     */
    @Override
    public void onBackPressed() {
        //Close navigation drawer onBackPressed.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Handle nav selection (NOT USED)
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }


    /**
     * Nav selection.
     * Set new fragment for content.
     * Handle nav auto-close.
     */
    public void customFrag(int frag, DrawerLayout drawer) {

        switch (frag) {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.content, charFrag).commit();
                getSupportActionBar().setTitle("Party");
                drawer.closeDrawer(GravityCompat.START);
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new DiceFragment()).commit();
                getSupportActionBar().setTitle("Dice Generator");
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }


    /*
    Executed from CharacterFragment.
     */
    @Override
    public void startDialog() {
        //Start DialogClass.
        final DialogClass inputDialog = new DialogClass();
        inputDialog.show(getSupportFragmentManager(), "inputDialog");
    }

    /*
    Executed from DialogClass.
     */
    @Override
    public void getInputData(RowItem data) {
        //Send data to CharacterFragment.
        charFrag.setData(data);
    }
}
