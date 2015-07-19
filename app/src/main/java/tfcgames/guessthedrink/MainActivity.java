package tfcgames.guessthedrink;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
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

    //получить и установить текущий массив картинок
    String[] array;
    public String[] getMas(){
        return array;
    }
    public void setMas(String[] mas){
        array = mas;
    }

    //получить и установить значение текущего индекса главного игрового массива строк
    int valueArrayIndex;
    public int getArrayIndex(){
        return valueArrayIndex;
    }
    public void setArrayIndex(int i){
        valueArrayIndex = i;
    }

    public void startLvl(){
        final ImageView imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        final Button btnA = (Button) findViewById(R.id.btnA);
        final Button btnB = (Button) findViewById(R.id.btnB);
        final Button btnC = (Button) findViewById(R.id.btnC);
        final Button btnD = (Button) findViewById(R.id.btnD);
        AssetManager am = getAssets();
        try {
            String[] pics = am.list("pics");
            shuffleArray(pics);
            setMas(pics);
            int i = 0;
            InputStream ims = getAssets().open("pics/" + pics[i]);
            Drawable d = Drawable.createFromStream(ims, null);
            imgPhoto.setImageDrawable(d);
            setArrayIndex(i);
            Button[] btns = {btnA, btnB, btnC, btnD};

            //генерирую случайные надписи для кнопок из имен файлов в папке assets
            int randomPic = (int)(Math.random() * pics.length);
            ArrayList<Integer> buffer = new ArrayList<Integer>();
            for (int index = 0; index < btns.length; index++){
                while (buffer.contains(randomPic) || randomPic == i){
                    randomPic = (int)(Math.random() * pics.length);
                }
                buffer.add(randomPic);
                btns[index].setText(pics[randomPic].substring(0,pics[randomPic].lastIndexOf(".")));
            }

            //вывожу на рандомную кнопку правильный ответ (картинка, которая выводится на экран в данный момент)
            int j = (int)(Math.random() * btns.length);
            btns[j].setText(pics[i].substring(0,pics[i].lastIndexOf(".")));
        }
        catch (IOException ex){
            //return;
        }
    }

    //перетусовка массива
    static void shuffleArray(String[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    //обработка события нажатия на кнопку - вариант ответа
    public void isMatch(int i){
        final Button btnA = (Button) findViewById(R.id.btnA);
        final Button btnB = (Button) findViewById(R.id.btnB);
        final Button btnC = (Button) findViewById(R.id.btnC);
        final Button btnD = (Button) findViewById(R.id.btnD);
        final Button btnOK = (Button) findViewById(R.id.btnOK);
        final TextView txtScore = (TextView) findViewById(R.id.txtScore);
        final TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        final TextView txtScoreView = (TextView) findViewById(R.id.txtScoreView);
        final ImageView imgPhoto = (ImageView) findViewById(R.id.imgPhoto);

        String[] pics = getMas();
        Button[] btns = {btnA, btnB, btnC, btnD};
        int current = getArrayIndex() + 1;

        //проверка на правильный ответ (беру текст с кнопки и сверяю с именем картинки)
        if (pics[getArrayIndex()].contains(btns[i].getText().toString())){

            //увеличиваю счетчик очков на единицу
            txtScore.setText(String.valueOf(Integer.valueOf(txtScore.getText().toString()) + 1));

            //проверка - не закончились ли картинки
            if (getArrayIndex() >= getMas().length - 1){
                btnA.setVisibility(View.INVISIBLE);
                btnB.setVisibility(View.INVISIBLE);
                btnC.setVisibility(View.INVISIBLE);
                btnD.setVisibility(View.INVISIBLE);
                btnOK.setVisibility(View.VISIBLE);
                txtInfo.setVisibility(View.VISIBLE);
                txtScore.setVisibility(View.INVISIBLE);
                txtScoreView.setVisibility(View.INVISIBLE);
                imgPhoto.setVisibility(View.INVISIBLE);

                txtInfo.setText("Поздравляем, Вы закончили уровень и заработали " + txtScore.getText() + " очков!");
            }

            //если картинки не закончились, то увеличиваю счетчик очков и перехожу на следующую картинку
            else {
                try {
                    InputStream ims = getAssets().open("pics/" + pics[current]);
                    Drawable d = Drawable.createFromStream(ims, null);
                    imgPhoto.setImageDrawable(d);
                    setArrayIndex(current);

                    //генерирую случайные надписи для кнопок из имен файлов в папке assets
                    int randomPic = (int)(Math.random() * pics.length);
                    ArrayList<Integer> buffer = new ArrayList<Integer>();
                    for (int index = 0; index < btns.length; index++){
                        while (buffer.contains(randomPic) || randomPic == current){
                            randomPic = (int)(Math.random() * pics.length);
                        }
                        buffer.add(randomPic);
                        btns[index].setText(pics[randomPic].substring(0,pics[randomPic].lastIndexOf(".")));
                    }

                    //вывожу на рандомную кнопку правильный ответ (картинка, которая выводится на экран в данный момент)
                    int j = (int)(Math.random() * btns.length);
                    btns[j].setText(pics[current].substring(0,pics[current].lastIndexOf(".")));
                }
                catch (IOException ex){
                    //return;
                }
            }
        }

        //если ответ не правильный, то конец игры
        else {
            btnA.setVisibility(View.INVISIBLE);
            btnB.setVisibility(View.INVISIBLE);
            btnC.setVisibility(View.INVISIBLE);
            btnD.setVisibility(View.INVISIBLE);
            btnOK.setVisibility(View.VISIBLE);
            txtInfo.setVisibility(View.VISIBLE);
            txtScore.setVisibility(View.INVISIBLE);
            txtScoreView.setVisibility(View.INVISIBLE);
            imgPhoto.setVisibility(View.INVISIBLE);

            txtInfo.setText("Вы заработали " + txtScore.getText() + " очков!");
            }
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

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
