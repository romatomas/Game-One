package tfcgames.guessthedrink;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import tfcgames.guessthedrink.DataBaseOperation.DBHelper;
import tfcgames.guessthedrink.DataBaseOperation.DataBaseConnector;
import tfcgames.guessthedrink.FaceBookConnector.FBConnector;

//keytool -exportcert -alias androiddebugkey -keystore "c:\Users\e2-User\.android\debug.keystore" | "c:\OpenSSL-Win64\bin\openssl.exe" sha1 -binary | "c:\OpenSSL-Win64\bin\openssl.exe" base64
//keytool -exportcert -alias guessthedrink -keystore "d:\TFC_Games\android.jks " | "c:\OpenSSL-Win64\bin\openssl.exe" sha1 -binary | "c:\OpenSSL-Win64\bin\openssl.exe" base64
//Key Hashes
//Yt4237+VQo5qW1XXbuvVyqXBUNA=

public class MainActivity extends ActionBarActivity {

<<<<<<< HEAD
    private Button btnDatabase;
    private DataBaseConnector dbConnector;

    private DBHelper dbHelper;
=======
    private FacebookConnector facebookConnector;
>>>>>>> origin/master

    private FBConnector fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnStart = (Button) findViewById(R.id.btnStart);
        final ImageButton imgBtnSound = (ImageButton) findViewById(R.id.imgBtnSound);
        final ImageButton imgBtnFB = (ImageButton) findViewById(R.id.imgBtnFB);

<<<<<<< HEAD
        btnDatabase = (Button) findViewById(R.id.btnDatabase);

        fb = new FBConnector(MainActivity.this, MainActivity.this);
        //нажатие на кнопку FB авторизации
        imgBtnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FBConnector fb = new FBConnector(MainActivity.this, MainActivity.this);
                fb.authorizeAndPostMassage();
=======
        facebookConnector = new FacebookConnector(MainActivity.this, MainActivity.this);
        //press on FB button
        imgBtnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookConnector.facebookPublish("Guess The Drink", "It's a final countdown!",
                        "New incredible game by TFC Games", "http://tfcgames.com",
                        "http://i.gyazo.com/93f87e866500a145288e7471f5508076.png");
>>>>>>> origin/master
            }
        });

        //press on START button
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectLvlActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
            }
        });

        //on/off sound
        imgBtnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = getValSound() + 1;
                if ((i%2) == 0){
                    imgBtnSound.setBackgroundResource(R.drawable.sound_on);
                }
                else{imgBtnSound.setBackgroundResource(R.drawable.sound_off);}
                setValSound(i);
            }
        });

<<<<<<< HEAD
        // Работа с БД (временно тут)
        btnDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbConnector = new DataBaseConnector(v.getContext(), dbHelper);
                dbConnector.open();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fb.getFacebook().authorizeCallback(requestCode, resultCode, data);
    }

    //получить и установить значение для кнопки звука
=======
    //get and set value for SOUND button
>>>>>>> origin/master
    int value_sound;
    public int getValSound(){
        return value_sound;
    }

    public void setValSound(int i){
        value_sound = i;
    }

    //BACK button processing
    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    //are you sure you want to quit?
    private void openQuitDialog() {
<<<<<<< HEAD
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(MainActivity.this);
        quitDialog.setTitle("Выход: Вы уверены?");
        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
=======
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                MainActivity.this);
        quitDialog.setTitle("Exit: Are you sure?");

        quitDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
>>>>>>> origin/master
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }
}

