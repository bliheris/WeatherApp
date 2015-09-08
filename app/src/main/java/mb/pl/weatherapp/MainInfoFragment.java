package mb.pl.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class MainInfoFragment extends Fragment {

    private static final String EXTRA_FORECAST = "pl.mb.mainInfo.forecast";

    private Forecast forecast;

    private TextView city;
    private TextView coordinates;
    private TextView date;
    private TextView dayTemperature;
    private TextView nightTemperature;
    private TextView pressure;
    private TextView description;
    private ImageView weatherConditionIcon;

    public static MainInfoFragment newInstance(Forecast forecast) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_FORECAST, forecast);

        MainInfoFragment f = new MainInfoFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forecast = (Forecast) getArguments().getSerializable(EXTRA_FORECAST);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }

    private void updateData() {
        if(forecast != null){
            city.setText(forecast.getCity());
            coordinates.setText(forecast.getCoordinates());
            date.setText(forecast.getDate());
            dayTemperature.setText(forecast.getDayTemperature(unitMode()));
            nightTemperature.setText(forecast.getNightTemperature(unitMode()));
            pressure.setText(forecast.getPressure());
            description.setText(forecast.getDescription());
            weatherConditionIcon.setImageResource(imageFromIcon(forecast.getIcon()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_info_fragment, container, false);
        city = (TextView) view.findViewById(R.id.cityName);
        coordinates = (TextView) view.findViewById(R.id.coord);
        date = (TextView) view.findViewById(R.id.date);
        dayTemperature = (TextView) view.findViewById(R.id.dayTemperature);
        nightTemperature = (TextView) view.findViewById(R.id.nightTemperature);
        pressure = (TextView) view.findViewById(R.id.pressure);
        description = (TextView) view.findViewById(R.id.description);
        weatherConditionIcon = (ImageView) view.findViewById(R.id.weatherConditionIcon);
        return view;
    }

    private Units.UNIT_MODE unitMode(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String unitType = prefs.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_metric));
        if(unitType.equals(getString(R.string.pref_units_metric))) {
            return Units.UNIT_MODE.METRIC;
        }else {
            return Units.UNIT_MODE.IMPERIAL;
        }
    }

    private int imageFromIcon(String icon){
        HashMap<String, Integer> map = new HashMap<>();
        map.put("01d", R.drawable.d01);
        map.put("02d", R.drawable.d02);
        map.put("03d", R.drawable.d03);
        map.put("04d", R.drawable.d04);
        map.put("09d", R.drawable.d09);
        map.put("10d", R.drawable.d10);
        map.put("11d", R.drawable.d11);
        map.put("13d", R.drawable.d13);
        map.put("50d", R.drawable.d50);

        map.put("01n", R.drawable.n01);
        map.put("02n", R.drawable.n02);
        map.put("03n", R.drawable.n03);
        map.put("04n", R.drawable.n04);
        map.put("09n", R.drawable.n09);
        map.put("10n", R.drawable.n10);
        map.put("11n", R.drawable.n11);
        map.put("13n", R.drawable.n13);
        map.put("50n", R.drawable.n50);

        return map.get(icon);
    }
}
