package tfcgames.guessthedrink;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import tfcgames.guessthedrink.DataBaseOperation.DBHelper;
import tfcgames.guessthedrink.DataBaseOperation.DataBaseConnector;

/**
 * Created by e2-User on 14.05.2015.
 */
public class SelectLvlActivity extends MainActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectlvl);

        LinearLayout lnrLayout = (LinearLayout) findViewById(R.id.cntLevelList);
        LayoutInflater ltInflater = getLayoutInflater();

        final ImageButton imgBtnBackSelectlvl = (ImageButton) findViewById(R.id.imgBtnBackSelectlvl);

        imgBtnBackSelectlvl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLvlActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        try {
            DataBaseConnector dbConnector = new DataBaseConnector(this, dbHelper);
            dbConnector.open();

            Cursor cLevelList = dbConnector.getLevelList();

            if (cLevelList != null) {
                if (cLevelList.moveToFirst()) {
                    do {
                        View item = ltInflater.inflate(R.layout.item, lnrLayout, false);
                        TextView lvlName = (TextView) item.findViewById(R.id.lvlName);
                        lvlName.setText(cLevelList.getString(cLevelList.getColumnIndex("lvlName")));
                        Button btnStartLevel = (Button) item.findViewById(R.id.btnSelectLevel);
                        btnStartLevel.setTag(cLevelList.getInt(cLevelList.getColumnIndex("lvlId")));
                        btnStartLevel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int levelId = (Integer) v.getTag();
                                Intent intent = new Intent(SelectLvlActivity.this, GameActivity.class);
                                intent.putExtra("levelId", levelId);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                            }
                        });
                        lnrLayout.addView(item);
                    } while (cLevelList.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.d("GTD_LOG", e.getMessage().toString());
        }

    }

    //обработка нажатия кнопки BACK
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelectLvlActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
}
