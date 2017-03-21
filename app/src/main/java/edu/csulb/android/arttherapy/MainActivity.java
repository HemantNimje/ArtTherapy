package edu.csulb.android.arttherapy;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private CustomCanvasView customCanvasView;
    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private Vibrator vibrator;
    private long lastUpdate;
    private int shakeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCanvasView = (CustomCanvasView) findViewById(R.id.custom_canvas_view);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            checkShake(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void checkShake(SensorEvent event){
        // Movement
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)/(SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();

        if (accelationSquareRoot >= 2){

            if (actualTime - lastUpdate < 200){
                return;
            }

            if ((actualTime - lastUpdate > 1000) && (++shakeCount>=3)){
                vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                customCanvasView.reset();

                shakeCount = 0;
                Toast.makeText(this,"clear",Toast.LENGTH_SHORT).show();
                playEraserSound eraserSound = new playEraserSound();
                eraserSound.execute();
                lastUpdate = actualTime;
            }

        }
    }

    private class playEraserSound extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.eraser);
            mediaPlayer.start();
            return null;
        }
    }
}
