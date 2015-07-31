package project.rajatsharma.nogpslocater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by rajat on 7/31/2015.
 */
public class DBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "noGPS.db";
    public static final String LOCATION_TABLE_NAME = "Readings";
    public static final String LOCATION_COLUMN_ID = "id";
    public static final String LOCATION_COLUMN_LAT = "Lat";
    public static final String LOCATION_COLUMN_LNG = "Lng";
    public static final String LOCATION_COLUMN_DEGREES = "Degrees";
    public static final String LOCATION_COLUMN_CELLID = "CellID";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Readings(id integer primary key,Lat text,Lng text,Degrees text, CellID text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Readings");
        onCreate(db);
    }
    public boolean insertReading(String Lat, String Lng, String Degrees, String CellID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Lat", Lat);
        contentValues.put("Lng", Lng);
        contentValues.put("Degrees", Degrees);
        contentValues.put("CellID", CellID);
        db.insert("Readings", null, contentValues);
        return true;
    }
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Readings where id="+id+"", null );
        return res;
    }
    public Cursor getLastLocation(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM Readings WHERE ID = (SELECT MAX(ID) FROM Readings);", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LOCATION_TABLE_NAME);
        return numRows;
    }
    public ArrayList<String> getAllCotacts()
    {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Readings", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add("Lat:"+res.getString(res.getColumnIndex(LOCATION_COLUMN_LAT))
                    +"\nLng:"+res.getString(res.getColumnIndex(LOCATION_COLUMN_LNG))
                    +"\nDegrees:"+res.getString(res.getColumnIndex(LOCATION_COLUMN_DEGREES))
                            +"\nCellId:"+res.getString(res.getColumnIndex(LOCATION_COLUMN_CELLID))
            );
            res.moveToNext();
        }
        return array_list;
    }
}
