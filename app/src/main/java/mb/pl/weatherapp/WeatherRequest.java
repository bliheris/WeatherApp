package mb.pl.weatherapp;

import android.net.Uri;

public class WeatherRequest {

    public static final int DAYS_NUM = 10;

    private final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    private final String LOCATION_PARAM = "q";
    private final String FORMAT_PARAM = "mode";
    private final String UNITS_PARAM = "units";

    private final String DAYS_PARAM = "cnt";
    private final String UNIT_TYPE = "metric";
    private final String FORMAT = "json";

    private String location;

    public WeatherRequest(String location) {
        this.location = location;
    }

    public Uri toURI(){
        return Uri.parse(FORECAST_BASE_URL).buildUpon().
                appendQueryParameter(LOCATION_PARAM, location).
                appendQueryParameter(FORMAT_PARAM, FORMAT).
                appendQueryParameter(UNITS_PARAM, UNIT_TYPE).
                appendQueryParameter(DAYS_PARAM, Integer.toString(DAYS_NUM)).
                build();
    }
}
