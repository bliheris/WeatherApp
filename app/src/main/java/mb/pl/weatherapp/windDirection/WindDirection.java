package mb.pl.weatherapp.windDirection;

import java.util.ArrayList;

public class WindDirection {

    private ArrayList<WindDirectionRange> ranges;
    private double value = 11.25;

    public WindDirection() {
        ranges = new ArrayList<>();
        ranges.add(new WindDirectionRange(0, value, "N"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "NNE"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "NE"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "ENE"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "E"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "ESE"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "SE"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "SSE"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "S"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "SSW"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "SW"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "WSW"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "W"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "WNW"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "NW"));
        ranges.add(new WindDirectionRange(value, increaseValue(), "NNW"));
        ranges.add(new WindDirectionRange(value, 360, "N"));
    }

    private double increaseValue() {
        value += 22.5;
        return value;
    }

    public String degreesToSymbol(double deg){
        for (WindDirectionRange range : ranges) {
            if(range.contains(deg)) {
                return range.getSymbol();
            }
        }
        return "ERROR";
    }
}
