package edu.csulb.android.arttherapy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Hemant on 3/20/2017.
 */

public class CustomCanvasView extends View {

    private Paint paint = new Paint();
    private Path path = new Path();

    public CustomCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Canvas defines shapes while Paint defines color,style,font and so forth
        // Before we can draw anything with canvas object we first need a paint object
        // Call the init method where the paint object is created

        init();

    }

    private void init() {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);

        // Save the X and Y coordinates of the user touch
        float X = event.getX();
        float Y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Set starting point for the drawing
                path.moveTo(X, Y);
                return true;

            case MotionEvent.ACTION_MOVE:
                // Track the motion on the screen
                path.lineTo(X, Y);
                break;
        }

        // Invalidate the view to let system know that the view needs to be redrawn
        invalidate();
        return true;
    }

    public void reset() {
        path.reset();
        invalidate();
    }
}
