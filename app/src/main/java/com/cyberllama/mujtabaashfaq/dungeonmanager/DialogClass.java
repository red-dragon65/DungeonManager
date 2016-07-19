package com.cyberllama.mujtabaashfaq.dungeonmanager;


import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.Button;
import android.view.KeyEvent;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;


//Class that shows a custom dialog box and returns the data.
public class DialogClass extends DialogFragment {

    //Variable to hold the view.
    View v;

    //Interface is implemented by MainActivity.
    DataListener activityCommander;



    /**
     * Implemented interface.
     */
    public interface DataListener {
        void getInputData(RowItem data);
    }

    /**
     * Called whenever fragment is attached.
     */
    @Override
    public void onAttach(Activity act) {
        super.onAttach(act);

        //Sets activity commander.
        try {
            activityCommander = (DataListener) act;
        } catch (ClassCastException e) {
            throw new ClassCastException(act.toString());
        }
    }



    /**
     * Called when fragment is created.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Set up dialog box with custom xml layout.
        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_layout, null);

        //Create new custom dialog box.
        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null)
                .create();


        //Set auto start keyboard.
        builder.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        //Set button listener for dialog box.
        builder.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                //Set ok button.
                Button btnOk = builder.getButton(AlertDialog.BUTTON_POSITIVE);

                //Ok button listener.
                btnOk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //Dismiss once everything is OK.
                        if (createData()) {
                            builder.dismiss();
                        }
                    }
                });
            }
        });


        //Set keyboard 'done' listener.
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                //Runs if 'done' on keyboard is pressed.
                if (keyCode == 66 && v.findViewById(R.id.tpInput).hasFocus()) {
                    //Method to pass input to dialog caller.
                    createData();
                    return true;
                }
                return false;
            }
        });


        //Return the dialog box.
        return builder;
    }


    /**
     * Method to pass input to MainActivity.
     */
    public boolean createData() {

        //Get references to the dialog box.
        final EditText inputName = (EditText) v.findViewById(R.id.nameInput);
        final EditText inputHealth = (EditText) v.findViewById(R.id.healthInput);
        final EditText inputMana = (EditText) v.findViewById(R.id.manaInput);
        final EditText inputTp = (EditText) v.findViewById(R.id.tpInput);


        //Get the widget data and pass it to the main activity.
        //Input validation.
        if (!inputName.getText().toString().equals("") && !inputHealth.getText().toString().equals("")
                && !inputMana.getText().toString().equals("") && !inputTp.getText().toString().equals("")) {

            if (inputName.getText().toString().length() <= 25) {

                if (inputHealth.getText().toString().length() <= 9) {

                    if (inputMana.getText().toString().length() <= 9) {

                        if (inputTp.getText().toString().length() <= 9) {

                            String name = inputName.getText().toString(),
                                    health = inputHealth.getText().toString(),
                                    mana = inputMana.getText().toString(),
                                    tp = inputTp.getText().toString();

                            RowItem data = new RowItem(name, health, mana, tp);

                            activityCommander.getInputData(data);
                            dismiss();
                            return true;

                        } else
                            Toast.makeText(getContext(), "Tp Value Too Long!", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getContext(), "Mana Value Too Long!", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getContext(), "Health Value Too Long!", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getContext(), "Character Name Too Long!", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(getContext(), "All Fields Must Have Data!", Toast.LENGTH_LONG).show();


        return false;
    }
}
