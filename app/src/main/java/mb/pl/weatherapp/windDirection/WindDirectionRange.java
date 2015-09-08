package mb.pl.weatherapp.windDirection;

public class WindDirectionRange {

    private double lowerBound;
    private double upperBound;
    private String symbol;

    public WindDirectionRange(double lowerBound, double upperBound, String symbol) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.symbol = symbol;
    }

    public boolean contains(double test){
        if(test > lowerBound && test <= upperBound) {
            return true;
        }
        return false;
    }

    public String getSymbol() {
        return symbol;
    }
}
