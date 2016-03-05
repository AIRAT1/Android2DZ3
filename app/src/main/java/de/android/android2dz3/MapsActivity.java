package de.android.android2dz3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

public class MapsActivity extends FragmentActivity {
    public static final String LOG = "LOG";
    private final int START_MAP_STATE_VALUE = 1;
    private Button btnTest;
    private Map<String, LatLng> citiesMap = new WeakHashMap<>();
    SupportMapFragment mapFragment;
    GoogleMap map;
    int mapState;
    private String value;
    private float[] colors;
    private String selectedKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLocale();
        setContentView(R.layout.activity_maps);
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapState++;
                setMapType();
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();

        if (savedInstanceState != null) {
            mapState = savedInstanceState.getInt("mapState");
        }else {
            mapState = START_MAP_STATE_VALUE;
        }
        setMapType();

        if (map == null) {
            finish();
            return;
        }
        mapSettings();
        listInit();
        colors = new float[] {BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_AZURE,
                              BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_CYAN,
                              BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_MAGENTA,
                              BitmapDescriptorFactory.HUE_ORANGE, BitmapDescriptorFactory.HUE_ROSE,
                              BitmapDescriptorFactory.HUE_VIOLET, BitmapDescriptorFactory.HUE_YELLOW};
        mapInit();
    }

    private void listInit() {
        citiesMap.put("Paris", new LatLng(48.87146, 2.35500));
        citiesMap.put("Lisboa", new LatLng(38.70908933, -9.1557312));
        citiesMap.put("Madrid", new LatLng(40.38839687, -3.70513916));
        citiesMap.put("Roma", new LatLng(41.88183137, 12.48939514));
        citiesMap.put("London", new LatLng(51.4813829, -0.12359619));
        citiesMap.put("Berlin", new LatLng(52.50869894, 13.40675354));
        citiesMap.put("Warszawa", new LatLng(52.21938689, 21.0017395));
        citiesMap.put("Kiev", new LatLng(50.40151532, 30.51452637));
        citiesMap.put("Athens", new LatLng(37.95719224, 23.73321533));
        citiesMap.put("Istanbul", new LatLng(41.00684811, 28.98090363));
        citiesMap.put("Dublin", new LatLng(53.32759238, -6.26495361));
        citiesMap.put("Oslo", new LatLng(59.89169258, 10.75836182));
        citiesMap.put("Stockholm", new LatLng(59.31076796, 18.06976318));
        citiesMap.put("Helsinki", new LatLng(60.15244221, 24.93621826));
        citiesMap.put("Wien", new LatLng(48.18440113, 16.37786865));
        citiesMap.put("Bucuresti", new LatLng(44.41024041, 26.09527588));
        citiesMap.put("Rejkjavik", new LatLng(64.11300063, -21.81610107));
    }

    private void mapInit() {
        for (Map.Entry<String, LatLng> entry : citiesMap.entrySet()) {
            map.addMarker(new MarkerOptions()
                            .position(entry.getValue())
                            .title(entry.getKey())
                            .icon(BitmapDescriptorFactory.defaultMarker(
                                    colors[new Random().nextInt(9)]
                            ))
//            ).showInfoWindow();
            );
            value = entry.getKey();
            Log.d(LOG, entry.getKey());
            Log.d(LOG, String.valueOf(entry.getValue()));
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals("Paris")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://de.wikipedia.org/wiki/Paris")));
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }

    private void changeLocale() {
        String languageToLoad = "de_DE";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("mapState", mapState);
        super.onSaveInstanceState(outState);
    }
    private void mapSettings() {
        UiSettings settings = map.getUiSettings();
        settings.setAllGesturesEnabled(true);
        settings.setCompassEnabled(true);
        map.setMyLocationEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        settings.setRotateGesturesEnabled(true);
        settings.setZoomControlsEnabled(true);
        settings.setZoomGesturesEnabled(true);
    }
    public void setMapType() {
        if (mapState > 3) {
            mapState = 1;
        }
        switch (mapState) {
            case 1:
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 2:
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case 3:
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            default:
                break;
        }
    }

    public void onChangeLandscape(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Landscape")
                .setMessage("Enter integer value between 2 and 20");
        final EditText editText = new EditText(MapsActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        editText.setEms(2);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(editText);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        })
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    float landscapeValue = Float.parseFloat(editText.getText().toString());
                    if (landscapeValue >= 2 && landscapeValue < 21) {
                        map.moveCamera(CameraUpdateFactory.zoomTo(landscapeValue));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
         }).create().show();
    }

    public void onMoveNext(View view) {
        List<LatLng> coordinates = new ArrayList(citiesMap.size());
        for (LatLng value : citiesMap.values()) {
            coordinates.add(value);
        }
        int selectedValue = new Random().nextInt(citiesMap.size());

        for (Map.Entry<String, LatLng> map : citiesMap.entrySet()) {
            if (map.getValue().equals(coordinates.get(selectedValue))) {
                selectedKey = map.getKey();
            }
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinates.get(
               selectedValue ), 5f);
        map.animateCamera(cameraUpdate, 2000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Toast.makeText(MapsActivity.this, "We are now in " + selectedKey, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {
                Toast.makeText(MapsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
