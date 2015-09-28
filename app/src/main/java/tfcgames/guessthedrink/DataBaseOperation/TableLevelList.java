package tfcgames.guessthedrink.DataBaseOperation;

public class TableLevelList {
    public final static String FIELD_LEVEL_NAME = "lvlName"; // Field in table
    // Query for create
    public final static String CREATE_TABLE_LEVEL_LIST = "create table tblLvlList(lvlId integer primary key autoincrement, " +
                                                         "lvlName text);";
    public final static String TABLE_NAME = "tblLvlList"; // table name
}
