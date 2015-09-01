package com.example.sabrina.hsvcolorpicker.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sabrina.hsvcolorpicker.ColorCursorAdapter;
import com.example.sabrina.hsvcolorpicker.HSVColor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColorDBHelper extends SQLiteOpenHelper {

    private static String DATABASE_PATH = "/data/data/com.example.sabrina.hsvcolorpicker/databases/";
    private static final String DATABASE_NAME = "ColorDB.sqlite";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase mDatabase;
    private final Context myContext;

    public static final String SELECTION_AND = "Hue >= ? and Hue <= ? and Saturation >= ? and Saturation <= ? and Value >= ? and Value <= ?";
    public static final String SELECTION_OR = "Hue >= ? or Hue <= ? and Saturation >= ? and Saturation <= ? and Value >= ? and Value <= ?";
    public static final String SELECTION_EXACT = "Hue = ? and Saturation = ? and Value = ?";

    public ColorDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    public void createDatabase() throws IOException {

        boolean dbExist = checkDatabase();

        if(dbExist){
            // do nothing - database already exist
        }else{

            // By calling this method an empty database will be created into the default system path
            // of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDatabase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    private boolean checkDatabase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database doesn't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null;
    }

    private void copyDatabase() throws IOException{

        //Open local db as the input stream
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDatabase() throws SQLException {
        //Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
        mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public static String[] getSelectionArgs(){
        //TODO add the deltas in the query
        float hueL = HSVColor.getHueLeft();
        float hueR = HSVColor.getHueRight();
        float saturation = HSVColor.getSaturation();
        float value = HSVColor.getValue();

        float leftSaturation = saturation - .05f;
        float rightSaturation = saturation + .05f;
        float leftValue = value - .05f;
        float rightValue = value + .05f;

        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(Float.toString(hueL));
        selectionArgs.add(Float.toString(hueR));
        selectionArgs.add(Float.toString(leftSaturation));
        selectionArgs.add(Float.toString(rightSaturation));
        selectionArgs.add(Float.toString(leftValue));
        selectionArgs.add(Float.toString(rightValue));

        final String[] SELECTION_ARGS = new String[selectionArgs.size()];
        selectionArgs.toArray(SELECTION_ARGS);

        return SELECTION_ARGS;
    }

    public static String[] getExactSelectionArgs(){
        float hue = HSVColor.getExactHue();
        float saturation = HSVColor.getSaturation();
        float value = HSVColor.getValue();
         return new String[]{String.valueOf(hue), String.valueOf(saturation), String.valueOf(value)};
    }
    public static String getOrder(){
        return ColorCursorAdapter.currentOrder != -1 ? ColorCursorAdapter.ORDER_BY[ColorCursorAdapter.currentOrder] : null;
    }
    @Override
    public synchronized void close() {

        if(mDatabase != null)
            mDatabase.close();

        super.close();

    }

    @Override
    public void onCreate( SQLiteDatabase database ) { }

    @Override
    public void onUpgrade( SQLiteDatabase database,
                           int oldVersion,
                           int newVersion) { }
}

