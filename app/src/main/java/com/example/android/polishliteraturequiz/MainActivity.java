package com.example.android.polishliteraturequiz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    EditText nameField;
    RadioGroup centuriesGroup;
    RadioButton tenthCentury;
    RadioButton thirteenthCentury;
    RadioButton fifteenthCentury;
    EditText nobelPrizes;
    CheckBox lem;
    CheckBox reymont;
    CheckBox milosz;
    CheckBox bauman;
    CheckBox szymborska;
    CheckBox herbert;
    CheckBox sienkiewicz;
    CheckBox walesa;
    RadioGroup fantasyGroup;
    RadioButton gollum;
    RadioButton kingArthur;
    RadioButton witcher;
    RadioButton reaperMan;
    RadioGroup poetsGroup;
    RadioButton lesmian;
    RadioButton gombrowicz;
    RadioButton tuwim;
    RadioButton brzechwa;
    EditText mickiewicz;
    RadioGroup comicGroup;
    RadioButton wolverine;
    RadioButton thorgal;
    RadioButton spiderman;
    RadioButton superman;
    boolean submitPressed;
    boolean resetClicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameField = findViewById(R.id.name_view);
        centuriesGroup = findViewById(R.id.century_group);
        tenthCentury = findViewById(R.id.tenth_century);
        thirteenthCentury = findViewById(R.id.thirteenth_century);
        fifteenthCentury = findViewById(R.id.fifteenth_century);
        nobelPrizes = findViewById(R.id.number_of_prizes);
        lem = findViewById(R.id.lem_checkbox);
        reymont = findViewById(R.id.reymont_checkbox);
        milosz = findViewById(R.id.milosz_checkbox);
        bauman = findViewById(R.id.bauman_checkbox);
        szymborska = findViewById(R.id.szymborska_checkbox);
        herbert = findViewById(R.id.herbert_checkbox);
        sienkiewicz = findViewById(R.id.sienkiewicz_checkbox);
        walesa = findViewById(R.id.walesa_checkbox);
        gollum = findViewById(R.id.gollum_radiobutton);
        kingArthur = findViewById(R.id.king_arthur_radiobutton);
        witcher = findViewById(R.id.witcher_radiobutton);
        reaperMan = findViewById(R.id.reaper_man_radiobutton);
        lesmian = findViewById(R.id.lesmian_radiobutton);
        gombrowicz = findViewById(R.id.gombrowicz_radiobutton);
        tuwim = findViewById(R.id.tuwim_radiobutton);
        brzechwa = findViewById(R.id.brzechwa_radiobutton);
        mickiewicz = findViewById(R.id.adam_mickiewicz_edittext);
        wolverine = findViewById(R.id.wolverine_radiobutton);
        thorgal = findViewById(R.id.thorgal_radiobutton);
        spiderman = findViewById(R.id.spiderman_radiobutton);
        superman = findViewById(R.id.superman_radiobutton);
        resetClicks = false;

        // prevent from opening keyboard on creation
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // create list of all buttons
        ArrayList<CompoundButton> allButtons = new ArrayList<>();
        allButtons.add(tenthCentury);
        allButtons.add(thirteenthCentury);
        allButtons.add(fifteenthCentury);
        allButtons.add(lem);
        allButtons.add(reymont);
        allButtons.add(milosz);
        allButtons.add(bauman);
        allButtons.add(szymborska);
        allButtons.add(herbert);
        allButtons.add(sienkiewicz);
        allButtons.add(walesa);
        allButtons.add(gollum);
        allButtons.add(kingArthur);
        allButtons.add(witcher);
        allButtons.add(reaperMan);
        allButtons.add(lesmian);
        allButtons.add(gombrowicz);
        allButtons.add(tuwim);
        allButtons.add(brzechwa);
        allButtons.add(wolverine);
        allButtons.add(thorgal);
        allButtons.add(spiderman);
        allButtons.add(superman);

        // allows RadioButtons, CheckBoxes stay focusableInTouchMode without need to click twice to check them
        for (int i = 0; i < 23; i++) {
            allButtons.get(i).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        v.performClick();
                    }
                }
            });
        }
    }

    /**
     * This method set focus on the last answer a user gave.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        View focusedChild = getCurrentFocus();

        if (focusedChild != null) {
            int focusID = focusedChild.getId();
            int cursorLoc = 0;

            if (focusedChild instanceof EditText) {
                cursorLoc = ((EditText) focusedChild).getSelectionStart();
            }

            outState.putInt("focusID", focusID);
            outState.putInt("cursorLoc", cursorLoc);
        }
    }

    /**
     * This method finds the last given answer before rotation and sets the screen
     * on this view after a phone rotated (not to last EditText as without it).
     */
    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);

        int focusID = inState.getInt("focusID", View.NO_ID);

        View focusedChild = findViewById(focusID);
        if (focusedChild != null) {
            focusedChild.requestFocus();

            if (focusedChild instanceof EditText) {
                int cursorLoc = inState.getInt("cursorLoc", 0);
                ((EditText) focusedChild).setSelection(cursorLoc);
            }
        }
    }


    /**
     * This method shows an appropriate text in a toast.
     */
    public void createScoreMessage(View view) {
        int score = calculateScore();
        String name = nameField.getText().toString();
        if (name.equals("")) {
            name = getString(R.string.whatever_name);
        }
        if (score == 10) {
            displayToastMessage(getString(R.string.score_10, name, score));
        } else if (score >= 8) {
            displayToastMessage(getString(R.string.score_9_8, name, score));
        } else if (score >= 6) {
            displayToastMessage(getString(R.string.score_7_6, score, name));
        } else if (score >= 4) {
            displayToastMessage(getString(R.string.score_5_4, score, name));
        } else if (score >= 1) {
            displayToastMessage(getString(R.string.score_3_2_1, score, name));
        } else if (score == 0) {
            displayToastMessage(getString(R.string.score_0, score, name));
        }
        submitPressed = true;
        Button sendEmail = findViewById(R.id.send_email_button);
        sendEmail.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        sendEmail.setTextColor(getResources().getColor(R.color.colorBackground));
    }

    /**
     * Check the answers and calculate the score. It's called in createScoreMessage method.
     */
    private int calculateScore() {
        // Check the answer to the question 1.
        int points = 0;
        thirteenthCentury = findViewById(R.id.thirteenth_century);
        if (thirteenthCentury.isChecked()) {
            points += 1;
        }

        // Check the answer to the question 2.
        nobelPrizes = findViewById(R.id.number_of_prizes);
        String numberOfNobels = nobelPrizes.getText().toString();
        if (numberOfNobels.equals("4")) {
            points += 1;
        }

        // Check the correct answers to the question 3.
        reymont = findViewById(R.id.reymont_checkbox);
        milosz = findViewById(R.id.milosz_checkbox);
        szymborska = findViewById(R.id.szymborska_checkbox);
        sienkiewicz = findViewById(R.id.sienkiewicz_checkbox);

        if (reymont.isChecked()) {
            points += 1;
        }
        if (milosz.isChecked()) {
            points += 1;
        }
        if (szymborska.isChecked()) {
            points += 1;
        }
        if (sienkiewicz.isChecked()) {
            points += 1;
        }

        // Check the answer to the question 4.
        witcher = findViewById(R.id.witcher_radiobutton);
        if (witcher.isChecked()) {
            points += 1;
        }
        // Check the answer to the question 5.
        brzechwa = findViewById(R.id.brzechwa_radiobutton);
        if (brzechwa.isChecked()) {
            points += 1;
        }
        // Check the answer to the question 6.
        mickiewicz = findViewById(R.id.adam_mickiewicz_edittext);
        String adamMickiewicz = mickiewicz.getText().toString();
        if (adamMickiewicz.equalsIgnoreCase("adam mickiewicz") || adamMickiewicz.equalsIgnoreCase("adam mickiewicz ")) {
            points += 1;
        }

        // Check the answer to the question 7.
        thorgal = findViewById(R.id.thorgal_radiobutton);
        if (thorgal.isChecked()) {
            points += 1;
        }
        return points;
    }

    /**
     * This method creates a toast message with score.
     */
    private void displayToastMessage(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    /**
     * This happens after clicking Share button:
     * 1. If the submit button hadn't been clicked, it makes a warning toast.
     * 2. If it had been clicked, it opens e-mail app with message.
     */
    public void sendScore(View view) {
        if (!submitPressed) {
            displayMessageTooSoon(getString(R.string.sending_too_soon));
            return;
        }
        String mailText = createMailText();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, mailText);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Displays message in a toast after clicking "send" button while "submit" hadn't been yet clicked.
     */
    private void displayMessageTooSoon(String shareTooSoon) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, shareTooSoon, duration);
        toast.show();
    }

    /**
     * This method and creates the e-mail text.
     */
    private String createMailText() {
        int score = calculateScore();
        String name = nameField.getText().toString();
        if (name.equals("")) {
            name = getString(R.string.whatever_name);
        }
        String mailText = getString(R.string.email_hi);
        if (score == 10) {
            mailText += getString(R.string.score_10, name, score);
        } else if (score >= 8) {
            mailText += getString(R.string.score_9_8, name, score);
        } else if (score >= 6) {
            mailText += getString(R.string.score_7_6, score, name);
        } else if (score >= 4) {
            mailText += getString(R.string.score_5_4, score, name);
        } else if (score >= 1) {
            mailText += getString(R.string.score_3_2_1, score, name);
        } else if (score == 0) {
            mailText += getString(R.string.score_0, score, name);
        }
        mailText += getString(R.string.correct_answers);
        return mailText;
    }

    /**
     * What happens when "reset" button is clicked for the first and second time.
     */

    public void resetQuiz(View v) {
        Button resetButton = findViewById(R.id.reset_button);
        if (!resetClicks) {
            displayResetWarning(getString(R.string.reset_warning));
            resetButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            resetButton.setTextColor(getResources().getColor(R.color.colorBackground));
            resetClicks = true;
        } else {
            nameField.getText().clear();
            nobelPrizes.getText().clear();
            mickiewicz.getText().clear();
            centuriesGroup = findViewById(R.id.century_group);
            centuriesGroup.clearCheck();
            fantasyGroup = findViewById(R.id.fantasy_group);
            fantasyGroup.clearCheck();
            poetsGroup = findViewById(R.id.poets_group);
            poetsGroup.clearCheck();
            comicGroup = findViewById(R.id.comic_group);
            comicGroup.clearCheck();
            lem = findViewById(R.id.lem_checkbox);
            reymont = findViewById(R.id.reymont_checkbox);
            milosz = findViewById(R.id.milosz_checkbox);
            bauman = findViewById(R.id.bauman_checkbox);
            szymborska = findViewById(R.id.szymborska_checkbox);
            herbert = findViewById(R.id.herbert_checkbox);
            sienkiewicz = findViewById(R.id.sienkiewicz_checkbox);
            walesa = findViewById(R.id.walesa_checkbox);
            lem.setChecked(false);
            reymont.setChecked(false);
            milosz.setChecked(false);
            bauman.setChecked(false);
            szymborska.setChecked(false);
            herbert.setChecked(false);
            sienkiewicz.setChecked(false);
            walesa.setChecked(false);

            Button sendEmailButton = findViewById(R.id.send_email_button);
            sendEmailButton.setBackgroundColor(getResources().getColor(R.color.colorButtonLight));
            sendEmailButton.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            resetButton.setBackgroundColor(getResources().getColor(R.color.colorButtonLight));
            resetButton.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            resetClicks = false;
            submitPressed = false;
        }
    }

    /**
     * It displays warning after the first click "Reset" button.
     */
    private void displayResetWarning(String warningReset) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, warningReset, duration);
        toast.show();
    }
}

