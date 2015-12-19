package tfcgames.guessthedrink.Entity;

import android.database.Cursor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tfcgames.guessthedrink.DataBaseOperation.DataBaseConnector;

public class Frame {
    public String getPicture() {
        return picture;
    }

    private String picture;
    private int complexity;

    public ArrayList<String> getFalseImageList() {
        return falseImageList;
    }

    public ArrayList<String> getComplexityFalseImageList() {
        return complexityImageList;
    }

    private ArrayList<String> falseImageList;
    public static ArrayList<Integer> falseImageListId;
    private ArrayList<String> complexityImageList;
    private static ArrayList<Integer> complexityImageListId;
    private int levelId;
    private DataBaseConnector dbConnector;

    public Frame(String picture, int complexity, int levelId, DataBaseConnector dbConnector) {
        this.picture = picture;
        this.complexity = complexity;
        this.levelId = levelId;
        this.dbConnector = dbConnector;
        falseImageList = new ArrayList<String>();
        falseImageListId = new ArrayList<Integer>();
        complexityImageList = new ArrayList<String>();
        complexityImageListId = new ArrayList<Integer>();
    }

    //Fill false picture list
    public void fillFalseImageList() {
        switch (complexity) {
            case 1: // 3 simple picture not from current level + 0 complexity pic from current level
                getItem(4,0);
                break;
            case 2: // 2 simple picture not from current level + 1 complexity pic from current level
                getItem(4,1);
                break;
            case 3: // 1 simple picture not from current level + 2 complexity pic from current level
                getItem(4,2);
                break;
        }
    }

    private Integer getRandomImageId() {
        Random rnd = new Random(System.currentTimeMillis());
        int maxImageId = 50; // через базу получать
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

    private  Integer getRandomComplexityImageId() {
        Random rnd = new Random(System.currentTimeMillis());
        int maxComplexityImageId = 50;
        int minComplexityImageId = 1;
        Boolean isFine = false;
        int randomComplexityImageId = 0;
        while (!isFine) {
            randomComplexityImageId = minComplexityImageId + rnd.nextInt(maxComplexityImageId);
            Cursor img = dbConnector.getImageComplexity(levelId, randomComplexityImageId);
            if (img != null) {
                if (img.moveToFirst()){
                    do {
                        if (!complexityImageListId.contains(randomComplexityImageId)) {
                            complexityImageListId.add(randomComplexityImageId);
                            isFine = true;
                        }
                    } while (img.moveToNext());
                }
            }
        }
        return randomComplexityImageId;
    }

    private void getItem(int countSimple, int countComplexity) {
        //generate array with simple false image list
        for (int i = 0; i < countSimple; i++) {
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
        //generate array with complexity false image list
        for (int i = 0; i < countComplexity; i++){
            int randomComplexityImageId = getRandomComplexityImageId();
            Cursor img = dbConnector.getImageComplexity(levelId, randomComplexityImageId);
            if (img != null) {
                if (img.moveToFirst()){
                    do {
                        complexityImageList.add(img.getString(img.getColumnIndex("imgCaption")));
                    } while (img.moveToNext());
                }
            }
        }
        //generate final array with false image
        Random r = new Random();
        for (int i = 0; i < countComplexity; i++) {
            String[] falseImageListArray = falseImageList.toArray(new String[falseImageList.size()]);
            String[] complexityImageListArray = complexityImageList.toArray(new String[complexityImageList.size()]);
            int rnd = r.nextInt(3);
            if (!falseImageListArray[rnd].equals(complexityImageListArray[i])) {
                falseImageListArray[rnd] = complexityImageListArray[i];
                falseImageList = new ArrayList<String>(Arrays.asList(falseImageListArray));
            }
        }
    }
}
