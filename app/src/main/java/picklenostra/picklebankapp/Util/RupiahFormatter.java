package picklenostra.picklebankapp.Util;

import java.text.DecimalFormatSymbols;
import java.text.DecimalFormat;

/**
 * Created by andrikurniawan.id@gmail.com on 5/12/2016.
 */

public class RupiahFormatter {

    public static String format(int harga){
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        String hsl = "Rp. " + df.format(harga);
        return hsl;
    }

}
