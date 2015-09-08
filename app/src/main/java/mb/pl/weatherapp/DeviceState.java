package mb.pl.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DeviceState {

    public static boolean isOnline(Context c){
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        }
        if(!ni.isConnected()) {
            return false;
        }
        if(!ni.isAvailable()) {
            return false;
        }
        return true;
    }
}
