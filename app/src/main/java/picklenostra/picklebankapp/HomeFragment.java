package picklenostra.picklebankapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
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

        shared = this.getActivity().getSharedPreferences(getString(R.string.KEY_SHARED_PREF), Context.MODE_PRIVATE);
        totalCustomer = shared.getInt(getString(R.string.KEY_TOTAL_NASABAH_BANK), 0);
        totalGarbage = shared.getInt(getString(R.string.KEY_SAMPAH_PLASTIK_BANK),0) + shared.getInt(getString(R.string.KEY_SAMPAH_KERTAS_BANK),0) +
                shared.getInt(getString(R.string.KEY_SAMPAH_BESI_BANK),0);

        tvCustomer.setText(totalCustomer + " Nasabah");
        tvGarbage.setText(totalGarbage + " kg dan " + shared.getInt(getString(R.string.KEY_SAMPAH_BOTOL_BANK),0) + " Buah");

        //Floating Action Button
        FloatingActionButton transacFAB = (FloatingActionButton)  view.findViewById(R.id.add_transac_fab);
        transacFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TransactionFormActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            }
        });

        return view;
    }
}
