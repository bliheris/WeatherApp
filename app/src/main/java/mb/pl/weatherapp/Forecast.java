package mb.pl.weatherapp;

import android.text.format.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import mb.pl.weatherapp.Units.UNIT_MODE;
import mb.pl.weatherapp.windDirection.WindDirection;

public class Forecast implements Serializable {

    private final String COD = "cod";
    private final String ERROR = "404";
    private final String CITY = "city";
    private final String CITY_NAME = "name";
    private final String COUNTRY_NAME = "country";
    private final String COORD = "coord";
    private final String LON = "lon";
    private final String LAT = "lat";

    private final String HUMIDITY = "humidity";
    private final String WIND_SPEED = "speed";
    private final String CLOUDINESS = "clouds";
    private final String WIND_DIRECTION = "deg";
    private final String PRESSURE = "pressure";

    private final String LIST = "list";

    private final String WEATHER = "weather";
    private final String DESCRIPTION = "description";
    private final String ICON = "icon";

    private final String OWM_TEMPERATURE = "temp";
    private final String DAY = "day";
    private final String NIGHT = "night";
    private final String MAX = "max";
    private final String MIN = "min";

    private String forecastJSON;
    private String cod;

    private String city;
    private String coordinates;
    private String date;
    private double dayTemperature;
    private double nightTemperature;
    private String pressure;
    private String description;

    private String humidity;
    private double windSpeed;
    private String cloudiness;
    private String windDirection;

    private String icon;

    public Forecast(String forecastJSON) {
        this.forecastJSON = forecastJSON;
        try {
            JSONObject forecastJson = new JSONObject(forecastJSON);
            cod = forecastJson.getString(COD);
            if (notProper()) {
                return;
            }
            extractMainData();
            extractAdditionalData();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void extractMainData() throws JSONException {
        JSONObject forecastJson = new JSONObject(forecastJSON);
        JSONObject cityJson = forecastJson.getJSONObject(CITY);
        city = cityJson.getString(CITY_NAME) + ", " + cityJson.getString(COUNTRY_NAME);
        JSONObject coords = cityJson.getJSONObject(COORD);
        coordinates = coords.getString(LON) + ", " + coords.getString(LAT);

        date = getReadableDateString(currentDateTime());

        JSONArray weatherArray = forecastJson.getJSONArray(LIST);
        JSONObject dayForecast = weatherArray.getJSONObject(0);

        JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
        dayTemperature = temperatureObject.getDouble(DAY);
        nightTemperature = temperatureObject.getDouble(NIGHT);

        pressure = dayForecast.getDouble(PRESSURE) + " " + Units.PRESSURE;

        JSONObject weatherObject = dayForecast.getJSONArray(WEATHER).getJSONObject(0);
        description = weatherObject.getString(DESCRIPTION);
        icon = weatherObject.getString(ICON);
    }

    private long currentDateTime(){
        Time dayTime = new Time();
        dayTime.setToNow();
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
        dayTime = new Time();
        return dayTime.setJulianDay(julianStartDay);
    }


    private void extractAdditionalData() throws JSONException {
        JSONObject forecastJson = new JSONObject(forecastJSON);
        JSONArray weatherArray = forecastJson.getJSONArray(LIST);
        JSONObject dayForecast = weatherArray.getJSONObject(0);
        humidity = dayForecast.getString(HUMIDITY) + " " + Units.HUMIDITY;
        windSpeed = dayForecast.getDouble(WIND_SPEED);
        cloudiness = dayForecast.getString(CLOUDINESS) + " " + Units.CLOUDINESS;
        double windD = dayForecast.getDouble(WIND_DIRECTION);
        windDirection = new WindDirection().degreesToSymbol(windD);
    }

    private String[] extractForecastList(UNIT_MODE unitMode) throws JSONException {
        JSONObject forecastJson = new JSONObject(forecastJSON);
        JSONArray weatherArray = forecastJson.getJSONArray(LIST);

        Time dayTime = new Time();
        dayTime.setToNow();
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
        dayTime = new Time();

        String[] forecastList = new String[WeatherRequest.DAYS_NUM];
        for(int i = 0; i < weatherArray.length(); i++) {
            JSONObject dayForecast = weatherArray.getJSONObject(i);
            long dateTime;
            dateTime = dayTime.setJulianDay(julianStartDay+i);
            String day = getReadableDateString(dateTime);

            JSONObject weatherObject = dayForecast.getJSONArray(WEATHER).getJSONObject(0);
            String description = weatherObject.getString(DESCRIPTION);

            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(MAX);
            double low = temperatureObject.getDouble(MIN);
            String highAndLow = formatHighLows(high, low, unitMode);
            forecastList[i] = day + " - " + description + " - " + highAndLow;
        }

        return forecastList;
    }

    private String formatHighLows(double high, double low, UNIT_MODE unitMode) {
        if (unitMode.equals(UNIT_MODE.IMPERIAL)) {
            high = convertToFahrenheit(high);
            low = convertToFahrenheit(low);
        }
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + unitMode.getTemperatureSymbol()
                + "/" + roundedLow + unitMode.getTemperatureSymbol();
        return highLowStr;
    }

    private double convertToFahrenheit(double temperature){
        return (temperature * 1.8) + 32;
    }

    private String getReadableDateString(long time){
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE dd MMM ");
        return shortenedDateFormat.format(time);
    }

    public boolean notProper() {
        return cod.equals(ERROR);
    }

    public String getForecastJSON() {
        return forecastJSON;
    }

    public String getCity() {
        return city;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getDate() {
        return date;
    }

    public String getDayTemperature(UNIT_MODE unitMode) {
        double temp = dayTemperature;
        if (unitMode.equals(UNIT_MODE.IMPERIAL)) {
            temp = convertToFahrenheit(temp);
        }
        temp = Math.round(temp);
        return temp + " " + unitMode.getTemperatureSymbol();
    }

    public String getNightTemperature(UNIT_MODE unitMode){
        double temp = nightTemperature;
        if (unitMode.equals(UNIT_MODE.IMPERIAL)) {
            temp = convertToFahrenheit(temp);
        }
        temp = Math.round(temp);
        return temp + " " + unitMode.getTemperatureSymbol();
    }

    public String getPressure() {
        return pressure;
    }

    public String getDescription() {
        return description;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWindSpeed(UNIT_MODE unitMode) {
        BigDecimal ws = new BigDecimal(windSpeed);
        if (unitMode.equals(UNIT_MODE.IMPERIAL)) {
            ws = ws.multiply(new BigDecimal(2.2369362920544));
        }
        ws = ws.setScale(2, BigDecimal.ROUND_UP);
        return ws.toString() + " " + unitMode.getSpeedSymbol();
    }

    public String getCloudiness() {
        return cloudiness;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getIcon() {
        return icon;
    }

    public String[] getForecastList(UNIT_MODE unitMode) {
        try {
            return extractForecastList(unitMode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new String[]{};
    }
}
