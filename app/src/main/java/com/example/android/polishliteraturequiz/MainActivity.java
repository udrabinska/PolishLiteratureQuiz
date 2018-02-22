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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    EditText nameField;
    RadioGroup centuriesGroup;
    RadioButton tenthCentury;
    RadioButton thirteenthCentury;
    RadioButton fifteenthCentury;
    EditText nobelPrizes;
    TextView correctAnswer2;
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
    TextView correctAnswer6;
    RadioGroup comicGroup;
    RadioButton wolverine;
    RadioButton thorgal;
    RadioButton spiderman;
    RadioButton superman;
    Button resetButton;
    Button sendEmail;
    boolean submitPressed;
    boolean resetClicked;
    List<CompoundButton> allButtons = new ArrayList<>();
    // two variables to save and restore what a user wrote in Q2&6 - to show (or not)
    // the correct answers underneath after rotation (without it was showing them always)
    private static String ANSWER_2 = "answer2";
    private static String ANSWER_6 = "answer6";

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
        correctAnswer2 = findViewById(R.id.correct_2);
        lem = findViewById(R.id.lem_checkbox);
        reymont = findViewById(R.id.reymont_checkbox);
        milosz = findViewById(R.id.milosz_checkbox);
        bauman = findViewById(R.id.bauman_checkbox);
        szymborska = findViewById(R.id.szymborska_checkbox);
        herbert = findViewById(R.id.herbert_checkbox);
        sienkiewicz = findViewById(R.id.sienkiewicz_checkbox);
        walesa = findViewById(R.id.walesa_checkbox);
        fantasyGroup = findViewById(R.id.fantasy_group);
        gollum = findViewById(R.id.gollum_radiobutton);
        kingArthur = findViewById(R.id.king_arthur_radiobutton);
        witcher = findViewById(R.id.witcher_radiobutton);
        reaperMan = findViewById(R.id.reaper_man_radiobutton);
        poetsGroup = findViewById(R.id.poets_group);
        lesmian = findViewById(R.id.lesmian_radiobutton);
        gombrowicz = findViewById(R.id.gombrowicz_radiobutton);
        tuwim = findViewById(R.id.tuwim_radiobutton);
        brzechwa = findViewById(R.id.brzechwa_radiobutton);
        mickiewicz = findViewById(R.id.adam_mickiewicz_edittext);
        correctAnswer6 = findViewById(R.id.correct_6);
        comicGroup = findViewById(R.id.comic_group);
        wolverine = findViewById(R.id.wolverine_radiobutton);
        thorgal = findViewById(R.id.thorgal_radiobutton);
        spiderman = findViewById(R.id.spiderman_radiobutton);
        superman = findViewById(R.id.superman_radiobutton);
        resetButton = findViewById(R.id.reset_button);
        sendEmail = findViewById(R.id.send_email_button);
        resetClicked = false;

        // prevent from opening keyboard on creation
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // add elements to all CompoundButtons list, first correct ones (0-7), then wrong (8-22)
        allButtons.add(thirteenthCentury);
        allButtons.add(reymont);
        allButtons.add(milosz);
        allButtons.add(szymborska);
        allButtons.add(sienkiewicz);
        allButtons.add(witcher);
        allButtons.add(brzechwa);
        allButtons.add(thorgal);
        allButtons.add(tenthCentury);
        allButtons.add(fifteenthCentury);
        allButtons.add(lem);
        allButtons.add(bauman);
        allButtons.add(herbert);
        allButtons.add(walesa);
        allButtons.add(gollum);
        allButtons.add(kingArthur);
        allButtons.add(reaperMan);
        allButtons.add(lesmian);
        allButtons.add(gombrowicz);
        allButtons.add(tuwim);
        allButtons.add(wolverine);
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

        // restore - were "Submit" or "Reset" pressed? if yes, do what needed after rotation
        if (savedInstanceState != null) {
            savedInstanceState.getString("answer2");
            nobelPrizes.setText(ANSWER_2);
            savedInstanceState.getString("answer6");
            mickiewicz.setText(ANSWER_6);
            submitPressed = savedInstanceState.getBoolean("isSubmitted");
            if (submitPressed) {
                sendEmail.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                sendEmail.setTextColor(getResources().getColor(R.color.colorBackground));
                giveColorToAnswers();
            }
            resetClicked = savedInstanceState.getBoolean("resetClicked");
            if (resetClicked) {
                resetButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                resetButton.setTextColor(getResources().getColor(R.color.colorBackground));
            }
        }
    }

    /**
     * This method sets focus on the last answer a user gave
     * and saves the state - remembers if "reset" or "submit" were clicked
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        View focusedChild = getCurrentFocus();

        // It saves the focus on the last clicked view before rotation.
        // written with help: http://onegullibull.com/WP-OneGulliBull/?p=252
        if (focusedChild != null) {
            int focusID = focusedChild.getId();
            int cursorLoc = 0;

            if (focusedChild instanceof EditText) {
                cursorLoc = ((EditText) focusedChild).getSelectionStart();
            }

            ANSWER_2 = nobelPrizes.getText().toString();
            ANSWER_6 = mickiewicz.getText().toString();

            outState.putInt("focusID", focusID);
            outState.putInt("cursorLoc", cursorLoc);
            outState.putBoolean("isSubmitted", submitPressed);
            outState.putBoolean("resetClicked", resetClicked);
            outState.putString("answer2", ANSWER_2);
            outState.putString("answer6", ANSWER_6);
        }
    }

    /**
     * This method finds the last given answer before rotation and again sets the screen
     * on this view. Also if "reset" or "submit" button were clicked,
     * repeats what was done after click (changes button color, shows wrong and correct answers)
     */
    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);

        // It restores the focus on the last clicked view before rotation (instead of last EditText)
        int focusID = inState.getInt("focusID", View.NO_ID);

        View focusedChild = findViewById(focusID);
        if (focusedChild != null) {
            focusedChild.requestFocus();

            if (focusedChild instanceof EditText) {
                int cursorLoc = inState.getInt("cursorLoc", 0);
                ((EditText) focusedChild).setSelection(cursorLoc);
            }
        }
        nobelPrizes.setText(ANSWER_2);
        mickiewicz.setText(ANSWER_6);
        inState.getBoolean("isSubmitted");
        if (submitPressed) {
            giveColorToAnswers();
        }
        inState.getBoolean("resetClicked");
        if (resetClicked) {
            resetButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            resetButton.setTextColor(getResources().getColor(R.color.colorBackground));
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
        sendEmail.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        sendEmail.setTextColor(getResources().getColor(R.color.colorBackground));

        giveColorToAnswers();
    }

    private void giveColorToAnswers() {
        // good answers turn green - showing right answers
        for (int i = 0; i < 8; i++) {
            allButtons.get(i).setTextColor(getResources().getColor(R.color.colorCorrect));
        }
        // wrong answers in CompoundButtons turn red
        for (int i = 8; i < 23; i++) {
            if (allButtons.get(i).isChecked()) {
                allButtons.get(i).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }

        // EditTexts (Q2 and Q6): good answers --> green, bad --> red
        String answerTwo = nobelPrizes.getText().toString();
        if (answerTwo.equals("4")) {
            nobelPrizes.setTextColor(getResources().getColor(R.color.colorCorrect));
        } else {
            nobelPrizes.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            correctAnswer2.setText(getString(R.string.four));
        }

        String answerSix = mickiewicz.getText().toString();
        if (answerSix.equalsIgnoreCase("adam mickiewicz") || answerSix.equalsIgnoreCase("adam mickiewicz ")) {
            mickiewicz.setTextColor(getResources().getColor(R.color.colorCorrect));
        } else {
            mickiewicz.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            correctAnswer6.setText(getString(R.string.adam_mickiewicz));
        }
}

    /**
     * Check the answers and calculate the score. It's called in createScoreMessage method.
     */
    private int calculateScore() {
        // Check the answer to the question 1.
        int points = 0;
         if (thirteenthCentury.isChecked()) {
            points += 1;
        }
        // Check the answer to the question 2.
        String numberOfNobels = nobelPrizes.getText().toString();
        if (numberOfNobels.equals("4")) {
            points += 1;
        }
        // Check the correct answers to the question 3.
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
        if (witcher.isChecked()) {
            points += 1;
        }
        // Check the answer to the question 5.
        if (brzechwa.isChecked()) {
            points += 1;
        }
        // Check the answer to the question 6.
        String adamMickiewicz = mickiewicz.getText().toString();
        if (adamMickiewicz.equalsIgnoreCase("adam mickiewicz") || adamMickiewicz.equalsIgnoreCase("adam mickiewicz ")) {
            points += 1;
        }
        // Check the answer to the question 7.
        if (thorgal.isChecked()) {
            points += 1;
        }
        return points;
    }

    /**
     * This method creates a toast message with score - after clicking "Submit" button.
     */
    private void displayToastMessage(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    /**
     * This happens after clicking "Share" button:
     * 1. If the submit button hadn't been clicked, it shows a warning toast.
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
     * Displays toast message after clicking "send" button, in case "submit" hadn't been clicked yet.
     */
    private void displayMessageTooSoon(String shareTooSoon) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, shareTooSoon, duration);
        toast.show();
    }

    /**
     * This method creates the e-mail text.
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
     * What happens when "reset" button is clicked once and twice.
     */

    public void resetQuiz(View v) {
        if (!resetClicked) {
            displayResetWarning(getString(R.string.reset_warning));
            resetButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            resetButton.setTextColor(getResources().getColor(R.color.colorBackground));
            resetClicked = true;
        } else {
            nameField.getText().clear();
            nobelPrizes.getText().clear();
            correctAnswer2.setText("");
            mickiewicz.getText().clear();
            correctAnswer6.setText("");

            for (int i = 0; i < 23; i++) {
                allButtons.get(i).setTextColor(getResources().getColor(R.color.colorSecondaryText));
                allButtons.get(i).setChecked(false);
            }
            nobelPrizes.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            mickiewicz.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            nameField.requestFocus();

            Button sendEmailButton = findViewById(R.id.send_email_button);
            sendEmailButton.setBackgroundColor(getResources().getColor(R.color.colorButtonLight));
            sendEmailButton.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            resetButton.setBackgroundColor(getResources().getColor(R.color.colorButtonLight));
            resetButton.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            resetClicked = false;
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