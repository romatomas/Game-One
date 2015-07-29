package tfcgames.guessthedrink;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by e2-User on 13.05.2015.
 */
public class GameActivity extends MainActivity{

    //получить и установить текущий массив картинок
    private String[] currentSetOfPictures;

    //получить и установить значение текущего индекса главного игрового массива строк
    private int valueArrayIndex;

    // объявляем активити с которыми патом будем работать
    private Button btnA;
    private Button btnB;
    private Button btnC;
    private Button btnD;
    private Button btnOK;

    private TextView txtInfo;
    private TextView txtScore;
    private TextView txtScoreView;

    private ImageView imgPhoto;
    private ImageButton imgBtnBackGame;

    Button btnSet[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // получил активити
        this.btnA = (Button) findViewById(R.id.btnA);
        this.btnB = (Button) findViewById(R.id.btnB);
        this.btnC = (Button) findViewById(R.id.btnC);
        this.btnD = (Button) findViewById(R.id.btnD);
        this.btnOK = (Button) findViewById(R.id.btnOK);

        this.txtInfo = (TextView) findViewById(R.id.txtInfo);
        this.txtScore = (TextView) findViewById(R.id.txtScore);
        this.txtScoreView = (TextView) findViewById(R.id.txtScoreView);

        this.imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        this.imgBtnBackGame = (ImageButton) findViewById(R.id.imgBtnBackGame);

        this.txtScore.setText("0");

        this.btnSet = new Button[]{this.btnA, this.btnB, this.btnC, this.btnD};
        // отобразил
        setUIVisible(true);

        startLvl();

        this.btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatch(0);
            }
        });

        this.btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatch(1);
            }
        });

        this.btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatch(2);
            }
        });

        this.btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatch(3);
            }
        });

        this.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, SelectLvlActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        imgBtnBackGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, SelectLvlActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
    }

    private void startLvl(){
        AssetManager am = getAssets();
        try {
            String[] pics = am.list("pics");
            shuffleArray(pics);
            this.currentSetOfPictures = pics;
            nextPicture(0);
        }
        catch (IOException ex){
            //return;
        }
    }

    //перетусовка массива
    private void shuffleArray(String[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    //обработка события нажатия на кнопку - вариант ответа
    private void isMatch(int i){
        //проверка на правильный ответ (беру текст с кнопки и сверяю с именем картинки)
        if (this.currentSetOfPictures[this.valueArrayIndex].contains(this.btnSet[i].getText().toString())){
            //увеличиваю счетчик очков на единицу
            this.txtScore.setText(String.valueOf(Integer.valueOf(this.txtScore.getText().toString()) + 1));
            //проверка - не закончились ли картинки
            if (this.valueArrayIndex >= this.currentSetOfPictures.length - 1){
                setUIVisible(false);
                this.txtInfo.setText("Поздравляем, Вы закончили уровень и заработали " + this.txtScore.getText() + " очков!");
            } else {  //если картинки не закончились, то увеличиваю счетчик очков и перехожу на следующую картинку
                try {
                    nextPicture(this.valueArrayIndex + 1);
                }
                catch (IOException ex){
                    //return;
                }
            }
        } else {  //если ответ не правильный, то конец игры
            setUIVisible(false);
            this.txtInfo.setText("Вы заработали " + this.txtScore.getText() + " очков!");
        }
    }

    // показать/скрыть компоненты
    private void setUIVisible(boolean isVisible) {
        if (isVisible) {
            this.btnA.setVisibility(View.VISIBLE);
            this.btnB.setVisibility(View.VISIBLE);
            this.btnC.setVisibility(View.VISIBLE);
            this.btnD.setVisibility(View.VISIBLE);
            this.btnOK.setVisibility(View.INVISIBLE);
            this.txtInfo.setVisibility(View.INVISIBLE);
            this.txtScore.setVisibility(View.VISIBLE);
            this.txtScoreView.setVisibility(View.VISIBLE);
            this.imgPhoto.setVisibility(View.VISIBLE);
        } else {
            this.btnA.setVisibility(View.INVISIBLE);
            this.btnB.setVisibility(View.INVISIBLE);
            this.btnC.setVisibility(View.INVISIBLE);
            this.btnD.setVisibility(View.INVISIBLE);
            this.btnOK.setVisibility(View.VISIBLE);
            this.txtInfo.setVisibility(View.VISIBLE);
            this.txtScore.setVisibility(View.INVISIBLE);
            this.txtScoreView.setVisibility(View.INVISIBLE);
            this.imgPhoto.setVisibility(View.INVISIBLE);
        }
    }

    private void nextPicture(int indexPicture) throws IOException {
        InputStream ims = getAssets().open("pics/" + this.currentSetOfPictures[indexPicture]);
        Drawable d = Drawable.createFromStream(ims, null);
        this.imgPhoto.setImageDrawable(d);
        this.valueArrayIndex = indexPicture;
        //генерирую случайные надписи для кнопок из имен файлов в папке assets
        int randomPic = (int)(Math.random() * this.currentSetOfPictures.length);
        ArrayList<Integer> buffer = new ArrayList<Integer>();
        for (int index = 0; index < this.btnSet.length; index++){
            while (buffer.contains(randomPic) || randomPic == indexPicture){
                randomPic = (int)(Math.random() * this.currentSetOfPictures.length);
            }
            buffer.add(randomPic);
            this.btnSet[index].setText(this.currentSetOfPictures[randomPic].substring(0, this.currentSetOfPictures[randomPic].lastIndexOf(".")));
        }
        //вывожу на рандомную кнопку правильный ответ (картинка, которая выводится на экран в данный момент)
        int j = (int)(Math.random() * this.btnSet.length);
        this.btnSet[j].setText(this.currentSetOfPictures[indexPicture].substring(0, this.currentSetOfPictures[indexPicture].lastIndexOf(".")));
    }

    //обработка нажатия кнопки BACK
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameActivity.this, SelectLvlActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
}
