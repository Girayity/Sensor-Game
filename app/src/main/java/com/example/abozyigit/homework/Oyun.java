package com.example.abozyigit.homework;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Oyun extends AppCompatActivity implements SensorEventListener {
    SensorManager sm;
    Sensor sensorA;
    float[] engeller = null;
    float dx = 0, dy = 0;
    float figureX;
    float figureY;
    boolean zor;
    MyDraw md;
    boolean oyunoynaniyor = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        md = new MyDraw(this);
        setContentView(md);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorA = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, sensorA, SensorManager.SENSOR_DELAY_GAME);
        Intent intent = getIntent();
        zor = intent.getBooleanExtra("zorluk", true);
        //Database database= new Database(this);
        //database.insert(true, 12);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(oyunoynaniyor){
            float x, y, z;
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER: {
                    x = event.values[0];
                    z = event.values[2];

                    dx = x * -2;
                    if (z > 0) dy = z * -2;
                    else dy = 2;

                    md.invalidate();
                    break;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class MyDraw extends View {
        public static final float radius = 30;
        Context context;
        Paint p;
        Paint p2;
        Paint p3;
        Date time1;
        public MyDraw(Context context) {
            super(context);
            this.context = context;
            p = new Paint();
            p2 = new Paint();
            p3 = new Paint();
            p2.setColor(Color.BLUE);
            p3.setStrokeWidth(20);
            p3.setColor(Color.RED);
            time1 = Calendar.getInstance().getTime();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth();
            int h = getHeight();

            if (engeller == null) {
                figureX = w / 2;
                figureY = h;
                if (zor) {
                    engeller = new float[16];
                } else {
                    engeller = new float[8];
                }

                Random random = new Random();
                List<Float> yler = new ArrayList<>();
                int yuzdelik = h / 10;
                int sonyuzdelik = h - yuzdelik;

                //https://stackoverflow.com/a/21980517
                int prevValue = yuzdelik;
                int maxRange;
                for (int i = 0; i < engeller.length / 4; i++) {
                    maxRange = sonyuzdelik - (((engeller.length / 4 - 1) - i) * Math.round(radius * 2 + 5)) - prevValue;
                    int nextValue = random.nextInt(maxRange);
                    prevValue += nextValue;
                    yler.add((float) prevValue);
                    prevValue += radius * 2 + 5;
                }

                for (int i = 0; i < engeller.length; i += 4) {
                    if (random.nextBoolean()) {
                        float xSon = w / 2 - (random.nextFloat() * w / 10);
                        engeller[i] = xSon;
                        engeller[i + 2] = w;
                    } else {
                        engeller[i] = 0;
                        float xSon = w / 2 + (random.nextFloat() * w / 10);
                        engeller[i + 2] = xSon;
                    }
                    engeller[i + 1] = yler.get(i / 4);
                    engeller[i + 3] = yler.get(i / 4);
                }
            }

            if (figureX + dx + radius >= w) {
                figureX = w - radius;
            } else if (figureX - radius + dx <= 0) {
                figureX = radius;
            } else {
                figureX += dx;
            }

            if (figureY + dy + radius >= h) {
                figureY = h - radius;
            } else if (figureY - radius + dy <= 0) {
                figureY = radius;
            } else {
                figureY += dy;
            }

            for (int i = 0; i < engeller.length; i += 4) {
                float x1 = engeller[i];
                float y1 = engeller[i + 1];
                float x2 = engeller[i + 2];
                if (dy < 0 && Math.abs(figureY - y1) < radius && (figureX + radius > x1 && figureX - radius < x2) && figureY + radius > y1) {
                    figureY = engeller[i + 1] + radius;
                    dy = 0;
                } else if (dy > 0 && Math.abs(figureY - y1) < radius && (figureX + radius > x1 && figureX - radius < x2) && figureY - radius < y1) {
                    figureY = engeller[i + 1] - radius;
                    dy = 0;
                }
            }
            if(figureY == radius){
                oyunoynaniyor = false;
                Toast.makeText(context, "Oyunu Kazandınız!", Toast.LENGTH_LONG).show();
                Date time2 = Calendar.getInstance().getTime();
                long milisaniye = time2.getTime() - time1.getTime();
                long saniye = TimeUnit.MILLISECONDS.toSeconds(milisaniye);
                Database database= new Database(context);
                database.insert(zor, (int) saniye);
                finish();
            }
            canvas.drawCircle(figureX, figureY, radius, p);
            canvas.drawLines(engeller, p2);
            canvas.drawLine(0,0, w, 0, p3);
        }
    }
}