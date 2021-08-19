package com.brighteststar.asiftamal.recipeapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import static com.brighteststar.asiftamal.recipeapp.RecipeDetailsActivity.mtxtCookingStepsDetails;
import static com.brighteststar.asiftamal.recipeapp.RecipeDetailsActivity.mtxtIngredientDetails;
import static com.brighteststar.asiftamal.recipeapp.RecipeDetailsActivity.mtxtSpecialInstructionDetails;

public class textZoom  extends View{
    public static final int TEXT_MAX_SIZE = 140;
    public static final int TEXT_MIN_SIZE = 40;
    private static final int STEP = 4;
Context context;
    private int mBaseDistZoomIn;
    private int mBaseDistZoomOut;


    private static final int STROKE_WIDTH = 1;
    private static final int CIRCLE_RADIUS = 20;

    private ArrayList<PointF> touchPoints = null;
    private Paint drawingPaint = null;
    private boolean isMultiTouch = false;
    private int pathEffectPhase = 0;

    public textZoom(Context context) {
        super(context);

        initialize(context);
    }

    public textZoom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initialize(context);
    }

    public textZoom(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(touchPoints.size() > 0)
        {
            DashPathEffect effect = new DashPathEffect(new float[] {7,7}, pathEffectPhase);
            PointF midpt = null;

            drawingPaint.setPathEffect(effect);

            for(int index=1; index<touchPoints.size(); ++index)
            {
                midpt = getMidPoint(
                        touchPoints.get(index - 1).x,touchPoints.get(index - 1).y,
                        touchPoints.get(index).x,touchPoints.get(index).y);

                canvas.drawCircle(
                        touchPoints.get(index - 1).x,touchPoints.get(index - 1).y,
                        1, drawingPaint);
                canvas.drawCircle(
                        touchPoints.get(index - 1).x,touchPoints.get(index - 1).y,
                        CIRCLE_RADIUS, drawingPaint);

                canvas.drawCircle(touchPoints.get(index).x,touchPoints.get(index).y,
                        1, drawingPaint);
                canvas.drawCircle(touchPoints.get(index).x,touchPoints.get(index).y,
                        CIRCLE_RADIUS, drawingPaint);

                canvas.drawLine(
                        touchPoints.get(index - 1).x,touchPoints.get(index - 1).y,
                        touchPoints.get(index).x,touchPoints.get(index).y,
                        drawingPaint);

                canvas.drawCircle(midpt.x,midpt.y, 10, drawingPaint);
            }

            ++pathEffectPhase;

            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int action = event.getAction() & MotionEvent.ACTION_MASK;

        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                invalidate();

                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:
            {
                isMultiTouch = true;

                setPoints(event);
                invalidate();

                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
            {
                isMultiTouch = false;

                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                if(isMultiTouch)
                {
                    setPoints(event);
                    invalidate();
                }

                break;
            }
        }

        return true;
    }

    private void initialize(Context context){
        drawingPaint = new Paint();

        drawingPaint.setColor(Color.RED);
        drawingPaint.setStrokeWidth(STROKE_WIDTH);
        drawingPaint.setStyle(Paint.Style.STROKE);
        drawingPaint.setAntiAlias(true);

        touchPoints = new ArrayList<PointF>();
    }

    public void setPoints(MotionEvent event){
        touchPoints.clear();

        int pointerIndex = 0;

        for(int index=0; index<event.getPointerCount(); ++index)
        {
            pointerIndex = event.getPointerId(index);

            touchPoints.add(new PointF(event.getX(pointerIndex),event.getY(pointerIndex)));
        }
    }

    private PointF getMidPoint(float x1, float y1, float x2, float y2) {
        PointF point = new PointF();

        float x = x1 + x2;
        float y = y1 + y2;

        point.set(x / 2, y / 2);

        return point;
    }


















  //  @Override
   /* public boolean onTouch(View v, MotionEvent event) {
       if(v.getId() == R.id.txtCookingStepsDetails){
           if (event.getPointerCount() == 2) {
               // TextView txtinstration = (TextView) findViewById(R.id.some_text_view);
               int action = event.getAction();
               int pure = action & MotionEvent.ACTION_MASK;

               if (pure == MotionEvent.ACTION_POINTER_DOWN
                       && mtxtCookingStepsDetails.getTextSize() <= TEXT_MAX_SIZE
                       && mtxtCookingStepsDetails.getTextSize() >= TEXT_MIN_SIZE) {

                   mBaseDistZoomIn = getDistanceFromEvent(event);
                   mBaseDistZoomOut = getDistanceFromEvent(event);

               } else {
                   int currentDistance = getDistanceFromEvent(event);
                   if (currentDistance > mBaseDistZoomIn) {
                       float finalSize = mtxtCookingStepsDetails.getTextSize() + STEP;
                       if (finalSize > TEXT_MAX_SIZE) {
                           finalSize = TEXT_MAX_SIZE;
                       }
                       mtxtCookingStepsDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, finalSize);
                   } else {
                       if (currentDistance < mBaseDistZoomOut) {
                           float finalSize = mtxtCookingStepsDetails.getTextSize() - STEP;
                           if (finalSize < TEXT_MIN_SIZE) {
                               finalSize = TEXT_MIN_SIZE;
                           }
                           mtxtCookingStepsDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, finalSize);
                       }
                   }
               }
               return true;
           }
       }
       else if(v.getId() == R.id.txtSpecialInstructionDetails){
           if (event.getPointerCount() == 2) {
               // TextView txtinstration = (TextView) findViewById(R.id.some_text_view);
               int action = event.getAction();
               int pure = action & MotionEvent.ACTION_MASK;

               if (pure == MotionEvent.ACTION_POINTER_DOWN
                       && mtxtSpecialInstructionDetails.getTextSize() <= TEXT_MAX_SIZE
                       && mtxtSpecialInstructionDetails.getTextSize() >= TEXT_MIN_SIZE) {

                   mBaseDistZoomIn = getDistanceFromEvent(event);
                   mBaseDistZoomOut = getDistanceFromEvent(event);

               } else {
                   int currentDistance = getDistanceFromEvent(event);
                   if (currentDistance > mBaseDistZoomIn) {
                       float finalSize = mtxtSpecialInstructionDetails.getTextSize() + STEP;
                       if (finalSize > TEXT_MAX_SIZE) {
                           finalSize = TEXT_MAX_SIZE;
                       }
                       mtxtSpecialInstructionDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, finalSize);
                   } else {
                       if (currentDistance < mBaseDistZoomOut) {
                           float finalSize = mtxtSpecialInstructionDetails.getTextSize() - STEP;
                           if (finalSize < TEXT_MIN_SIZE) {
                               finalSize = TEXT_MIN_SIZE;
                           }
                           mtxtSpecialInstructionDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, finalSize);
                       }
                   }
               }
               return true;
           }
       }
       else if(v.getId() == R.id.txtIngredientDetails){
           if (event.getPointerCount() == 2) {
               // TextView txtinstration = (TextView) findViewById(R.id.some_text_view);
               int action = event.getAction();
               int pure = action & MotionEvent.ACTION_MASK;

               if (pure == MotionEvent.ACTION_POINTER_DOWN
                       && mtxtIngredientDetails.getTextSize() <= TEXT_MAX_SIZE
                       && mtxtIngredientDetails.getTextSize() >= TEXT_MIN_SIZE) {

                   mBaseDistZoomIn = getDistanceFromEvent(event);
                   mBaseDistZoomOut = getDistanceFromEvent(event);

               } else {
                   int currentDistance = getDistanceFromEvent(event);
                   if (currentDistance > mBaseDistZoomIn) {
                       float finalSize = mtxtIngredientDetails.getTextSize() + STEP;
                       if (finalSize > TEXT_MAX_SIZE) {
                           finalSize = TEXT_MAX_SIZE;
                       }
                       mtxtIngredientDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, finalSize);
                   } else {
                       if (currentDistance < mBaseDistZoomOut) {
                           float finalSize = mtxtIngredientDetails.getTextSize() - STEP;
                           if (finalSize < TEXT_MIN_SIZE) {
                               finalSize = TEXT_MIN_SIZE;
                           }
                           mtxtIngredientDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, finalSize);
                       }
                   }
               }
               return true;
           }
       }

        return false;
    }


    // good function to get the distance between the multiple touch
    int getDistanceFromEvent(MotionEvent event) {
        int dx = (int) (event.getX(0) - event.getX(1));
        int dy = (int) (event.getY(0) - event.getY(1));
        return (int) (Math.sqrt(dx * dx + dy * dy));
    }*/


}
