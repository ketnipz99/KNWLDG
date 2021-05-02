package com.example.knwldg;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;


public class gameActivity extends AppCompatActivity {

    private TextView timer, currentQuesCount, eCoinsGame;
    private TextView question;
    private CheckBox option_A, option_B, option_C, option_D;
    private Button confirmAns, exit, boost;

    private List<Question> questionList;

    private int questionCounter = 0;
    private int questionCountTotal;
    private Question currentQuestion;

    //Timer-related variables declaration
    private static final long COUNTDOWN_IN_MIlLIS = 15000;
    private ColorStateList timerColorDefault;
    public CountDownTimer countDownTimer;
    public long timeRemainingMillis;

    private int eCoinsEarned = 0;
    private boolean answered;

    //Declaration for the boost popup window's widget
    private Button useBoost_1, useBoost_2, useBoost_3, useBoost_4, cancelBoost;
    private TextView remainingBoost_1, remainingBoost_2, remainingBoost_3, remainingBoost_4;
    public int addTime_5_left = 0;
    public int addTime_10_left = 0;
    public int addTime_15_left = 0;
    public int hintBoost_left = 0;
    public String boostType = "";

    private UserDatabaseHelper db;
    private QuesDbAccessHelper quesDbAccessHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        //Pass the corresponding question ID (foreign key) from selected tutorial list in gameTutorial
        //ID list to query the questions corresponding to the shown tutorial
        Intent intent = getIntent();
        int[] quesIDList = intent.getIntArrayExtra("quesIdList");

        timer = findViewById(R.id.tvTimer);
        currentQuesCount = findViewById(R.id.tvCurrentQues);
        eCoinsGame = findViewById(R.id.tvCoinsQues);
        question = findViewById(R.id.tvQues);
        option_A = findViewById(R.id.cbOptA);
        option_B = findViewById(R.id.cbOptB);
        option_C = findViewById(R.id.cbOptC);
        option_D = findViewById(R.id.cbOptD);
        confirmAns = findViewById(R.id.btnFinishQuiz);
        exit = findViewById(R.id.btnExit);
        boost = findViewById(R.id.btnUseBoost);

        timerColorDefault = timer.getTextColors();

        //Access the question database
        quesDbAccessHelper = QuesDbAccessHelper.getInstance(this);
        quesDbAccessHelper.openDatabase();

        //Fetching questions into questionList
        questionList = quesDbAccessHelper.getQuestion(quesIDList);
        questionCountTotal = questionList.size();

        //Shuffle the ques
        Collections.shuffle(questionList);

        //Setting the widgets
        confirmAns.setEnabled(false);
        eCoinsGame.setText(Integer.toString(eCoinsEarned));

        //Start displaying the questions
        showNextQuestion();
        quesDbAccessHelper.closeDatabase();

        //Setting the checkbox only check one answer in a time
        option_A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    option_B.setChecked(false);
                    option_C.setChecked(false);
                    option_D.setChecked(false);
                    confirmAns.setEnabled(true);
                }
            }
        });
        option_B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    option_A.setChecked(false);
                    option_C.setChecked(false);
                    option_D.setChecked(false);
                    confirmAns.setEnabled(true);
                }
            }
        });
        option_C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    option_A.setChecked(false);
                    option_B.setChecked(false);
                    option_D.setChecked(false);
                    confirmAns.setEnabled(true);
                }
            }
        });
        option_D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    option_A.setChecked(false);
                    option_B.setChecked(false);
                    option_C.setChecked(false);
                    confirmAns.setEnabled(true);
                }
            }
        });

        confirmAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });

        boost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pause the time when the "Boost" button is pressed
                timerPause();
                showBoostMenu(v);

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerPause();
                exitAlertDialog();
            }
        });
    }

    @Override
    public void onBackPressed () {
        timerPause();
        exitAlertDialog();
    }


    public void exitAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Exiting now will lose all the progress\nAre you sure?");

        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(gameActivity.this, home.class);
                startActivity(intent);
                finish();
                Toast.makeText(gameActivity.this, "Goodbye", Toast.LENGTH_LONG).show();
            }
        });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                timerResume();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showNextQuestion() {
        //Unchecked all the option buttons
        option_A.setChecked(false);
        option_B.setChecked(false);
        option_C.setChecked(false);
        option_D.setChecked(false);
        confirmAns.setEnabled(false);

        if (questionCounter < questionCountTotal) {     //displaying the question one after another
            currentQuestion = questionList.get(questionCounter);

            question.setText(currentQuestion.getQuestion());
            option_A.setText(currentQuestion.getOption_1());
            option_B.setText(currentQuestion.getOption_2());
            option_C.setText(currentQuestion.getOption_3());
            option_D.setText(currentQuestion.getOption_4());

            timeRemainingMillis = COUNTDOWN_IN_MIlLIS;
            startCountdownTimer(timeRemainingMillis);

            if (questionCounter == 9) {
                confirmAns.setText("End game");
            }

            currentQuesCount.setText(Integer.toString(questionCounter + 1));
            answered = false;
            questionCounter++;

        } else {
            finishGame();   //Call to show game result summary and then back home
        }
    }

    private void startCountdownTimer(long timeLengthMillis) {
        countDownTimer = new CountDownTimer(timeLengthMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timeRemainingMillis = 0;
                updateTimer();
                checkAnswer();
            }
        }.start();
    }


    private void checkAnswer() {
        answered = true;            //Check user answer the question or not
        countDownTimer.cancel();    //Stop the timer
        int selectedAnswer = 0;     //Initialized selectedAnswer and result
        boolean result = false;
        String correctAns = "";

        if (option_A.isChecked()) { //Convert chosen answer into selectedAnswer
            selectedAnswer = 1;
        } else if (option_B.isChecked()) {
            selectedAnswer = 2;
        } else if (option_C.isChecked()) {
            selectedAnswer = 3;
        } else if (option_D.isChecked()) {
            selectedAnswer = 4;
        }

        //checking on the correct answer
        if (selectedAnswer == currentQuestion.getAnswer()) {
            eCoinsEarned = eCoinsEarned + 20;   //Earn eCoin is correct
            result = true;
            eCoinsGame.setText(Integer.toString(eCoinsEarned));
        } else {
            result = false;

        }
        showCorrectAns(result, currentQuestion.getAnswer());
    }

    public void showCorrectAns(boolean result, int correctAnswer) {
        //Popup window initialization; Inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window_result, null);

        View view = new View(this);
        String comment = "";
        String message = new String(comment);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TypedArray images = getResources().obtainTypedArray(R.array.meme_images);

        //Random number generator for selecting picture to display (ranged from 1 to 99)
        Random random = new Random();
        int memeChoice = 0;

        //Declaration for the result popup window's widgets
        TextView resultCaption = popupView.findViewById(R.id.tvResult);
        ImageView memes = popupView.findViewById(R.id.ivMeme);

        //Display the outcome and the correct answer if user answered wrongly
        if (result) {
            message = "Well done!!  Your answer is correct $,$ \n\n\n + 20$";
            memeChoice = random.nextInt(14);

        } else if (correctAnswer == 1) {
            message = "WRONG!!!\nThe correct answer is\n" + currentQuestion.getOption_1() + "\n\n No coin earned";
            memeChoice = random.nextInt(30 - 14) + 14;
        } else if (correctAnswer == 2) {
            message = "WRONG!!!\nThe correct answer is\n" + currentQuestion.getOption_2() + "\n\n No coin earned";
            memeChoice = random.nextInt(30 - 14) + 14;
        } else if (correctAnswer == 3) {
            message = "WRONG!!!\nThe correct answer is\n" + currentQuestion.getOption_3() + "\n\n No coin earned";
            memeChoice = random.nextInt(30 - 14) + 14;
        } else if (correctAnswer == 4) {
            message = "WRONG!!!\nThe correct answer is\n" + currentQuestion.getOption_4() + "\n\n No coin earned";
            memeChoice = random.nextInt(30 - 14) + 14;
        }

        memes.setImageResource(images.getResourceId(memeChoice, R.drawable.meme_0));
        images.recycle();
        resultCaption.setText(message);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);  //Display the popup window

        //dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                showNextQuestion();
                return true;
            }
        });
    }

    private void showBoostMenu(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window_boost, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false;
        final PopupWindow popupBoost = new PopupWindow(popupView, width, height, focusable);
        //popupInGameBoost boostOperation = new popupInGameBoost(this);

        //Pass the logged in email for SQLite query (session management)
        SharedPreferences preferencesEmail = this.getSharedPreferences(EMAIL, MODE_PRIVATE);
        String email = preferencesEmail.getString("email", "");

        String remainingBoost = "Remaining: ";

        useBoost_1 = popupView.findViewById(R.id.btnUseBoost_1);
        useBoost_2 = popupView.findViewById(R.id.btnUseBoost_2);
        useBoost_3 = popupView.findViewById(R.id.btnUseBoost_3);
        useBoost_4 = popupView.findViewById(R.id.btnUseBoost_4);
        remainingBoost_1 = popupView.findViewById(R.id.tvRemainingBoost_1);
        remainingBoost_2 = popupView.findViewById(R.id.tvRemainingBoost_2);
        remainingBoost_3 = popupView.findViewById(R.id.tvRemainingBoost_3);
        remainingBoost_4 = popupView.findViewById(R.id.tvRemainingBoost_4);
        cancelBoost = popupView.findViewById(R.id.btnCancelBoost);

        popupBoost.showAtLocation(view, Gravity.CENTER, 0, 0);

        db = new UserDatabaseHelper(this);
        try (Cursor cursor = db.fetchUserBoost(email)) {

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    addTime_5_left = Integer.parseInt(cursor.getString(0));
                    addTime_10_left = Integer.parseInt(cursor.getString(1));
                    addTime_15_left = Integer.parseInt(cursor.getString(2));
                    hintBoost_left = Integer.parseInt(cursor.getString(3));
                }
            }
        }

        remainingBoost_1.setText(remainingBoost + Integer.toString(addTime_5_left));
        remainingBoost_2.setText(remainingBoost + Integer.toString(addTime_10_left));
        remainingBoost_3.setText(remainingBoost + Integer.toString(addTime_15_left));
        remainingBoost_4.setText(remainingBoost + Integer.toString(hintBoost_left));

        if (addTime_5_left == 0) {
            useBoost_1.setEnabled(false);
        } else {
            useBoost_1.setEnabled(true);
        }

        if (addTime_10_left == 0) {
            useBoost_2.setEnabled(false);
        } else {
            useBoost_2.setEnabled(true);
        }

        if (addTime_15_left == 0) {
            useBoost_3.setEnabled(false);
        } else {
            useBoost_3.setEnabled(true);
        }

        if (hintBoost_left == 0) {
            useBoost_4.setEnabled(false);
        } else {
            useBoost_4.setEnabled(true);
        }

        useBoost_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeRemainingMillis = timeRemainingMillis + 5000;
                boostType = "addTime_5";
                addTime_5_left = addTime_5_left - 1;
                db.updateBoost(email, boostType, addTime_5_left);
                popupBoost.dismiss();
                timerResume();
            }
        });

        useBoost_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeRemainingMillis = timeRemainingMillis + 10000;
                boostType = "addTime_10";
                addTime_10_left = addTime_10_left - 1;
                db.updateBoost(email, boostType, addTime_10_left);
                popupBoost.dismiss();
                timerResume();
            }
        });

        useBoost_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeRemainingMillis = timeRemainingMillis + 15000;
                boostType = "addTime_15";
                addTime_15_left = addTime_15_left - 1;
                db.updateBoost(email, boostType, addTime_15_left);
                popupBoost.dismiss();
                timerResume();
            }
        });

        useBoost_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeRemainingMillis = timeRemainingMillis + 5000;
                boostType = "showHintBoost";
                hintBoost_left = hintBoost_left - 1;
                db.updateBoost(email, boostType, hintBoost_left);
                popupBoost.dismiss();
                timerResume();
            }
        });

        cancelBoost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupBoost.dismiss();
                timerResume();
            }
        });

    }

    private void finishGame() {
        Intent intent = new Intent(gameActivity.this, gameSummary.class);
        intent.putExtra("eCoinScore", eCoinsEarned);
        startActivity(intent);
        finish();
    }

    public void timerPause() {
        countDownTimer.cancel();
    }

    private void timerResume() {
        startCountdownTimer(timeRemainingMillis);
    }

    private void updateTimer() {
        int minutes = (int) (timeRemainingMillis / 1000) / 60;
        int seconds = (int) (timeRemainingMillis / 1000) % 60;

        String formatTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timer.setText(formatTime);

        if (timeRemainingMillis < 10000) {
            timer.setTextColor(Color.RED);      //Change timer colour when only 10 seconds left
        } else {
            timer.setTextColor(timerColorDefault);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}