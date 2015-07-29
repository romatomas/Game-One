package tfcgames.guessthedrink;
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

//keytool -exportcert -alias androiddebugkey -keystore "c:\Users\e2-User\.android\debug.keystore" | "c:\OpenSSL-Win64\bin\openssl.exe" sha1 -binary | "c:\OpenSSL-Win64\bin\openssl.exe" base64
//keytool -exportcert -alias guessthedrink -keystore "d:\TFC_Games\android.jks " | "c:\OpenSSL-Win64\bin\openssl.exe" sha1 -binary | "c:\OpenSSL-Win64\bin\openssl.exe" base64
//Key Hashes
//Yt4237+VQo5qW1XXbuvVyqXBUNA=

public class MainActivity extends ActionBarActivity {

    public final String API_KEY = "1399587140356912";
    public final String[] permissions = {"publish_stream"};
    Facebook facebook = new Facebook(API_KEY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnStart = (Button) findViewById(R.id.btnStart);
        final ImageButton imgBtnSound = (ImageButton) findViewById(R.id.imgBtnSound);
        final TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        final ImageButton imgBtnFB = (ImageButton) findViewById(R.id.imgBtnFB);

        //нажатие на кнопку FB авторизации
        imgBtnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorizeAndPostMassage();
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
    }

    //авторизоваться и запостить сообщение на facebook
    public void authorizeAndPostMassage() {
        facebook.authorize(this, permissions, new Facebook.DialogListener() {
            @Override
            public void onComplete(Bundle values) {
                Toast.makeText(MainActivity.this, "Authorization successful", Toast.LENGTH_SHORT).show();
                postMessage();
            }

            @Override
            public void onFacebookError(FacebookError e) {
                Toast.makeText(MainActivity.this, "Facebook error, try again later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(DialogError e) {
                Toast.makeText(MainActivity.this, "Error, try again later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Authorization canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
    }

    private void postMessage() {
        Bundle messageBunlde = prepareBundle();
        facebook.dialog(this, "feed", messageBunlde, new Facebook.DialogListener() {

            @Override
            public void onComplete(Bundle values) {
                Toast.makeText(MainActivity.this, "Thank you!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFacebookError(FacebookError e) {
                Toast.makeText(MainActivity.this, "Facebook error, try again later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(DialogError e) {
                Toast.makeText(MainActivity.this, "Error, try again later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Authorization canceled", Toast.LENGTH_SHORT).show();
            }});
    }

    private Bundle prepareBundle() {
        Bundle postBundle = new Bundle();
        postBundle.putString("name", "Guess The Drink");
        postBundle.putString("caption", "It's a final countdown!");
        postBundle.putString("description", "New incredible game by TFC Games");
        postBundle.putString("link","http://tfcgames.com");
        postBundle.putString("picture","http://i.gyazo.com/93f87e866500a145288e7471f5508076.png");
        return postBundle;
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
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                MainActivity.this);
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
