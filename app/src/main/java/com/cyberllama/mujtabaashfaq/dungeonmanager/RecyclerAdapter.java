package com.cyberllama.mujtabaashfaq.dungeonmanager;


import android.support.v7.widget.RecyclerView;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.content.Context;
import android.widget.TextView;
import android.graphics.Point;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.Collections;
import android.view.View;
import java.util.List;


/*
Use a item change method (When a data item has it's data updated).
Use a structural change method (When data is removed, repositioned, or inserted).

notifyItemChanged(int) - item change

notifyItemInserted(int) - structure change
notifyItemRemoved(int) - structure change

notifyDataSetChanged() - Last resort. Taxing. Full update.
 */


/*
    Displays DataItems in a list.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    //Hold the list of row data objects passed.
    List<RowItem> rows = Collections.emptyList();

    Context context;

    private LayoutInflater inflater;

    //Hold hitrate.
    int hitrate;
    int portion = 0;
    float start = 0;

    //Variables to hold location of button clicked.
    float xLoc, yLoc, checker = 6;

    //Flag for bit health and mana.
    boolean bitBar;

    //Holds view for each character.
    MyViewHolder holder;


    /**
     * Default constructor
     */
    public RecyclerAdapter(Context context, List<RowItem> data) {

        //Get inflater.
        inflater = LayoutInflater.from(context);

        //Set character data passed on startup.
        rows = data;

        this.context = context;
    }


    /**
     * Called every time a new row is to be displayed.
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Get row layout view.
        View view = inflater.inflate(R.layout.custom_row, parent, false);

        //Put layout in MyViewHolder object.
        holder = new MyViewHolder(view);

        return holder;
    }

    /**
     * Sets the data that should correspond to the current row.
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //Get the row to be displayed.
        RowItem current = rows.get(position);

        //Update layout widgets.
        holder.name.setText(current.name);
        holder.healthPercentage.setText(current.healthPercentage + "%");
        holder.manaPercentage.setText(current.manaPercentage + "%");
        holder.tpPercentage.setText(current.tpPercentage + "%");

        holder.healthTot.setText("Total: " + current.healthTotal);
        holder.manaTot.setText("Total: " + current.manaTotal);
        holder.tpTot.setText("Total: " + current.tpTotal);

        holder.healthRem.setText("Left: " + current.healthRem);
        holder.manaRem.setText("Left: " + current.manaRem);
        holder.tpRem.setText("Left: " + current.tpRem);

        holder.healthBar.setProgress(Integer.parseInt(current.healthPercentage));
        holder.manaBar.setProgress(Integer.parseInt(current.manaPercentage));
        holder.tpBar.setProgress(Integer.parseInt(current.tpPercentage));


        //Set the sprite spacing.

        //Toggle progress bar visibility.
        if (bitBar) {
            holder.healthBar.setVisibility(View.INVISIBLE);
            holder.manaBar.setVisibility(View.INVISIBLE);
        } else {
            holder.healthBar.setVisibility(View.VISIBLE);
            holder.manaBar.setVisibility(View.VISIBLE);
        }

        //Initial X offset of sprites.
        float spacing = start;

        //Variables for setting heart visibility.
        int hProg = Integer.parseInt(current.healthPercentage);
        int mProg = Integer.parseInt(current.manaPercentage);

        //Variable that checks percentage.
        int value = 0;

        //Holds array of heart sprite references.
        ImageView[] heartReferences = {holder.heart0, holder.heart1, holder.heart2, holder.heart3, holder.heart4, holder.heart5, holder.heart6, holder.heart7, holder.heart8, holder.heart9};

        //Holds array of potion sprite references.
        ImageView[] manaReferences = {holder.mana0, holder.mana1, holder.mana2, holder.mana3,
                holder.mana4, holder.mana5, holder.mana6, holder.mana7, holder.mana8, holder.mana9};


        //Initialize hearts.
        for (ImageView h : heartReferences) {

            //Set picture.
            h.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_icon));
            //Set initial offset.
            h.setX(spacing);
            //Set Y offset.
            h.setY(holder.healthPercentage.getY() + 55);
            //Calculate sprite spacing.
            spacing += portion / 10;

            //Set what hearts are to be visible on resume.
            if (value < hProg || value == 100) {
                h.setVisibility(View.VISIBLE);
            } else
                h.setVisibility(View.INVISIBLE);

            if (!bitBar)
                h.setVisibility(View.INVISIBLE);

            //Increase percentage value to check.
            value += 10;
        }

        //Reset the values for potion sprites.
        spacing = start;
        value = 0;

        //Initialize potions.
        for (ImageView m : manaReferences) {

            //Set picture.
            m.setImageDrawable(context.getResources().getDrawable(R.drawable.potion_icon));
            //Set initial offset.
            m.setX(spacing);
            //Set Y offset.
            m.setY(holder.manaPercentage.getY() + 55);
            //Calculate sprite spacing.
            spacing += portion / 10;

            //Set what potions are to be visible on resume.
            if (value < mProg || value == 100) {
                m.setVisibility(View.VISIBLE);
            } else
                m.setVisibility(View.INVISIBLE);

            if (!bitBar)
                m.setVisibility(View.INVISIBLE);

            //Increase percentage value to check.
            value += 10;
        }

    }


    /**
     * Return the number of rows.
     */
    @Override
    public int getItemCount() {
        return rows.size();
    }


    /**
     * Class that gets references to the row layout.
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        private TextView name;

        private TextView healthPercentage;
        private TextView manaPercentage;
        private TextView tpPercentage;

        private TextView healthTot;
        private TextView manaTot;
        private TextView tpTot;

        private TextView healthRem;
        private TextView manaRem;
        private TextView tpRem;


        private ProgressBar healthBar;
        private ProgressBar manaBar;
        private ProgressBar tpBar;


        private Button remove;
        private Button addHealth;
        private Button minHealth;
        private Button addMana;
        private Button minMana;
        private Button addTp;
        private Button minTp;


        private ImageView heart0;
        private ImageView heart1;
        private ImageView heart2;
        private ImageView heart3;
        private ImageView heart4;
        private ImageView heart5;
        private ImageView heart6;
        private ImageView heart7;
        private ImageView heart8;
        private ImageView heart9;

        private ImageView mana0;
        private ImageView mana1;
        private ImageView mana2;
        private ImageView mana3;
        private ImageView mana4;
        private ImageView mana5;
        private ImageView mana6;
        private ImageView mana7;
        private ImageView mana8;
        private ImageView mana9;


        public MyViewHolder(View itemView) {
            super(itemView);//Find views (text, image) from view passed.

            heart0 = (ImageView) itemView.findViewById(R.id.heart0);
            heart1 = (ImageView) itemView.findViewById(R.id.heart1);
            heart2 = (ImageView) itemView.findViewById(R.id.heart2);
            heart3 = (ImageView) itemView.findViewById(R.id.heart3);
            heart4 = (ImageView) itemView.findViewById(R.id.heart4);
            heart5 = (ImageView) itemView.findViewById(R.id.heart5);
            heart6 = (ImageView) itemView.findViewById(R.id.heart6);
            heart7 = (ImageView) itemView.findViewById(R.id.heart7);
            heart8 = (ImageView) itemView.findViewById(R.id.heart8);
            heart9 = (ImageView) itemView.findViewById(R.id.heart9);

            mana0 = (ImageView) itemView.findViewById(R.id.mana0);
            mana1 = (ImageView) itemView.findViewById(R.id.mana1);
            mana2 = (ImageView) itemView.findViewById(R.id.mana2);
            mana3 = (ImageView) itemView.findViewById(R.id.mana3);
            mana4 = (ImageView) itemView.findViewById(R.id.mana4);
            mana5 = (ImageView) itemView.findViewById(R.id.mana5);
            mana6 = (ImageView) itemView.findViewById(R.id.mana6);
            mana7 = (ImageView) itemView.findViewById(R.id.mana7);
            mana8 = (ImageView) itemView.findViewById(R.id.mana8);
            mana9 = (ImageView) itemView.findViewById(R.id.mana9);

            //Get text references.
            name = (TextView) itemView.findViewById(R.id.character_name);
            healthPercentage = (TextView) itemView.findViewById(R.id.healthPercent);
            manaPercentage = (TextView) itemView.findViewById(R.id.manaPercent);
            tpPercentage = (TextView) itemView.findViewById(R.id.tpPercent);

            healthTot = (TextView) itemView.findViewById(R.id.healthTot);
            manaTot = (TextView) itemView.findViewById(R.id.manaTot);
            tpTot = (TextView) itemView.findViewById(R.id.tpTot);

            healthRem = (TextView) itemView.findViewById(R.id.healthRem);
            manaRem = (TextView) itemView.findViewById(R.id.manaRem);
            tpRem = (TextView) itemView.findViewById(R.id.tpRem);

            //Get progress bar references.
            healthBar = (ProgressBar) itemView.findViewById(R.id.healthBar);
            manaBar = (ProgressBar) itemView.findViewById(R.id.manaBar);
            tpBar = (ProgressBar) itemView.findViewById(R.id.tpBar);

            //Get button references.
            remove = (Button) itemView.findViewById(R.id.removeButton);
            addHealth = (Button) itemView.findViewById(R.id.healthAddButton);
            minHealth = (Button) itemView.findViewById(R.id.healthMinButton);
            addMana = (Button) itemView.findViewById(R.id.manaAddButton);
            minMana = (Button) itemView.findViewById(R.id.manaMinButton);
            addTp = (Button) itemView.findViewById(R.id.tpAddButton);
            minTp = (Button) itemView.findViewById(R.id.tpMinButton);


            //Set click listener.
            remove.setOnTouchListener(this);
            addHealth.setOnTouchListener(this);
            minHealth.setOnTouchListener(this);
            addMana.setOnTouchListener(this);
            minMana.setOnTouchListener(this);
            addTp.setOnTouchListener(this);
            minTp.setOnTouchListener(this);
        }


        /**
         * Method to respond to click events.
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //Get location of button.
            Point point = getCenterPointOfView(v);
            xLoc = point.x;
            yLoc = point.y;

            //Deduce what button was clicked.
            switch (v.getId()) {
                case R.id.removeButton:
                    //Displays dialog box confirming character removal.
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Delete?");
                        builder.setMessage("Data cannot be recovered.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete(getAdapterPosition());
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                        return true;
                    }
                    break;


                case R.id.healthAddButton:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        //Auto click detector method.
                        setChecker(true);

                        //Update row data.
                        update(getAdapterPosition(), "health", "add");
                        return true;
                    }
                    break;
                case R.id.healthMinButton:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        //Auto click detector method.
                        setChecker(false);

                        //Update row data.
                        update(getAdapterPosition(), "health", "min");
                    }
                    break;


                case R.id.manaAddButton:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        //Auto click detector method.
                        setChecker(true);

                        //Update row data.
                        update(getAdapterPosition(), "mana", "add");
                        return true;
                    }
                    break;
                case R.id.manaMinButton:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        //Auto click detector method.
                        setChecker(false);

                        //Update row data.
                        update(getAdapterPosition(), "mana", "min");
                    }
                    break;


                case R.id.tpAddButton:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        //Auto click detector method.
                        setChecker(true);

                        //Update row data.
                        update(getAdapterPosition(), "tp", "add");
                        return true;
                    }
                    break;
                case R.id.tpMinButton:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        //Auto click detector method.
                        setChecker(false);

                        //Update row data.
                        update(getAdapterPosition(), "tp", "min");
                    }
                    break;
            }

            return false;
        }
    }

    /**
     * Method that sets value of presses (checker) needed in order to initiate auto click.
     */
    public void setChecker(boolean addition){

        /*
        ADD BUTTON
         */
        if(addition) {
            checker++;
            //Prevents checker from going below minimum value.
            if (checker > 12)
                checker = 12;

            //Reset checker if opposite button is pressed.
            //(Resets checker if value is one less than max.)
            if (checker == 1)
                checker = 6;

            //Resets checker correctly if prog value is max. (two more than min.)
            if (checker == 2)
                checker = 7;
        }


        /*
        MIN BUTTON
         */
        if(!addition) {
            checker--;
            //Prevents checker from going below minimum value.
            if (checker < 0)
                checker = 0;

            //Reset checker if opposite button is pressed.
            //(Resets checker if value is one less than max.)
            if (checker == 11)
                checker = 6;

            //Resets checker correctly if prog value is min. (two less than max.)
            if (checker == 10) {
                checker = 5;
            }
        }
    }

    /**
     * Method to get location of button to be pressed.
     */
    private Point getCenterPointOfView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0] + view.getWidth() / 2;
        int y = location[1] + view.getHeight() / 2;
        return new Point(x, y);
    }




    /**
     * Method to delete item.
     */
    public void delete(int position) {
        //Remove data item.
        rows.remove(position);

        //Notify data removal.
        notifyItemRemoved(position);
    }

    /**
     * Method to update data. (Button pressed: values changed.)
     */
    public void update(int position, String type, String incrORdecr) {

        //Variable to hold position of data to update.
        RowItem newData = rows.get(position);

        //Deduce what to update.
        if (type.equals("health")) {

            float healthRem = Float.parseFloat(newData.healthRem);
            float healthTot = Float.parseFloat(newData.healthTotal);

            //Increase health.
            if (incrORdecr.equals("add")) {

                healthRem += hitrate;

                //Keeps health within max.
                if (healthRem > healthTot) {
                    healthRem = healthTot;
                    //Stops auto clicking.
                    checker = 11;
                }

                newData.healthRem = "" + (int) healthRem;
                newData.healthPercentage = "" + ((int) (100 * (healthRem / healthTot)));
            } else {

                //Decrease health.
                healthRem -= hitrate;

                //Keeps health within min.
                if (healthRem < 0) {
                    healthRem = 0;
                    //Stops auto clicking.
                    checker = 1;
                }

                newData.healthRem = "" + (int) healthRem;
                newData.healthPercentage = "" + ((int) (100 * (healthRem / healthTot)));
            }

        } else if (type.equals("mana")) {

            float manaRem = Float.parseFloat(newData.manaRem);
            float manaTot = Float.parseFloat(newData.manaTotal);

            //Increase mana.
            if (incrORdecr.equals("add")) {

                manaRem += hitrate;

                //Keeps mana within max.
                if (manaRem > manaTot) {
                    manaRem = manaTot;
                    //Stops auto clicking.
                    checker = 11;
                }

                newData.manaRem = "" + (int) manaRem;
                newData.manaPercentage = "" + ((int) (100 * (manaRem / manaTot)));
            } else {

                //Decrease mana.
                manaRem -= hitrate;

                //Keeps mana within min.
                if (manaRem < 0) {
                    manaRem = 0;
                    //Stops auto clicking.
                    checker = 1;
                }
                newData.manaRem = "" + (int) manaRem;
                newData.manaPercentage = "" + ((int) (100 * (manaRem / manaTot)));
            }

        } else if (type.equals("tp")) {

            float tpRem = Float.parseFloat(newData.tpRem);
            float tpTot = Float.parseFloat(newData.tpTotal);

            //Increase TP.
            if (incrORdecr.equals("add")) {

                tpRem += hitrate;

                //Keeps TP within max.
                if (tpRem > tpTot) {
                    tpRem = tpTot;
                    //Stops auto clicking.
                    checker = 11;
                }

                newData.tpRem = "" + (int) tpRem;
                newData.tpPercentage = "" + ((int) (100 * (tpRem / tpTot)));
            } else {

                //Decrease TP.
                tpRem -= hitrate;

                //Keeps TP within min.
                if (tpRem < 0) {
                    tpRem = 0;
                    //Stops auto clicking.
                    checker = 1;
                }
                newData.tpRem = "" + (int) tpRem;
                newData.tpPercentage = "" + ((int) (100 * (tpRem / tpTot)));
            }
        }


        //Set old data to new data.
        rows.set(position, newData);

        //Notify data update.
        notifyItemChanged(position);
    }

    /**
     * Method to add data.
     */
    public void insert(RowItem rowData) {

        //Add data to data list.
        rows.add(rowData);

        //Notify data insert.
        notifyItemInserted(rows.size());
    }

    /**
     * Method to return data for saving purposes.
     */
    public List<RowItem> getData() {
        return rows;
    }



    /**
     * Sets user preferences.
     */
    public void refreshPrefs() {

        //Get preference object.
        final SharedPreferences userPrefs;
        userPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        //Get hitrate value.
        hitrate = Integer.parseInt(userPrefs.getString("pref_hitrate", "1"));

        //Get bar type.
        String bit = userPrefs.getString("pref_progress_bar", "Green");
        if (bit.equals("Green"))
            bitBar = false;
        else
            bitBar = true;
    }



    /**
     * Sets the progress bar values.
     */
    public void updateProgBar(float start, int portion) {
        //Bar values.
        this.start = start;
        this.portion = portion;

        //Update recycler view.
        notifyDataSetChanged();
    }
}
