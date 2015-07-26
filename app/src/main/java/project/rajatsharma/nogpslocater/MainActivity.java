package project.rajatsharma.nogpslocater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    Button smap,scompass,acompass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smap = (Button)findViewById(R.id.smap);
        scompass = (Button)findViewById(R.id.scompass);
        acompass = (Button)findViewById(R.id.acompass);
        smap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MapsActivity.class);
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
    }
}
