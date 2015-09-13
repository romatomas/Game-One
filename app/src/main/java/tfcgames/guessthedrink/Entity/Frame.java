package tfcgames.guessthedrink.Entity;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Random;

import tfcgames.guessthedrink.DataBaseOperation.DataBaseConnector;

/**
 * Created by asus_home on 08.09.2015.
 */
public class Frame {
    public String getPicture() {
        return picture;
    }

    private String picture;
    private int complexity;

    public ArrayList<String> getFalseImageList() {
        return falseImageList;
    }

    private ArrayList<String> falseImageList;
    public static ArrayList<Integer> falseImageListId;
    private int levelId;
    private DataBaseConnector dbConnector;

    public Frame(String picture, int complexity, int levelId, DataBaseConnector dbConnector) {
        this.picture = picture;
        this.complexity = complexity;
        this.levelId = levelId;
        this.dbConnector = dbConnector;
        falseImageList = new ArrayList<String>();
        falseImageListId = new ArrayList<Integer>();
    }

    //Fill false picture list
    public void fillFalseImageList() {
        switch (complexity) {
            case 1: // 3 простых картинки с нетекущего уровня
                getSimpleImage(4);
                break;
            case 2: // 2 простыъ картинки с нетекущего уровня + 1 сложный вариант с текущего уровня
                getSimpleImage(4);
                break;
            case 3: // 1 простая картинка с нетекущего уровня + 2 сложных варианта с текущего уровня
                getSimpleImage(4);
                break;
        }
    }

    private Integer getRandomImageId() {
        Random rnd = new Random(System.currentTimeMillis());
        int maxImageId = 45; // через базу получать
        int minImageId = 1;
        Boolean isFine = false;
        int randomImageId = 0;
        while (!isFine) {
            randomImageId = minImageId + rnd.nextInt(maxImageId);
            Cursor img = dbConnector.getImageFromNotThisLevel(levelId, randomImageId);
            if (img != null) {
                if (img.moveToFirst()){
                    do {
                        if (!falseImageListId.contains(randomImageId)) {
                            falseImageListId.add(randomImageId);
                            isFine = true;
                        }
                    } while (img.moveToNext());
                }
            }
        }
        return randomImageId;
    }

    private void getSimpleImage(int count) {
        for (int i = 0; i < count; i++) {
            int randomImageId = getRandomImageId();
            Cursor img = dbConnector.getImageFromNotThisLevel(levelId, randomImageId);
            if (img != null) {
                if (img.moveToFirst()){
                    do {
                        falseImageList.add(img.getString(img.getColumnIndex("imgCaption")));
                    } while (img.moveToNext());
                }
            }
        }
    }
}
