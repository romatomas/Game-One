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
        switch (complexity) {
            case 1: // 3 простых картинки с нетекущего уровня
                break;
            case 2: // 2 простыъ картинки с нетекущего уровня + 1 сложный вариант с текущего уровня
                break;
            case 3: // 1 простая картинка с нетекущего уровня + 2 сложных варианта с текущего уровня
                break;
        }
    }
}
