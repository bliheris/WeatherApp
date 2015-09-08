package mb.pl.weatherapp;

public class Units {

    public static final String CELSIUS = "°C";
    public static final String FAHRENHEIT = "°F";
    public static final String HUMIDITY = "%";
    public static final String CLOUDINESS = "%";
    public static final String PRESSURE = "hPa";
    public static final String SPEED_METRIC = "m/s";
    public static final String SPEED_IMPERIAL = "mph";

    public enum UNIT_MODE {
        METRIC(CELSIUS, SPEED_METRIC),
        IMPERIAL(FAHRENHEIT, SPEED_IMPERIAL);

        private String temperatureSymbol;
        private String speedSymbol;

        UNIT_MODE(String temperatureSymbol, String speedSymbol) {
            this.temperatureSymbol = temperatureSymbol;
            this.speedSymbol = speedSymbol;
        }

        public String getTemperatureSymbol() {
            return temperatureSymbol;
        }

        public String getSpeedSymbol() {
            return speedSymbol;
        }
    }
}
