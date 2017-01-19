package pam.ugd_x_yyyy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.aenterhy.toggleswitch.ToggleSwitchButton;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, MapboxMap.OnMapLongClickListener{

    private MapboxMap mMap; // Might be null if Google Play services APK is not available.

    private DrawGeoJSON drawJson;

    JSONParser _jsonParser;

    ArrayList<HashMap<String, String>> dataList;
    JSONArray datas = null;

    private ProgressBar loading;
    private EditText txtSearch;
    private Button btnSearch;
    private RelativeLayout btnTampil;
    private MapView mMapView;

    FloatingActionButton myLocation;

    ToggleSwitchButton toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mMapView = (MapView) findViewById(R.id.map);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnTampil = (RelativeLayout) findViewById(R.id.buttonTampil);
        loading = (ProgressBar)findViewById(R.id.profile_loading);

        myLocation = (FloatingActionButton) findViewById(R.id.mylocation);

        myLocation.setOnClickListener(this);

        _jsonParser = new JSONParser();

        mMapView.onCreate(savedInstanceState);

        btnTampil.setOnClickListener(this);

        mMapView.getMapAsync(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                connectAsyncTask_geocode conn = new connectAsyncTask_geocode(_jsonParser.makeURL_geocoding(txtSearch.getText().toString().replaceAll(" ", "%20")));
                conn.execute();
                Log.d("SETUPGEO", txtSearch.getText().toString());
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
                    }
        });

        toggle = (ToggleSwitchButton) findViewById(R.id.toggle);
        toggle.setOnTriggerListener(new ToggleSwitchButton.OnTriggerListener() {
            @Override
            public void toggledUp() {
                CameraPosition cameraPosition2 = new CameraPosition.Builder()
                        .zoom(mMap.getCameraPosition().zoom + 0.5f)
                        .tilt(20)  // set the camera's tilt
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2), 1500);
            }

            @Override
            public void toggledDown() {
                CameraPosition cameraPosition2 = new CameraPosition.Builder()
                        .zoom(mMap.getCameraPosition().zoom - 0.5f)
                        .tilt(20)  // set the camera's tilt
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2), 1500);
            }
        });
    }

    public void onMapReady(final MapboxMap mapboxMap) {
        this.mMap = mapboxMap;
        // Set map style
        mapboxMap.setStyleUrl(Style.DARK);
        mapboxMap.setOnMapLongClickListener(this);

        com.mapbox.mapboxsdk.camera.CameraPosition cameraPosition = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder()
                .target(new com.mapbox.mapboxsdk.geometry.LatLng(-7.7941754998553705, 110.37401503000262)) // set the camera's center position
                .zoom(11)// set the camera's zoom level
                .tilt(20)  // set the camera's tilt
                .build();

        mapboxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newCameraPosition(cameraPosition), 4000);
    }

    @Override
    public void onClick(View v) {
        if(v == btnTampil)
        {
            drawJson = new DrawGeoJSON();
            drawJson.execute();
        }
        else if(v == myLocation)
        {
            mMap.setMyLocationEnabled(true);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude())) // set the camera's center position
                    .zoom(14)// set the camera's zoom level
                    .tilt(20)  // set the camera's tilt
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000);

            IconFactory iconFactory = IconFactory.getInstance(getApplicationContext());
            Drawable iconDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.me);
            Icon icon = iconFactory.fromDrawable(iconDrawable);

            mMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                    .position(new com.mapbox.mapboxsdk.geometry.LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()))
                    .title("Posisi Saya")
                    .icon(icon));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void repositionMarker(String result) {
        try {
            final JSONObject json = new JSONObject(result);
            JSONArray resultArray = json.getJSONArray("results");
            JSONObject results = resultArray.getJSONObject(0);
            JSONObject geometry = results.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            String stringLat = location.getString("lat");
            String stringLon = location.getString("lng");
            LatLng newlatlng = new LatLng(Double.parseDouble(stringLat),
                    Double.parseDouble(stringLon));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(newlatlng) // set the camera's center position
                    .zoom(11)// set the camera's zoom level
                    .tilt(20)  // set the camera's tilt
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 4000);
            // Move the camera to that position
            mMap.setMyLocationEnabled(true);

            Toast.makeText(getApplicationContext(), "Reposition",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapLongClick(@NonNull com.mapbox.mapboxsdk.geometry.LatLng point) {
        mMap.setMyLocationEnabled(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude())) // set the camera's center position
                .zoom(14)// set the camera's zoom level
                .tilt(20)  // set the camera's tilt
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000);

        IconFactory iconFactory = IconFactory.getInstance(getApplicationContext());
        Drawable iconDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.me);
        Icon icon = iconFactory.fromDrawable(iconDrawable);

        mMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                .position(new com.mapbox.mapboxsdk.geometry.LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()))
                .title("Posisi Saya")
                .icon(icon));
    }

    private class connectAsyncTask_geocode extends AsyncTask<Void, Void, String> {

        String url;

        connectAsyncTask_geocode(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrlFind(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.setVisibility(View.GONE);
            if (result != null) {
                repositionMarker(result);
                Log.d("result geocode", result);
            }
        }
    }


    private class DrawGeoJSON extends AsyncTask<Void, Void, List<LatLng>> {
        int sukses = 0;

        DrawGeoJSON()
        {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<LatLng> doInBackground(Void... voids) {

            try {
                // Load GeoJSON file
                InputStream inputStream = getAssets().open("data.geojson");
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }

                inputStream.close();

                // Parse JSON
                JSONObject json = new JSONObject(sb.toString());

                if (json != null) {
                    sukses = json.getInt("message");

                    if (sukses == 1) {
                        dataList = new ArrayList<HashMap<String, String>>();
                        Log.d("Semua Data: ", json.toString());
                        datas = json.getJSONArray("result");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<LatLng> points) {
            super.onPostExecute(points);
            try {
                if (sukses == 1) {
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject c = datas.getJSONObject(i);

                        String lat = c.getString("latitude");    //ambil data dari geojson seperti pada modul 7
                        String lng = c.getString("longitude");    // file geojson ada pada assets -> data.geojson
                        LatLng pos = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));

                        IconFactory iconFactory = IconFactory.getInstance(getApplicationContext());
                        Drawable iconDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.music);
                        Icon icon = iconFactory.fromDrawable(iconDrawable);
                        mMap.addMarker(new MarkerOptions()
                                .title("Walmart Store")
                                .position(pos)
                                .icon(icon));
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
