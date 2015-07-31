package project.rajatsharma.nogpslocater;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class TrackingActivity extends FragmentActivity implements SensorEventListener{

    TextView statusText;
    Float degree,azimuth;
    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    Sensor accelerometer, magnetometer;
    DBHelper mydb;
    String lng,lat;
    Integer cid;
    private GoogleMap mMap;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    float[] mGravity;
    float[] mGeomagnetic;


    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = orientation[0];
                degree = azimuth *360/(2*3.14159f);
                rotate(degree);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mydb = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        image = (ImageView) findViewById(R.id.imageView);
        statusText = (TextView) findViewById(R.id.textView3);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
        String networkOperator = telephonyManager.getNetworkOperator();
        String mcc = networkOperator.substring(0, 3);
        String mnc = networkOperator.substring(3);
        cid = cellLocation.getCid();
        statusText.setText("Cell Id Acquired,Waiting for Lat Lng........");
        int lac = cellLocation.getLac();
        String json = "{\n" +
                "\t\"radioType\": \"gsm\",\n" +
                "\t\"cellTowers\": [{\n" +
                "\t\t\"mobileCountryCode\": "+mcc+",\n" +
                "\t\t\"mobileNetworkCode\": "+mnc+",\n" +
                "\t\t\"locationAreaCode\": "+lac+",\n" +
                "\t\t\"cellId\": "+cid+"\n" +
                "\t}]\n" +
                "}";
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(json);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void rotate(float degree){
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setDuration(210);
        ra.setFillAfter(true);

        image.startAnimation(ra);
        currentDegree = -degree;
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).title("Marker"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 12.0f));
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            String gurl = "https://cps.combain.com?key=cj0vcpwz2ygth1kc8lae";
            RequestBody body = RequestBody.create(JSON, urls[0]);
            Request request = new Request.Builder()
                    .url(gurl)
                    .post(body)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "notsuccess";
        }
        protected void onPostExecute(String response){
            Log.d("rajat", "Got Result" + response);
            try {
                JSONObject json = new JSONObject(response);
                JSONObject location = json.getJSONObject("location");
                lng=location.getString("lng");
                lat=location.getString("lat");
                mydb.insertReading(lat, lng, degree.toString(), cid.toString());
                statusText.setText("Lat lng Acquired........");
                statusText.setText("Database Updated........");
                statusText.setText("Lat:"+lat+"     lng:"+lng+"     Cell Id:"+cid);
                setUpMapIfNeeded();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
