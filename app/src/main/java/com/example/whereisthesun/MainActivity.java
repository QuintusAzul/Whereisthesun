package com.example.whereisthesun;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.*;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];

    private TextView azimuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws SecurityException {

        final String date = "" + Calendar.getInstance().getTimeInMillis();
        final String d = Calendar.getInstance().getTime().toString();
        final Calendar calendar = Calendar.getInstance();
        final TimeZone timeZone = calendar.getTimeZone();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        final EditText userLongitude = findViewById(R.id.user_longitude);
        final EditText userLatitude = findViewById(R.id.user_latitude);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        final TextView dateText = findViewById(R.id.date);
        final TextView timeText = findViewById(R.id.time);
        final TextView timezone = findViewById(R.id.timezone);

        azimuth = findViewById(R.id.azimuthV);
        TextView pitch = findViewById(R.id.pitchV);
        TextView roll = findViewById(R.id.rollV);

        final TextView azi = findViewById(R.id.azimuth);
        final TextView ele = findViewById(R.id.elevation);

        setSupportActionBar(toolbar);
        final Context context = this.getApplicationContext();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button start = findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, ("Longitude " + userLongitude.getText().toString() + "  Latitude " + userLatitude.getText().toString()),
                        Snackbar.LENGTH_LONG).setAction("Action",null).show();
                double longitude = Double.parseDouble(userLongitude.getText().toString());
                double latitude = Double.parseDouble(userLatitude.getText().toString());
                PositionManager manager = new PositionManager(longitude,latitude);
                azi.setText("Azimuth: " + manager.azimuth);
                ele.setText("Elevation: " + manager.elevation);

                dateText.setText("Year: " + calendar.get(Calendar.YEAR) + " Month: " + (calendar.get(Calendar.MONTH) + 1) + " Day: " + calendar.get(Calendar.DATE));
                timeText.setText("" + calendar.getTimeInMillis());
                timezone.setText("" + timeZone.getID());

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
