package com.example.android.polishliteraturequiz;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.polishliteraturequiz.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    boolean submitPressed;
    boolean resetClicked;
    List<CompoundButton> allButtons = new ArrayList<>();
    // two variables to save and restore what a user wrote in Q2&6 - to show (or not)
    // the correct answers underneath after rotation (without it was showing them always)
    private static String ANSWER_2 = "answer2";
    private static String ANSWER_6 = "answer6";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        resetClicked = false;
        // prevent from opening keyboard on creation
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // add elements to all CompoundButtons list, first correct ones (0-7), then wrong (8-22)
        allButtons.add(binding.thirteenthCentury);
        allButtons.add(binding.reymontCheckbox);
        allButtons.add(binding.miloszCheckbox);
        allButtons.add(binding.szymborskaCheckbox);
        allButtons.add(binding.sienkiewiczCheckbox);
        allButtons.add(binding.witcherRadiobutton);
        allButtons.add(binding.brzechwaRadiobutton);
        allButtons.add(binding.thorgalRadiobutton);
        allButtons.add(binding.tenthCentury);
        allButtons.add(binding.fifteenthCentury);
        allButtons.add(binding.lemCheckbox);
        allButtons.add(binding.baumanCheckbox);
        allButtons.add(binding.herbertCheckbox);
        allButtons.add(binding.walesaCheckbox);
        allButtons.add(binding.gollumRadiobutton);
        allButtons.add(binding.kingArthurRadiobutton);
        allButtons.add(binding.reaperManRadiobutton);
        allButtons.add(binding.lesmianRadiobutton);
        allButtons.add(binding.gombrowiczRadiobutton);
        allButtons.add(binding.tuwimRadiobutton);
        allButtons.add(binding.wolverineRadiobutton);
        allButtons.add(binding.spidermanRadiobutton);
        allButtons.add(binding.supermanRadiobutton);

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
            binding.numberOfPrizes.setText(ANSWER_2);
            savedInstanceState.getString("answer6");
            binding.adamMickiewiczEdittext.setText(ANSWER_6);
            submitPressed = savedInstanceState.getBoolean("isSubmitted");
            if (submitPressed) {
                binding.sendEmailButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.sendEmailButton.setTextColor(getResources().getColor(R.color.colorBackground));
                giveColorToAnswers();
            }
            resetClicked = savedInstanceState.getBoolean("resetClicked");
            if (resetClicked) {
                binding.resetButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                binding.resetButton.setTextColor(getResources().getColor(R.color.colorBackground));
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

            ANSWER_2 = binding.numberOfPrizes.getText().toString();
            ANSWER_6 = binding.adamMickiewiczEdittext.getText().toString();

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
        binding.numberOfPrizes.setText(ANSWER_2);
        binding.adamMickiewiczEdittext.setText(ANSWER_6);
        inState.getBoolean("isSubmitted");
        if (submitPressed) {
            giveColorToAnswers();
        }
        inState.getBoolean("resetClicked");
        if (resetClicked) {
            binding.resetButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            binding.resetButton.setTextColor(getResources().getColor(R.color.colorBackground));
        }
   }

    /**
     * This method shows an appropriate text in a toast.
     */
    public void createScoreMessage(View view) {
        int score = calculateScore();
        String name = binding.nameView.getText().toString();
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
        binding.sendEmailButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        binding.sendEmailButton.setTextColor(getResources().getColor(R.color.colorBackground));

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
        String answerTwo = binding.numberOfPrizes.getText().toString();
        if (answerTwo.equals("4")) {
            binding.numberOfPrizes.setTextColor(getResources().getColor(R.color.colorCorrect));
        } else {
            binding.numberOfPrizes.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            binding.correctAnswer2.setText(getString(R.string.four));
        }

        String answerSix = binding.adamMickiewiczEdittext.getText().toString();
        if (answerSix.equalsIgnoreCase("adam mickiewicz") || answerSix.equalsIgnoreCase("adam mickiewicz ")) {
            binding.adamMickiewiczEdittext.setTextColor(getResources().getColor(R.color.colorCorrect));
        } else {
            binding.adamMickiewiczEdittext.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            binding.correctAnswer6.setText(getString(R.string.adam_mickiewicz));
        }
}

    /**
     * Check the answers and calculate the score. It's called in createScoreMessage method.
     */
    private int calculateScore() {
        // Check the answer to the question 1.
        int points = 0;
         if (binding.thirteenthCentury.isChecked()) {
            points += 1;
        }
        // Check the answer to the question 2.
        String numberOfNobels = binding.numberOfPrizes.getText().toString();
        if (numberOfNobels.equals("4")) {
            points += 1;
        }
        // Check the correct answers to the question 3.
        if (binding.reymontCheckbox.isChecked()) {
            points += 1;
        }
        if (binding.miloszCheckbox.isChecked()) {
            points += 1;
        }
        if (binding.szymborskaCheckbox.isChecked()) {
            points += 1;
        }
        if (binding.sienkiewiczCheckbox.isChecked()) {
            points += 1;
        }
        // Check the answer to the question 4.
        if (binding.witcherRadiobutton.isChecked()) {
            points += 1;
        }
        // Check the answer to the question 5.
        if (binding.brzechwaRadiobutton.isChecked()) {
            points += 1;
        }
        // Check the answer to the question 6.
        String adamMickiewicz = binding.adamMickiewiczEdittext.getText().toString();
        if (adamMickiewicz.equalsIgnoreCase("adam mickiewicz") || adamMickiewicz.equalsIgnoreCase("adam mickiewicz ")) {
            points += 1;
        }
        // Check the answer to the question 7.
        if (binding.thorgalRadiobutton.isChecked()) {
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
        String name = binding.nameView.getText().toString();
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
            binding.resetButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            binding.resetButton.setTextColor(getResources().getColor(R.color.colorBackground));
            resetClicked = true;
        } else {
            binding.nameView.getText().clear();
            binding.numberOfPrizes.getText().clear();
            binding.correctAnswer2.setText("");
            binding.adamMickiewiczEdittext.getText().clear();
            binding.correctAnswer6.setText("");

            for (int i = 0; i < 23; i++) {
                allButtons.get(i).setTextColor(getResources().getColor(R.color.colorSecondaryText));
                allButtons.get(i).setChecked(false);
            }
            binding.numberOfPrizes.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            binding.adamMickiewiczEdittext.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            binding.nameView.requestFocus();

            binding.sendEmailButton.setBackgroundColor(getResources().getColor(R.color.colorButtonLight));
            binding.sendEmailButton.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            binding.resetButton.setBackgroundColor(getResources().getColor(R.color.colorButtonLight));
            binding.resetButton.setTextColor(getResources().getColor(R.color.colorPrimaryText));
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