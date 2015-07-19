package tfcgames.guessthedrink;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by e2-User on 13.05.2015.
 */
public class GameActivity extends MainActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final Button btnA = (Button) findViewById(R.id.btnA);
        final Button btnB = (Button) findViewById(R.id.btnB);
        final Button btnC = (Button) findViewById(R.id.btnC);
        final Button btnD = (Button) findViewById(R.id.btnD);
        final Button btnOK = (Button) findViewById(R.id.btnOK);
        final TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        final TextView txtScore = (TextView) findViewById(R.id.txtScore);
        final TextView txtScoreView = (TextView) findViewById(R.id.txtScoreView);
        final ImageView imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        final ImageButton imgBtnBackGame = (ImageButton) findViewById(R.id.imgBtnBackGame);

        btnA.setVisibility(View.VISIBLE);
        btnB.setVisibility(View.VISIBLE);
        btnC.setVisibility(View.VISIBLE);
        btnD.setVisibility(View.VISIBLE);
        btnOK.setVisibility(View.INVISIBLE);
        txtInfo.setVisibility(View.INVISIBLE);
        txtScore.setVisibility(View.VISIBLE);
        txtScoreView.setVisibility(View.VISIBLE);
        imgPhoto.setVisibility(View.VISIBLE);

        startLvl();
        //randomizePic();
        txtScore.setText("0");

        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatch(0);
            }
        });

        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatch(1);
            }
        });

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatch(2);
            }
        });

        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMatch(3);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
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

    //обработка нажатия кнопки BACK
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameActivity.this, SelectLvlActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
}
