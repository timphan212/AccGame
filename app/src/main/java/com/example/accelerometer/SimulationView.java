package com.example.accelerometer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Timothy on 10/16/2015.
 */
public class SimulationView extends View implements SensorEventListener {
    private Bitmap mField;
    private Bitmap mBasket;
    private Bitmap mBitmap;
    private static final int BALL_SIZE = 64;
    private static final int BASKET_SIZE = 80;
    private float mXOrigin;
    private float MYOrigin;
    private float mHorizontalBound;
    private float mVerticalBound;
    private Display mDisplay;

    public SimulationView(Context context) {
        super(context);
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mBitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);
        Bitmap basket = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        mBasket = Bitmap.createScaledBitmap(basket, BASKET_SIZE, BASKET_SIZE, true);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        mField = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);

        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
