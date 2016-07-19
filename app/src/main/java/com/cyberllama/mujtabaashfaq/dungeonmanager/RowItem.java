package com.cyberllama.mujtabaashfaq.dungeonmanager;


/*
    Describes row data to be placed in recycler view.
 */
public class RowItem {

    //Variables to hold character data.
    String name;

    String healthTotal;
    String healthRem;
    String healthPercentage;

    String manaTotal;
    String manaRem;
    String manaPercentage;

    String tpTotal;
    String tpRem;
    String tpPercentage;


    /**
     * Default constructor.
     */
    public RowItem(String name, String health, String mana, String tp) {

        this.name = name;

        healthTotal = health;
        healthRem = health;
        healthPercentage = "100";

        manaTotal = mana;
        manaRem = mana;
        manaPercentage = "100";

        tpTotal = tp;
        tpRem = tp;
        tpPercentage = "100";
    }


    /**
     * Constructor.
     * Called when recreating rows.
     */
    public RowItem() {

    }


    /**
     * Converts variables into a string.
     * Saving purposes.
     */
    public String toString() {

        String parse = name +
                "," + healthTotal + "," + healthRem + "," + healthPercentage +
                "," + manaTotal + "," + manaRem + "," + manaPercentage +
                "," + tpTotal + "," + tpRem + "," + tpPercentage;

        return parse;
    }


    /**
     * Sets variables based on passed string.
     * For restoring from. Saving purpose.
     */
    public void toObject(String parse) {

        int i = 0;

        for (String token : parse.split(",")) {
            switch (i) {
                case 0:
                    this.name = token;
                    break;
                case 1:
                    this.healthTotal = token;
                    break;
                case 2:
                    this.healthRem = token;
                    break;
                case 3:
                    this.healthPercentage = token;
                    break;
                case 4:
                    this.manaTotal = token;
                    break;
                case 5:
                    this.manaRem = token;
                    break;
                case 6:
                    this.manaPercentage = token;
                    break;
                case 7:
                    this.tpTotal = token;
                    break;
                case 8:
                    this.tpRem = token;
                    break;
                case 9:
                    this.tpPercentage = token;
                    break;
            }
            i++;
        }

    }
}
