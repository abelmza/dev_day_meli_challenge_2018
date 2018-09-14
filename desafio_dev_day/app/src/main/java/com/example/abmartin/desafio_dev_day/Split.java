package com.example.abmartin.desafio_dev_day;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import java.io.File;

public class Split {

    private final static String BLACK = "#000000";
    private final static String WHITE = "#FFFFFF";

    static final int distance = 10;
    File file;
    String colorSI, colorSD, colorII, colorID;
    Bitmap bitmap;

    boolean alreadyUsed = false;

    Split left = null;
    Split right = null;
    Split top = null;
    Split bottom = null;

    public Split(File file) {
        this.file = file;
        bitmap = BitmapFactory.decodeFile(file.getPath());
        colorSI = getStringColor(bitmap.getPixel(distance, distance));
        colorSD = getStringColor(bitmap.getPixel(bitmap.getWidth() - distance, distance));
        colorII = getStringColor(bitmap.getPixel(distance, bitmap.getHeight() - distance));
        colorID = getStringColor(bitmap.getPixel(bitmap.getWidth() - distance, bitmap.getHeight() - distance));
    }

    @Nullable
    public final String getStringColor(int pixel) {
        final String color = String.format("#%06X", (0xFFFFFF & pixel));
        if (color.equals(WHITE) || color.equals(BLACK)) {
            return null;
        }
        return color;
    }

    @Override
    public String toString() {
        return "name=" + file.getName();
    }
}
