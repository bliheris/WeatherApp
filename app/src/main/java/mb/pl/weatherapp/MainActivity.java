package mb.pl.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends FragmentActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String FORECAST = "forecast";
    public static final String LOCATION = "location";
    public static final String FILENAME = "forecast.json";


    public static final String REQUEST_LOCATION = "location";
    public static final int REQUEST_CODE = 0;

    private ViewPager viewPager;
    private Forecast forecast;

    private String location;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FORECAST, forecast);
        outState.putString(LOCATION, location);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainView);

        if(savedInstanceState != null && savedInstanceState.getSerializable(FORECAST) != null) {
            Log.i(TAG, "Forecast from savedInstance");
            forecast = (Forecast) savedInstanceState.getSerializable(FORECAST);
            location = savedInstanceState.getString(LOCATION);
            createView();
        }else{
            if(DeviceState.isOnline(this)){
                downloadForecastAndSaveToFile();
            }else{
                loadForecastFromFile();
                createView();
            }
        }
    }

    private void createView(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        if(viewPager != null){
            setupViewPager();
        }else{
            FragmentManager fm = getSupportFragmentManager();

            Fragment mainInfo = fm.findFragmentById(R.id.mainInfoContainer);
            if(mainInfo == null) {
                mainInfo = MainInfoFragment.newInstance(forecast);
                fm.beginTransaction()
                        .add(R.id.mainInfoContainer, mainInfo)
                        .commit();
            }

            Fragment additionalInfo = fm.findFragmentById(R.id.additionalInfoContainer);
            if(additionalInfo == null) {
                additionalInfo = AdditionalInfoFragment.newInstance(forecast);
                fm.beginTransaction()
                        .add(R.id.additionalInfoContainer, additionalInfo)
                        .commit();
            }

            Fragment forecastFragment = fm.findFragmentById(R.id.forecastContainer);
            if(forecastFragment == null) {
                forecastFragment = ForecastFragment.newInstance(forecast);
                fm.beginTransaction()
                        .add(R.id.forecastContainer, forecastFragment)
                        .commit();
            }
        }
    }

    private void downloadForecastAndSaveToFile() {
        if(!DeviceState.isOnline(this)){
            showToast(R.string.no_internet_connection);
            return;
        }
        Log.i(TAG, "Downloadnig forecast from internet");
        FetchWeatherTask task = new FetchWeatherTask() {
            @Override
            protected void onPostExecute(String result) {
                if (result == null) {
                    Log.d(TAG, "Forecast is null !!!!");
                    showToast(R.string.location_error);
                    return;
                }
                forecast = new Forecast(result);
                if(forecast.notProper()){
                    showToast(R.string.location_error);
                    return;
                }

                Log.d(TAG, forecast.getForecastJSON());
                saveForecastToFile();
                createView();
            }
        };
        Log.d(TAG, createUri().toString());
        task.execute(createUri());
    }

    private Uri createUri() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        return new WeatherRequest(location).toURI();
    }

    private void loadForecastFromFile() {
        Log.i(TAG, "Forecast from file");
        String fileForecast = null;
        try {
            FileAccess fileAccess = new FileAccess(this, FILENAME);
            fileForecast = fileAccess.loadForecast();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileForecast == null) {
            showToast(R.string.no_data_error);
        }else{
            showToast(R.string.not_online_message);
            forecast = new Forecast(fileForecast);
        }
    }

    private void setupViewPager() {
        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return MainInfoFragment.newInstance(forecast);
                } else if (position == 1) {
                    return AdditionalInfoFragment.newInstance(forecast);
                } else {
                    return ForecastFragment.newInstance(forecast);
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        viewPager.setCurrentItem(0);
    }

    private boolean saveForecastToFile(){
        FileAccess data = new FileAccess(this, FILENAME);
        try {
            data.saveForecast(forecast.getForecastJSON());
            Log.d(TAG, "Forecast saved to file");
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving forecast: ", e);
            return false;
        }
    }

    public void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.i(TAG, "Launching settings");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
            startActivityForResult(new Intent(this, SettingsActivity.class), REQUEST_CODE);
            return true;
        }
        if (id == R.id.action_refresh) {
            downloadForecastAndSaveToFile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            Log.d(TAG, "Not OK");
            return;
        }

        if(requestCode == REQUEST_CODE && data != null && data.hasExtra(REQUEST_LOCATION) ){
            String newLocation = data.getStringExtra(REQUEST_LOCATION);
            if(!location.equals(newLocation)){
                downloadForecastAndSaveToFile();
            }
        }
    }
}
