package com.example.phonecompanion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {



    public static final String EXTRA_TEXT = "com.example.myapplication.EXTRA_TEXT";
    private EditText editText;
    private TextView textView;

    String code="";

    private String text;

    private String text1;


    private TextView Status;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String TEXT1 = "text";
    public static AudioManager myAudioManager;
    public static NotificationManager notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText =  findViewById(R.id.edit_code);
        Button saveButton = findViewById(R.id.save_btn);
        textView = findViewById(R.id.text1);





        Status = findViewById(R.id.textView2);

        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);






        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                textView.setText(editText.getText().toString());
                code= editText.getText().toString();
                saveData();
            }
        });

        loadData();
        updateViews();

        Intent in = new Intent("my.action.string");
        in.putExtra("pass", code);
        sendBroadcast(in);
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT1, code);
        editor.putString(TEXT, textView.getText().toString());


        editor.apply();

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text1 = sharedPreferences.getString(TEXT, "");
        text = sharedPreferences.getString(TEXT, "");


    }

    public void updateViews() {
        code = text1;
        textView.setText(text);

    }





    public void vibrate(View view){
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }
    public void ring(View view){
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
    public void silent(View view){
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }
    @SuppressLint("SetTextI18n")
    public void mode(View view){
        int mod = myAudioManager.getRingerMode();
        if(mod == AudioManager.RINGER_MODE_NORMAL){
            Status.setText("Current Status: Ring");
        }
        else if(mod == AudioManager.RINGER_MODE_SILENT){
            Status.setText("Current Status: Silent");
        }
        else if(mod == AudioManager.RINGER_MODE_VIBRATE){
            Status.setText("Current Status: Vibrate");
        }

    }


}
