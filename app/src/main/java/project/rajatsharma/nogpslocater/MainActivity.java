package project.rajatsharma.nogpslocater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    Button smap,scompass,acompass,citb,trackb,dbb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        citb = (Button)findViewById(R.id.citb);
        smap = (Button)findViewById(R.id.smap);
        scompass = (Button)findViewById(R.id.scompass);
        acompass = (Button)findViewById(R.id.acompass);
        trackb = (Button)findViewById(R.id.trackb);
        dbb = (Button)findViewById(R.id.dbb);
        smap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CellidMap.class);
                startActivity(i);
            }
        });
        scompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CompassActivity.class);
                startActivity(i);
            }
        });
        acompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MagnetoActivity.class);
                startActivity(i);
            }
        });
        citb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CellIdActivity.class);
                startActivity(i);
            }
        });
        dbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,GPSdb.class);
                startActivity(i);
            }
        });
        trackb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TrackingActivity.class);
                startActivity(i);
            }
        });
    }
}
