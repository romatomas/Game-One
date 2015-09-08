package tfcgames.guessthedrink.Entity;

/**
 * Created by asus_home on 08.09.2015.
 */
public class Frame {
    private String picture;
    private int complexity;
    private String[] falseImageList;
    private int levelId;

    public Frame(String picture, int complexity, int levelId) {
        this.picture = picture;
        this.complexity = complexity;
        this.levelId = levelId;
    }

    //Fill false picture list
    public void fillFalseImageList() {
        
    }
}
