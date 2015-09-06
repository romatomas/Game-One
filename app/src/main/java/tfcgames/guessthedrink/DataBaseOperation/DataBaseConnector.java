package tfcgames.guessthedrink.DataBaseOperation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by asus_home on 15.08.2015.
 */
public class DataBaseConnector {
    private static final String DATABASE_NAME = "GTD_DATABASE";
    private static final int DATABASE_VERSION = 4;

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

    public Cursor getImgList() {
        return dbGTD.query(TableImageList.TABLE_NAME, null, null, null, null, null, null);
    }
}
