package com.example.sabrina.hsvcolorpicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SelectionActivity extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selection);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selection, menu);
        return true;
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

    public void colorExplorerButtonHandler(View view){
        Intent intent = new Intent( this, ColorExplorerActivity.class );
        startActivity(intent);
    }

    public void identifyColorButtonHandler(View view){
        takePicture();
    }

    /* Color Explorer calculations */
    private void takePicture(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == ((ColorExplorerActivity.RESULT_OK))) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Map<List<Float>, Integer> hsvStats = new HashMap<>();

            int pixel;
            float[] components = new float[3];
            for (int i = 0; i < imageBitmap.getWidth(); ++i) {
                for (int j = 0; j < imageBitmap.getHeight(); ++j) {
                    pixel = imageBitmap.getPixel(i, j);
                    Color.colorToHSV(pixel, components);
                    List<Float> keySearch = Arrays.asList(components[0], components[1], components[2]);
                    if (hsvStats.containsKey(keySearch)) {
                        hsvStats.put(keySearch, hsvStats.get(keySearch) + 1);
                    } else {
                        hsvStats.put(keySearch, 1);
                    }
                }
            }

            int maxColorCount = 0;
            List<Float> maxColor = null;
            for (Iterator it = hsvStats.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry pair = (Map.Entry) it.next();
                if ((int) pair.getValue() > maxColorCount) {
                    maxColorCount = (int) pair.getValue();
                    maxColor = (List<Float>) pair.getKey();
                }
                it.remove(); // avoids a ConcurrentModificationException
            }
            float hue = Math.round(maxColor.get(0));
            HSVColor.setExactHue(hue);
            HSVColor.setHueLeft(hue - 10f < 0 ? 360f + hue - 10f : hue - 10f);
            HSVColor.setHueRight((hue + 10) % 360);
            HSVColor.setHueDelta(10);
            HSVColor.setSaturation(Math.round(maxColor.get(1)*100f)/100f);
            HSVColor.setSaturationDelta(10);
            HSVColor.setValue(Math.round(maxColor.get(2)*100f)/100f);
            HSVColor.setValueDelta(10);

            Intent intent = new Intent( this, IdentifyColorActivity.class );
            startActivity(intent);
        }
    }
}
