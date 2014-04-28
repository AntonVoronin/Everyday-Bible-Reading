package ru.itprospect.everydaybiblereading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ScrollView;
import android.widget.TextView;

public class ZoomingScrollView extends ScrollView {

	private int textSize = 18;
	private static final int MINIMAL_TEXT_SIZE = 12, MAXIMAL_TEXT_SIZE = 50;
	private static final float SPEED = 0.4f;
    private float minScale = MINIMAL_TEXT_SIZE / (float) textSize;
    private float maxScale = MAXIMAL_TEXT_SIZE / (float) textSize;
    
    private ScaleGestureDetector scaleDetector;
    private float scaleFactor;
    
    private TextView textView;
    private PrefManager prefManager;
	
	public ZoomingScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ZoomingScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ZoomingScrollView(Context context) {
		super(context);
		init(context);
	}
	
    private void init(Context context) {
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        scaleFactor = 1.0f;
        
        prefManager = new PrefManager(context);
    }
   
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
   
    public void setTextView(TextView textView) {
    	this.textView = textView;
    }
        
    public void setTextSize(int textSize) {
    	this.textSize = textSize;
    	minScale = MINIMAL_TEXT_SIZE / (float) textSize;
        maxScale = MAXIMAL_TEXT_SIZE / (float) textSize;
        scaleFactor = 1.0f;
        
        if (textView != null) {
        	textView.setTextSize(textSize);
        }
    }
    
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
 
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float curScaleFactor = (detector.getScaleFactor() - 1) * SPEED + 1;
            
        	scaleFactor *= curScaleFactor;
            scaleFactor = Math.max(minScale, Math.min(scaleFactor, maxScale));
 
            int newTextSize = (int) (scaleFactor * textSize);
            newTextSize = Math.max(MINIMAL_TEXT_SIZE, Math.min(newTextSize, MAXIMAL_TEXT_SIZE)); 
            
            if (textView != null) {
            	textView.setTextSize(newTextSize);
            }
            
            //if (! detector.isInProgress()) {
            	prefManager.putTextSize(newTextSize);
            //}
            
            return true;
        }
    }

}
