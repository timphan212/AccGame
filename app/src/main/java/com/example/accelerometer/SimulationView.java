package com.example.accelerometer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Timothy on 10/16/2015.
 */
public class SimulationView extends View implements SensorEventListener {
    private Bitmap mField;
    private Bitmap mBasket;
    private Bitmap mBitmap;

    private static final int BALL_SIZE = 200;
    private static final int BASKET_SIZE = 480;

    private float mXOrigin;
    private float mYOrigin;

    private float mHorizontalBound;
    private float mVerticalBound;

    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private long mSensorTimeStamp;

    private Display mDisplay;
    private SensorManager sensorManager;
    private Particle mBall = new Particle();

    public SimulationView(Context context) {
        super(context);
        init();
    }

    public SimulationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mBitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);

        Bitmap basket = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        mBasket = Bitmap.createScaledBitmap(basket, BASKET_SIZE, BASKET_SIZE, true);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        mField = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);

        WindowManager mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        startSimulation();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            readAccelerometer(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void readAccelerometer(SensorEvent event) {
        mSensorX = event.values[0];
        mSensorY = event.values[1];
        mSensorZ = event.values[2];
        //mSensorTimeStamp = event.timestamp;
        mSensorTimeStamp = System.nanoTime();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if(mDisplay.getRotation() == Surface.ROTATION_0) {
                mSensorX = event.values[0];
                mSensorY = event.values[1];
            }
            else if(mDisplay.getRotation() == Surface.ROTATION_90) {
                mSensorX = -event.values[1];
                mSensorY = event.values[0];
            }
        }
    }

    public void startSimulation() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopSimulation() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mField, 0, 0, null);
        canvas.drawBitmap(mBasket, (mXOrigin + BASKET_SIZE) / 2, 0, null);
        mBall.updatePosition(mSensorX, mSensorY, mSensorZ, mSensorTimeStamp);
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);

        canvas.drawBitmap(mBitmap, (mXOrigin - BALL_SIZE / 2) + (mBall.mPosX),
                (mYOrigin - BALL_SIZE / 2) - (mBall.mPosY), null);

        invalidate();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mXOrigin = w * 0.5f;
        mYOrigin = h * 0.5f;

        mHorizontalBound = (w - BALL_SIZE) * 0.5f;
        mVerticalBound = (h - BALL_SIZE) * 0.5f;
    }
}
