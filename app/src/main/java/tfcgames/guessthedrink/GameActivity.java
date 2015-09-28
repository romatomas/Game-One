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
    //get and set current index value of main string array
    private int valueArrayIndex;

    // refresh activity
    private Button btnA;
    private Button btnB;
    private Button btnC;
    private Button btnD;

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

        this.txtScore = (TextView) findViewById(R.id.txtScore);
        this.txtScoreView = (TextView) findViewById(R.id.txtScoreView);

        this.imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        this.imgBtnBackGame = (ImageButton) findViewById(R.id.imgBtnBackGame);

        this.txtScore.setText("0");

        this.btnSet = new Button[]{this.btnA, this.btnB, this.btnC, this.btnD};

        Intent intent = getIntent();
        this.levelId = intent.getIntExtra("levelId", -1);

        // show
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
            this.txtScore.setVisibility(View.VISIBLE);
            this.txtScoreView.setVisibility(View.VISIBLE);
            this.imgPhoto.setVisibility(View.VISIBLE);
        } else {
            this.btnA.setVisibility(View.INVISIBLE);
            this.btnB.setVisibility(View.INVISIBLE);
            this.btnC.setVisibility(View.INVISIBLE);
            this.btnD.setVisibility(View.INVISIBLE);
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
        // fill pics with wrong variants
        for (int i = 0 ; i < btnSet.length; i++) {
            btnSet[i].setText(currentLevel.get(indexPicture).getFalseImageList().get(i));
        }
        // show on random button right answer (picture that displayed on the screen at this moment)
        int j = (int)(Math.random() * btnSet.length);
        btnSet[j].setText(currentLevel.get(indexPicture).getPicture());
    }

    private void isMatchNew(int i){
        // check on right answer (take text from button and check it with pic's name)
        if (currentLevel.get(valueArrayIndex).getPicture().equals(btnSet[i].getText().toString())) {
            // increment scorer on 1 point
            this.txtScore.setText(String.valueOf(Integer.valueOf(this.txtScore.getText().toString()) + 1));
            // check - pictures have ended?
            if (valueArrayIndex >= currentLevel.size() - 1){
                setUIVisible(false);
                Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                intent.putExtra("score", "Congratulations, You are finished the level and scored " + txtScore.getText() + " points!");
                startActivity(intent);
            } else {  // if pics not ended then increase scorer and go to the next pic
                try {
                    nextPictureNew(valueArrayIndex + 1);
                }
                catch (IOException ex){
                    //return;
                }
            }
        } else {  //if the answer is wrong then THE END
            setUIVisible(false);
            Intent intent = new Intent(GameActivity.this, ResultActivity.class);
            intent.putExtra("score", "You scored " + this.txtScore.getText() + " points!");
            startActivity(intent);
        }
    }
}
