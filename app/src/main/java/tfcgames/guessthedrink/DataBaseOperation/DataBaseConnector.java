package tfcgames.guessthedrink.DataBaseOperation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseConnector {
    private static final String DATABASE_NAME = "GTD_DATABASE";
    private static final int DATABASE_VERSION = 9;

    private final Context currentContext;
    private DBHelper dbHelper;
    private SQLiteDatabase dbGTD;

    public DataBaseConnector(Context currentContext, DBHelper dbHelper) {
        this.currentContext = currentContext;
        this.dbHelper = dbHelper;
    }

    // Open connection
    public void open() {
        dbHelper = new DBHelper(currentContext, DATABASE_NAME, null, DATABASE_VERSION);
        dbGTD = dbHelper.getWritableDatabase();
    }

    // Close connection
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public Cursor getLevelList() {
        return dbGTD.query(TableLevelList.TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getImgList(Integer lvlId) {
        String[] selectionArgs = {lvlId.toString()};
        return dbGTD.query(TableImageList.TABLE_NAME, null, "lvlId = ? AND complexity <> 0", selectionArgs, null, null, null);
    }

    public Cursor getImageFromNotThisLevel(Integer lvlId, Integer imageId) {
        String[] selectionArgs = {lvlId.toString(), imageId.toString()};
        return dbGTD.query(TableImageList.TABLE_NAME, null, "lvlId <> ? AND imgId = ? AND complexity <> 0", selectionArgs, null, null, null);
    }

    public Cursor getImageComplexity(Integer lvlId, Integer imageId) {
        String[] selectionArgs = {lvlId.toString(), imageId.toString()};
        return dbGTD.query(TableImageList.TABLE_NAME, null, "lvlId = ? AND imgId = ? AND complexity = 0", selectionArgs, null, null, null);
    }
}
