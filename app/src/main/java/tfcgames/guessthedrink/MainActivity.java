package tfcgames.guessthedrink;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import tfcgames.guessthedrink.DataBaseOperation.DBHelper;
import tfcgames.guessthedrink.DataBaseOperation.DataBaseConnector;
import tfcgames.guessthedrink.FaceBookConnector.FBConnector;

//keytool -exportcert -alias androiddebugkey -keystore "c:\Users\e2-User\.android\debug.keystore" | "c:\OpenSSL-Win64\bin\openssl.exe" sha1 -binary | "c:\OpenSSL-Win64\bin\openssl.exe" base64
//keytool -exportcert -alias guessthedrink -keystore "d:\TFC_Games\android.jks " | "c:\OpenSSL-Win64\bin\openssl.exe" sha1 -binary | "c:\OpenSSL-Win64\bin\openssl.exe" base64
//Key Hashes
//Yt4237+VQo5qW1XXbuvVyqXBUNA=

public class MainActivity extends ActionBarActivity {

    private Button btnDatabase;
    private DataBaseConnector dbConnector;

    private DBHelper dbHelper;

    private FBConnector fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnStart = (Button) findViewById(R.id.btnStart);
        final ImageButton imgBtnSound = (ImageButton) findViewById(R.id.imgBtnSound);
        final TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        final ImageButton imgBtnFB = (ImageButton) findViewById(R.id.imgBtnFB);

        btnDatabase = (Button) findViewById(R.id.btnDatabase);

        fb = new FBConnector(MainActivity.this, MainActivity.this);
        //нажатие на кнопку FB авторизации
        imgBtnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FBConnector fb = new FBConnector(MainActivity.this, MainActivity.this);
                fb.authorizeAndPostMassage();
            }
        });

        //нажатие на кнопку START
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectLvlActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
            }
        });

        //включить/выключить звук в приложении
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
    int value_sound;
    public int getValSound(){
        return value_sound;
    }

    public void setValSound(int i){
        value_sound = i;
    }

    //обработка нажатия кнопки BACK
    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    //открыть диалоговое окно при нажатии кнопки BACK на главном экране
    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(MainActivity.this);
        quitDialog.setTitle("Выход: Вы уверены?");
        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }
}
