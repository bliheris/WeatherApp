package mb.pl.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AdditionalInfoFragment extends Fragment {

    private static final String EXTRA_FORECAST = "pl.mb.additionalInfo.forecast";

    private Forecast forecast;

    private TextView humidity;
    private TextView windSpeed;
    private TextView windDirection;
    private TextView cloudiness;

    public static AdditionalInfoFragment newInstance(Forecast forecast) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_FORECAST, forecast);

        AdditionalInfoFragment f = new AdditionalInfoFragment();
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
            humidity.setText(forecast.getHumidity());
            windSpeed.setText(forecast.getWindSpeed(unitMode()));
            windDirection.setText(forecast.getWindDirection());
            cloudiness.setText(forecast.getCloudiness());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.additional_info_fragment, container, false);
        humidity = (TextView) view.findViewById(R.id.humidity);
        windSpeed = (TextView) view.findViewById(R.id.windSpeed);
        windDirection = (TextView) view.findViewById(R.id.windDirection);
        cloudiness = (TextView) view.findViewById(R.id.cloudiness);
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
}
