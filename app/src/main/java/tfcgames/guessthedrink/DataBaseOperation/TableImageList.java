package tfcgames.guessthedrink.DataBaseOperation;

/**
 * Created by asus_home on 25.08.2015.
 */
public class TableImageList {
    public final static String CREATE_TABLE_IMAGE_LIST = "create table tblImgList(imgId integer primary key autoincrement, " +
            "imgCaption text, complexity integer, lvlId integer);";
    public final static String TABLE_NAME = "tblImgList"; // table name
}
