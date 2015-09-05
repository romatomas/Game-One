package tfcgames.guessthedrink.DataBaseOperation;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

/**
 * Created by asus_home on 15.08.2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        String[] levelNames = new String[] {"Level 1",
                                            "Level 2",
                                            "Level 3",
                                            "Level 4",
                                            "Level 5",
                                            "Level 6",
                                            "Level 7",
                                            "Level 8",
                                            "Level 9",
                                            "Level 10",
                                            "Level 11",
                                            "Level 12",
                                            "Level 13",
                                            "Level 14"};
        db.execSQL(TableLevelList.CREATE_TABLE_LEVEL_LIST);
        for (int i = 0; i < levelNames.length; i++) {
            cv.put(TableLevelList.FIELD_LEVEL_NAME, levelNames[i]);
            db.insert(TableLevelList.TABLE_NAME, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ContentValues cv = new ContentValues();
        if (oldVersion == 1 && newVersion == 2 && newVersion == 3) {
            String imgNames1[] = new String[] {"BIRD IN HAND SHIRAZ",
                                               "BOMBAY SAPHIRE",
                                               "BRANDS LAIRA CAB MERLOT",
                                               "BREZZER JAMAICAN PASSION",
                                               "BUDWISER MAGNUM PINT"};
            String imgNames1_complexity[] = new String[] {"CAPE MENTELLE CABERNET MERLOT",
                                                          "CHAMPAGNE MOET & CHANDAN IMPERIAL",
                                                          "CHATEAU DU COURNEAU MEDOC",
                                                          "CHIAS REGAL 12YRS BLENDED SCOTCH WHISKY",
                                                          "CINZANO BIANCO"};
            String imgNames2[] = new String[] {"ALBINEA CANAL CODAROSSA",
                                               "AMRUT SINGLE MALT",
                                               "ANTIQUIETY WHISKY",
                                               "ARTIC GRN APLE VDKA",
                                               "BACARDI APPLE RUM"};
            String imgNames2_complexity[] = new String[] {"BACARDI WHITE RUM",
                                                          "BALLANTINES BLENDED SCOTCH WHISKY",
                                                          "BEEFEATER LONDON",
                                                          "Benziger chardonnay 750ml",
                                                          "Bertani valpolicella"};
            String imgNames3[] = new String[] {"3 TERRACES PINOT NOIR",
                                               "8 PM WHISKY",
                                               "35 SOUH CHARDONNAY  RESEVA SANPEDRO",
                                               "35 SOUTH CAB SAV RESR",
                                               "100 PIPERS 12 YRS BLENDED SCOTCH WHISKY"};
            String imgNames3_complexity[] = new String[] {"ABSINTHE TEICHNND",
                                                          "ABSOLUT APEACH",
                                                          "ABSOLUT CITRON",
                                                          "ABSOLUT KURANT",
                                                          "Albert bichot pouilly fuisse"};
            db.beginTransaction();
            try {
                db.execSQL(TableImageList.CREATE_TABLE_IMAGE_LIST);
                Random rnd = new Random(System.currentTimeMillis());
                String[] buf = null;
                String[] buf_complexity = null;
                for (int k = 1; k < 4; k++) {
                    if (k == 1){
                        buf = imgNames1;
                        buf_complexity = imgNames1_complexity;
                    } else if (k == 2) {
                        buf = imgNames2;
                        buf_complexity = imgNames2_complexity;
                    } else if (k == 3) {
                        buf = imgNames3;
                        buf_complexity = imgNames3_complexity;
                    }
                    for (int i = 0; i < buf.length; i++) {
                        int complexity = 1 + rnd.nextInt(3);
                        cv.clear();
                        cv.put("imgCaption", imgNames1[i]);
                        cv.put("complexity", complexity);
                        cv.put("lvlId", k);
                        db.insert(TableImageList.TABLE_NAME, null, cv);
                    }
                    for (int i = 0; i < buf_complexity.length; i++) {
                        cv.clear();
                        cv.put("imgCaption", imgNames1_complexity[i]);
                        cv.put("complexity", 0);
                        cv.put("lvlId", k);
                        db.insert(TableImageList.TABLE_NAME, null, cv);
                    }
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}
