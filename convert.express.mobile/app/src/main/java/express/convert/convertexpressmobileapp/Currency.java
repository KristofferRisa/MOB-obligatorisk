package express.convert.convertexpressmobileapp;

import android.util.Log;

/**
 * Created by kristofferrisa on 07.11.2017.
 */
public class Currency {
    public String base;

    public String date;

    public Rates rates;

    public Double GetRate(String currency) throws NoSuchFieldException {
        Object value = rates.getClass().getField(currency);
        Log.i(MainActivity.TAG, "Vale = " + value);

        return null;
    }
}
