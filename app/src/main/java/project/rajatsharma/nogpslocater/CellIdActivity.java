package project.rajatsharma.nogpslocater;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.TextView;

public class CellIdActivity extends Activity {
    TextView textGeo;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_id);
        TextView textGsmCellLocation = (TextView)findViewById(R.id.gsmcelllocation);
        textGeo = (TextView)findViewById(R.id.geo);
        TextView textMCC = (TextView)findViewById(R.id.mcc);
        TextView textMNC = (TextView)findViewById(R.id.mnc);
        TextView textCID = (TextView)findViewById(R.id.cid);
        TextView textLAC = (TextView)findViewById(R.id.lac);
        TextView textRemark = (TextView)findViewById(R.id.remark);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
        String networkOperator = telephonyManager.getNetworkOperator();
        String mcc = networkOperator.substring(0, 3);
        String mnc = networkOperator.substring(3);
        textMCC.setText("mcc: " + mcc);
        textMNC.setText("mnc: " + mnc);
        int cid = cellLocation.getCid();
        int lac = cellLocation.getLac();
        textGsmCellLocation.setText(cellLocation.toString());
        textCID.setText("gsm cell id: " + String.valueOf(cid));
        textLAC.setText("lac:"+ String.valueOf(lac));
        textLAC.setText("gsm location area code: " + String.valueOf(lac));
        textRemark.setText("All Data Received");
    }
}


