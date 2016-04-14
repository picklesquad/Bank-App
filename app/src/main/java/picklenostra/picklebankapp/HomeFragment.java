package picklenostra.picklebankapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Daniya on 3/20/16.
 */


public class HomeFragment extends Fragment {
    private SharedPreferences shared;
    private TextView tvCustomer, tvGarbage;
    private int totalCustomer, totalGarbage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment,container,false);

        tvCustomer = (TextView) view.findViewById(R.id.customer_stats_number);
        tvGarbage = (TextView) view.findViewById(R.id.level_name);

        shared = this.getActivity().getSharedPreferences(getResources().getString(R.string.KEY_SHARED_PREF), Context.MODE_PRIVATE);
        totalCustomer = shared.getInt("totalNasabah", 0);
        totalGarbage = shared.getInt("sampahPlastik",0) + shared.getInt("sampahBesi",0) + shared.getInt("sampahBotol",0) +
                       shared.getInt("sampahBesi",0);

        tvCustomer.setText(100 + " Nasabah");
        tvGarbage.setText(56 + " kgs");

        return view;
    }
}
