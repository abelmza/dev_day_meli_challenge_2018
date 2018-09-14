package com.example.abmartin.desafio_dev_day;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TableLayout tableLayout;
    LinearLayout verticalLayout;

    ArrayList<Split> sortedSplits = new ArrayList<>((400));

    List<Split> splits;
    static int nullCounter = 0;
    static int retriesCounter = 0;

    HashMap<String, String> topHelp = new HashMap<String, String>() {{
        put("4-4.png", "7-5.png");
        put("5-1.png", "1-17.png");
        put("4-5.png", "7-18.png");
        put("19-19.png", "5-13.png");
        put("12-15.png", "19-17.png");
    }};

    HashMap<String, String> bottomHelp = new HashMap<String, String>() {{
        put("0-4.png", "2-14.png");
        put("0-5.png", "18-12.png");
        put("2-2.png", "2-11.png");
        put("2-8.png", "9-4.png");
        put("5-6.png", "11-12.png");
        put("6-3.png", "4-15.png");
        put("5-13.png", "19-19.png");
    }};

    HashMap<String, String> leftHelp = new HashMap<String, String>() {{
        put("1-2.png", "2-1.png");
        put("4-5.png", "3-10.png");
        put("7-9.png", "7-12.png");
        put("9-5.png", "16-15.png");
        put("1-10.png", "19-11.png");
        put("1-14.png", "15-0.png");
        put("16-3.png", "17-5.png");
        put("17-3.png", "17-18.png");
        put("19-9.png", "9-10.png");
        put("5-16.png", "7-18.png");
        put("7-15.png", "12-17.png");
        put("8-16.png", "19-14.png");
        put("12-11.png", "14-10.png");
        put("14-11.png", "9-16.png");
        put("14-15.png", "4-16.png");
        put("15-13.png", "4-12.png");
    }};

    HashMap<String, String> rightHelp = new HashMap<String, String>() {{
        put("1-1.png", "19-7.png");
        put("1-2.png", "12-15.png");
        put("1-7.png", "7-7.png");
        put("3-8.png", "8-3.png");
        put("7-6.png", "12-9.png");
        put("8-0.png", "17-15.png");
        put("9-3.png", "6-10.png");
        put("4-12.png", "15-13.png");
        put("18-1.png", "8-8.png");
        put("3-0.png", "19-17.png");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = findViewById(R.id.tableLayout);
        verticalLayout = findViewById(R.id.linearlayout);

        splits = readFiles();

        for (int i = 0; i < 10; i++) {
            retriesCounter = i;
            findSimilarColors();
            log("nullCounter: " + nullCounter);
            nullCounter = 0;
        }

        showImages();
    }

    private List<Split> readFiles() {

        String path = Environment.getExternalStorageDirectory() + "/splitted";
        File imagesDir = new File(path);

        File[] images = imagesDir.listFiles();

        final List<Split> result = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            final Split split = new Split(images[i]);

            result.add(split);
        }

        return result;
    }

    private void findSimilarColors() {
        for (int i = 0; i < splits.size(); i++) {
            Split split = splits.get(i);
            if (split.file.getName().startsWith("18-1.")) {
                log("aca!");
            }
            if (split.right == null) {
                split.right = findRightSlice(split);
                if (split.right != null && split.right.left == null) {
                    split.right.left = split;
                }
            }
            if (split.left == null) {
                split.left = findLeftSlice(split);
                if (split.left != null && split.left.right == null) {
                    split.left.right = split;
                }
            }
            if (split.top == null) {
                split.top = findTopSlice(split);
                if (split.top != null && split.top.bottom == null) {
                    split.top.bottom = split;
                }
            }
            if (split.bottom == null) {
                split.bottom = findBottomSlice(split);
                if (split.bottom != null && split.bottom.top == null) {
                    split.bottom.top = split;
                }
            }
        }
    }

    private Split findFirstSplit() {
        for (Split split : splits) {
            if (split.file.getName().startsWith("18-1.p")) {
                return split;
            }

            if (split.colorSI == null
                && split.colorII == null
                && split.colorSD == null) {
                return split;
            }
        }
        return null;
    }

    private Split findRightSlice(Split split) {
        List<Split> results = new ArrayList<>();
        for (Split current : splits) {
            if (colorsAreEqual(split.colorSD, current.colorSI) &&
                colorsAreEqual(split.colorID, current.colorII)) {
                results.add(current);
            }
        }
        if (results.size() == 1) {
            return results.get(0);
        } else {
            if (split.top != null &&
                split.top.right != null &&
                split.top.right.bottom != null &&
                results.contains(split.top.right.bottom)) {
                return split.top.right.bottom;
            } else if (split.bottom != null &&
                split.bottom.right != null &&
                split.bottom.right.top != null &&
                results.contains(split.bottom.right.top)) {
                return split.bottom.right.top;
            } else {
                if (rightHelp.containsKey(split.file.getName())) {
                    String fileName = rightHelp.get(split.file.getName());
                    for (Split result : results) {
                        if (result.file.getName().equals(fileName)) {
                            return result;
                        }
                    }
                }
                if (split.colorSD != null || split.colorID != null) {
                    nullCounter++;
                }
                return null;
            }
        }
    }

    private Split findLeftSlice(Split split) {
        List<Split> results = new ArrayList<>();
        for (Split current : splits) {
            if (colorsAreEqual(split.colorSI, current.colorSD)
                && colorsAreEqual(split.colorII, current.colorID)) {
                results.add(current);
            }
        }
        if (results.size() == 1) {
            return results.get(0);
        } else {
            if (split.top != null &&
                split.top.left != null &&
                split.top.left.bottom != null &&
                results.contains(split.top.left.bottom)) {
                return split.top.left.bottom;
            } else if (split.bottom != null &&
                split.bottom.left != null &&
                split.bottom.left.top != null &&
                results.contains(split.bottom.left.top)) {
                return split.bottom.left.top;
            } else {
                if (leftHelp.containsKey(split.file.getName())) {
                    String fileName = leftHelp.get(split.file.getName());
                    for (Split result : results) {
                        if (result.file.getName().equals(fileName)) {
                            return result;
                        }
                    }
                }
                if (split.colorSI != null || split.colorII != null) {
                    nullCounter++;
                }
                return null;
            }
        }
    }

    private Split findTopSlice(Split split) {
        List<Split> results = new ArrayList<>();
        for (Split current : splits) {
            if (colorsAreEqual(split.colorSI, current.colorII)
                && colorsAreEqual(split.colorSD, current.colorID)) {
                results.add(current);
            }
        }
        if (results.size() == 1) {
            return results.get(0);
        } else {
            if (split.left != null &&
                split.left.top != null &&
                split.left.top.right != null &&
                results.contains(split.left.top.right)) {
                return split.left.top.right;
            } else if (split.right != null &&
                split.right.top != null &&
                split.right.top.left != null &&
                results.contains(split.right.top.left)) {
                return split.right.top.left;
            } else {
                if (topHelp.containsKey(split.file.getName())) {
                    String fileName = topHelp.get(split.file.getName());
                    for (Split result : results) {
                        if (result.file.getName().equals(fileName)) {
                            return result;
                        }
                    }
                }
                if (split.colorSI != null || split.colorSD != null) {
                    nullCounter++;
                }
                return null;
            }
        }
    }

    private Split findBottomSlice(Split split) {
        List<Split> results = new ArrayList<>();
        for (Split current : splits) {
            if (colorsAreEqual(split.colorII, current.colorSI)
                && colorsAreEqual(split.colorID, current.colorSD)) {
                results.add(current);
            }
        }
        if (results.size() == 1) {
            return results.get(0);
        } else {
            if (split.left != null &&
                split.left.bottom != null &&
                split.left.bottom.right != null &&
                results.contains(split.left.bottom.right)) {
                return split.left.bottom.right;
            } else if (split.right != null &&
                split.right.bottom != null &&
                split.right.bottom.left != null &&
                results.contains(split.right.bottom.left)) {
                return split.right.bottom.left;
            } else {
                if (bottomHelp.containsKey(split.file.getName())) {
                    String fileName = bottomHelp.get(split.file.getName());
                    for (Split result : results) {
                        if (result.file.getName().equals(fileName)) {
                            return result;
                        }
                    }
                }
                if (split.colorID != null || split.colorII != null) {
                    nullCounter++;
                }
                return null;
            }
        }
    }

    private boolean colorsAreEqual(String color1, String color2) {
        if (color1 == null && color2 == null) {
            return true;
        } else if (color1 != null && color1.equals(color2)) {
            return true;
        }
        return false;
    }

    private static void log(String msg) {
        Log.d("asdasd", msg);
    }

    /*private void sortLatter() {
        sortedSplits.add(getIndexFromCoordinates(0,0), findFirstSplit());

        for (int x = 0; x < 20; x++) {
            for (int y = x; y >= 0; y--) {
                int currentIndex = getIndexFromCoordinates(x, y);
                if (sortedSplits.size() > currentIndex &&
                    sortedSplits.get(currentIndex) != null) {

                } else {
                    sortedSplits.add(currentIndex, findSplitFor(x, y));
                }
            }
        }

    }

    private Split findSplitFor(int x, int y) {
        log("Finding split for: (" + x + "," + y + ")");
        Split top = null;
        Split left = null;
        if (y > 0) { //it is NOT the first row
            top = sortedSplits.get(getIndexFromCoordinates(x, y-1));
        }

        if (x > 0) { //it is NOT the first row
            left = sortedSplits.get(getIndexFromCoordinates(x-1, y));
        }

        for (Split current: splits) {
            if (top != null && left != null) {
                if (colorsAreEqual(top.colorII, current.colorSI) &&
                    colorsAreEqual(top.colorID, current.colorSD) &&
                    colorsAreEqual(left.colorID, current.colorII)) {
                    return current;
                }
            } else if (left == null) {
                if (colorsAreEqual(top.colorII, current.colorSI) &&
                    colorsAreEqual(top.colorID, current.colorSD) &&
                    colorsAreEqual(null, current.colorII)) {
                    return current;
                }
            } else if (top == null) {
                if (colorsAreEqual(left.colorSD, current.colorSI) &&
                    colorsAreEqual(null, current.colorSD) &&
                    colorsAreEqual(left.colorID, current.colorII)) {
                    return current;
                }
            }
        }
        return null;
    }

    private void sortSplits() {
        findTopRow();
    }

    private void findTopRow() {
        sortedSplits.add(getIndexFromCoordinates(0,0), findFirstSplit());
        Split leftOne = sortedSplits.get(0);
        for (int i = 1; i < 20; i++) {
            Split split = findTopSplitFrom(leftOne);
            sortedSplits.add(i, split);
            leftOne = split;
        }
    }

    private Split findTopSplitFrom(Split left) {
        for (Split split : splits) {
            if (split.colorSD == null &&
                split.colorSI == null &&
                left.colorID.equals(split.colorII)) {
                return split;
            }
        }
        return null;
    }

    private int getIndexFromCoordinates(int x, int y) {
        return y + y * 19 + x;
    }

    private void showImages() {

        for (int i = 0; i < 20; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(tableLayout.getLayoutParams());// TableLayout is the parent view
            tableLayout.addView(tableRow);
            for (int j = 0; j < 20; j++) {
                ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.image, null);
                int index = getIndexFromCoordinates(j, i);
                log("Drawing index: " + index + "(" + i + "," + j + ")");
                if (sortedSplits.size() > index) {
                    Split split = sortedSplits.get(index);
                    if (split != null) {
                        imageView.setImageBitmap(split.bitmap);
                    }
                }
                tableRow.addView(imageView);
            }
        }
    }*/

    /*private void showImages() {
        Split splitToDraw = findFirstSplit();
        Split bottom;

        int i = 0, j = 0;
        while (splitToDraw != null) {
            bottom = splitToDraw.bottom;
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(tableLayout.getLayoutParams());// TableLayout is the parent view
            j = 0;
            while (splitToDraw != null) {
                ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.image, null);
                imageView.setImageBitmap(splitToDraw.bitmap);
                log("Drawing " + i + ", " + j + " --> " + splitToDraw.file.getName());
                splitToDraw = splitToDraw.right;
                tableRow.addView(imageView);
                j++;
            }
            ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.image, null);
            tableRow.addView(imageView);
            tableLayout.addView(tableRow);
            splitToDraw = bottom;
            i++;
        }
    }*/

    private void showImages() {
        Split splitToDraw = findFirstSplit();
        Split bottom;

        LinearLayout.LayoutParams
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

        int i = 0, j = 0;
        while (splitToDraw != null) {
            bottom = splitToDraw.bottom;

            final LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setLayoutParams(params);
            horizontalLayout.setPadding(0, getResources().getDimensionPixelSize(R.dimen.padding), 0,
                getResources().getDimensionPixelSize(R.dimen.padding));

            j = 0;
            while (splitToDraw != null) {
                ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.image, null);
                imageView.setImageBitmap(splitToDraw.bitmap);
                log("Drawing " + i + ", " + j + " --> " + splitToDraw.file.getName());
                splitToDraw = splitToDraw.right;
                horizontalLayout.addView(imageView);
                j++;
            }
            verticalLayout.addView(horizontalLayout);
            splitToDraw = bottom;
            i++;
        }
    }
}
