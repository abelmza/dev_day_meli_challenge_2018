package com.example.abmartin.desafio_dev_day;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdvanceCanvasView extends View {
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Bitmap mBitmapBrush;
    private ArrayList<Bitmap> bitmapArrayList;
    private Vector2 mBitmapBrushDimensions;
    private Paint paintLine;
    private List<Vector2> mPositions = new ArrayList<Vector2>(100);
    private HashMap<Integer, Path> pathMap; // current Paths being drawn
    private HashMap<Integer, Point> previousPointMap; // current Points
    private int i = 0;

    private static final class Vector2 {
        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public final float x;
        public final float y;
    }

    @SuppressLint("UseSparseArrays")
    public AdvanceCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs); // pass context to View's constructor
    }

    public AdvanceCanvasView(Context c) {
        super(c);
        pathMap = new HashMap<>();
        previousPointMap = new HashMap<>();
        bitmapArrayList = new ArrayList<>();
        paintLine = new Paint();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
            Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
        for (int i = 0; i < mPositions.size(); i++) {
            canvas.drawBitmap(bitmapArrayList.get(i), mPositions.get(i).x, mPositions.get(i).y, null);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int actionIndex = event.getActionIndex();

        if (action == MotionEvent.ACTION_DOWN
            || action == MotionEvent.ACTION_POINTER_DOWN) {
            touchStarted(event.getX(actionIndex), event.getY(actionIndex),
                event.getPointerId(actionIndex));
        }
        else if (action == MotionEvent.ACTION_UP
            || action == MotionEvent.ACTION_POINTER_UP) {
            touchEnded(event.getPointerId(actionIndex));
        }
        else {
            touchMoved(event);
        }

        invalidate();

        return true;
    }

    private void touchStarted(float x, float y, int lineID) {
        Path path;
        Point point;
        path = new Path(); // create a new Path
        pathMap.put(lineID, path); // add the Path to Map
        point = new Point(); // create a new Point
        previousPointMap.put(lineID, point); // add the Point to the Map
        path = new Path(); // create a new Path
        point = new Point(); // create a new Point
        path.moveTo(x, y);
        point.x = (int) x;
        point.y = (int) y;

    } // end method touchStarted

    private void touchMoved(MotionEvent event) {
        // for each of the pointers in the given MotionEvent
        for (int i = 0; i < event.getPointerCount(); i++) {
            final float posX = event.getX();
            final float posY = event.getY();
            mPositions.add(new Vector2(posX - mBitmapBrushDimensions.x / 2, posY - mBitmapBrushDimensions.y / 2));
            bitmapArrayList.add(mBitmapBrush);
        }
        invalidate();

    }

    private void touchEnded(int lineID) {
        Path path = pathMap.get(lineID);
        path.reset();
    }

    public void init(Bitmap bitmap) {
        mBitmapBrush = bitmap;
        BitmapShader shader = new BitmapShader(mBitmapBrush, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
        paintLine.setShader(shader);
        mBitmapBrushDimensions = new Vector2(mBitmapBrush.getWidth(), mBitmapBrush.getHeight());
    }

}
