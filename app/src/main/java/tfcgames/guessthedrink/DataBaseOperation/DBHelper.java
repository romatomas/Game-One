package tfcgames.guessthedrink.DataBaseOperation;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        if (oldVersion == 1 && newVersion == 2) {
            String imgNames[] = new String[] {"bender", "bogdan", "gomer", "oleg", "roman", "stewie", "vadim"};
            db.beginTransaction();
            try {
                db.execSQL(TableImageList.CREATE_TABLE_IMAGE_LIST);
                for (int i = 0; i < imgNames.length; i++) {
                    cv.clear();
                    cv.put("imgCaption", imgNames[i]);
                    cv.put("complexity", 1);
                    cv.put("lvlId", 1);
                    db.insert(TableImageList.TABLE_NAME, null, cv);
                }
                for (int i = 0; i < imgNames.length; i++) {
                    cv.clear();
                    cv.put("imgCaption", imgNames[i]);
                    cv.put("complexity", 1);
                    cv.put("lvlId", 2);
                    db.insert(TableImageList.TABLE_NAME, null, cv);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}
