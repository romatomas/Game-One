package tfcgames.guessthedrink;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import tfcgames.guessthedrink.DataBaseOperation.DBHelper;
import tfcgames.guessthedrink.DataBaseOperation.DataBaseConnector;
import tfcgames.guessthedrink.Entity.Frame;

public class GameActivity extends MainActivity{
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

    private Integer levelId;
    private DBHelper dbHelper;
    private ArrayList<Frame> currentLevel = new ArrayList<Frame>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // got activity
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

        Intent intent = getIntent();
        this.levelId = intent.getIntExtra("levelId", -1);

        // отобразил
        setUIVisible(true);

        startLevelNew();

        this.btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatchNew(0);
            }
        });

        this.btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatchNew(1);
            }
        });

        this.btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatchNew(2);
            }
        });

        this.btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatchNew(3);
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

    // show/hide controls
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

    //BACK button processing
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameActivity.this, SelectLvlActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    private String getLevelPath() {
        return "pics/level_" + levelId.toString();
    }

    //new version for start level
    private void startLevelNew() {
        try {
            DataBaseConnector dbConnector = new DataBaseConnector(this, dbHelper);
            dbConnector.open();
            Cursor cPicturesList = dbConnector.getImgList(levelId);
            if (cPicturesList != null) {
                if (cPicturesList.moveToFirst()){
                    do {
                        Frame tempFrame = new Frame(cPicturesList.getString(cPicturesList.getColumnIndex("imgCaption")),
                                                    cPicturesList.getInt(cPicturesList.getColumnIndex("complexity")),
                                                    levelId,
                                                    dbConnector);
                        tempFrame.fillFalseImageList(); // Load false variants
                        currentLevel.add(tempFrame);
                    } while (cPicturesList.moveToNext());
                }
            }
            dbConnector.close();
            nextPictureNew(0);
        } catch (Exception e) {
            Log.d("GTD_LOG", e.getMessage().toString());
        }

    }

    private void nextPictureNew(int indexPicture) throws IOException {
        Frame frm = currentLevel.get(indexPicture);
        valueArrayIndex = indexPicture;
        InputStream ims = getAssets().open(getLevelPath() + "/" + frm.getPicture() + ".JPG");
        Drawable d = Drawable.createFromStream(ims, null);
        imgPhoto.setImageDrawable(d);
        // Заполнить картинки неверными вариантами
        for (int i = 0 ; i < btnSet.length; i++) {
            btnSet[i].setText(currentLevel.get(indexPicture).getFalseImageList().get(i));
        }
        //вывожу на рандомную кнопку правильный ответ (картинка, которая выводится на экран в данный момент)
        int j = (int)(Math.random() * btnSet.length);
        btnSet[j].setText(currentLevel.get(indexPicture).getPicture());
    }

    private void isMatchNew(int i){
        //проверка на правильный ответ (беру текст с кнопки и сверяю с именем картинки)
        if (currentLevel.get(valueArrayIndex).getPicture().equals(btnSet[i].getText().toString())) {
            //увеличиваю счетчик очков на единицу
            this.txtScore.setText(String.valueOf(Integer.valueOf(this.txtScore.getText().toString()) + 1));
            //проверка - не закончились ли картинки
            if (valueArrayIndex >= currentLevel.size() - 1){
                setUIVisible(false);
                this.txtInfo.setText("Поздравляем, Вы закончили уровень и заработали " + txtScore.getText() + " очков!");
            } else {  //если картинки не закончились, то увеличиваю счетчик очков и перехожу на следующую картинку
                try {
                    nextPictureNew(valueArrayIndex + 1);
                }
                catch (IOException ex){
                    //return;
                }
            }
        } else {  //если ответ не правильный, то конец игры
            setUIVisible(false);
            txtInfo.setText("Вы заработали " + this.txtScore.getText() + " очков!");
        }
    }
}
