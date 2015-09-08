package mb.pl.weatherapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ForecastFragment extends Fragment {

    private static final String EXTRA_FORECAST = "pl.mb.forecastFragment";

    private Forecast forecast;

    public static ForecastFragment newInstance(Forecast forecast) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_FORECAST, forecast);

        ForecastFragment f = new ForecastFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }

    private void updateData() {
        if(forecast != null){
            adapter.clear();
            adapter.addAll(forecast.getForecastList(unitMode()));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forecast = (Forecast) getArguments().getSerializable(EXTRA_FORECAST);
    }

    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        adapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                new ArrayList<String>());

        View view = inflater.inflate(R.layout.forecast_fragment, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

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
