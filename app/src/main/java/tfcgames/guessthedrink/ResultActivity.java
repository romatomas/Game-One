package tfcgames.guessthedrink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends MainActivity{

    public Button btnOK;
    public TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        this.btnOK = (Button) findViewById(R.id.btnOK);
        this.txtInfo = (TextView) findViewById(R.id.txtInfo);

        Intent intent = getIntent();
        String score = intent.getStringExtra("score");
        txtInfo.setText(score);

        //press on OK button
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, SelectLvlActivity.class);
                startActivity(intent);
            }
        });
    }
}
