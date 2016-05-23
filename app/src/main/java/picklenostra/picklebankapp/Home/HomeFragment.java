package picklenostra.picklebankapp.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import picklenostra.picklebankapp.Helper.UserSessionManager;
import picklenostra.picklebankapp.Helper.VolleyController;
import picklenostra.picklebankapp.R;
import picklenostra.picklebankapp.Transaction.TransactionFormActivity;
import picklenostra.picklebankapp.Util.RestUri;


/**
 * Created by Daniya on 3/20/16.
 */

public class HomeFragment extends Fragment {
    private SharedPreferences shared;
    private TextView tvCustomer, tvGarbage;
    private CardView cvGarbage;
    private int totalCustomer, totalGarbage;
    private String idBank;
    private FloatingActionButton add_transac_fab;
    private ProgressBar home_loader;
    private LinearLayout ll_home;
    private final String KEY_TOTAL_NASABAH_BANK = "totalNasabahBank";
    private final String KEY_SAMPAH_PLASTIK_BANK = "sampahPlastikBank";
    private final String KEY_SAMPAH_KERTAS_BANK = "sampahKertasBank";
    private final String KEY_SAMPAH_BOTOL_BANK = "sampahBotolBank";
    private final String KEY_SAMPAH_BESI_BANK = "sampahBesiBank";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment,container,false);

        tvCustomer = (TextView) view.findViewById(R.id.customer_stats_number);
        tvGarbage = (TextView) view.findViewById(R.id.level_name);
        cvGarbage = (CardView) view.findViewById(R.id.garbage_stats);
        add_transac_fab = (FloatingActionButton) view.findViewById(R.id.add_transac_fab);
        home_loader = (ProgressBar) view.findViewById(R.id.home_loader);
        ll_home = (LinearLayout) view.findViewById(R.id.home);

        shared = this.getActivity().getSharedPreferences(getString(R.string.KEY_SHARED_PREF), Context.MODE_PRIVATE);
        idBank = shared.getString(getString(R.string.KEY_ID_BANK),"");
        String apiToken = shared.getString(getString(R.string.KEY_API_TOKEN),"");

        Log.e("token", apiToken);

        volleyRequest(idBank, apiToken);

        totalCustomer = shared.getInt(getString(R.string.KEY_TOTAL_NASABAH_BANK), 0);
        Log.e("nasabah", "" + totalCustomer);
        totalGarbage = shared.getInt(getString(R.string.KEY_SAMPAH_PLASTIK_BANK),0) + shared.getInt(getString(R.string.KEY_SAMPAH_KERTAS_BANK),0) +
                shared.getInt(getString(R.string.KEY_SAMPAH_BESI_BANK),0);

        tvCustomer.setText(totalCustomer + " Nasabah");
        tvGarbage.setText(totalGarbage + " Kg dan " + shared.getInt(getString(R.string.KEY_SAMPAH_BOTOL_BANK),0) + " Botol");

        cvGarbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DetailSampahActivity.class));
            }
        });


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

    private void volleyRequest(final String idBank, final String apiToken){
        StringRequest login =  new StringRequest(Request.Method.GET, RestUri.profile.PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    home_loader.setVisibility(View.GONE);
                    ll_home.setVisibility(View.VISIBLE);
                    add_transac_fab.setVisibility(View.VISIBLE);
                    Log.e("Response Object", responseObject.toString());
                    JSONObject bank = responseObject.getJSONObject("data");
                    int totalNasabah = bank.getInt("totalNasabah");
                    int sampahPlastik = bank.getInt("sampahPlastik");
                    int sampahKertas = bank.getInt("sampahKertas");
                    int sampahBotol = bank.getInt("sampahBotol");
                    int sampahBesi = bank.getInt("sampahBesi");

                    //Simpan data nasabah di Shared Pref
                    shared = getActivity().getSharedPreferences(getString(R.string.KEY_SHARED_PREF), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putInt(KEY_TOTAL_NASABAH_BANK, totalNasabah);
                    editor.putInt(KEY_SAMPAH_PLASTIK_BANK, sampahPlastik);
                    editor.putInt(KEY_SAMPAH_KERTAS_BANK, sampahKertas);
                    editor.putInt(KEY_SAMPAH_BOTOL_BANK, sampahBotol);
                    editor.putInt(KEY_SAMPAH_BESI_BANK, sampahBesi);
                    boolean testCommit = editor.commit();
                    if(testCommit) Log.e("update", "update");

                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }  catch (Exception e){
                    Crashlytics.logException(e);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Crashlytics.logException(error);
                /** Needs to be define further more**/
            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<>();
                headers.put("id", idBank);
                headers.put("apiToken",apiToken);
                return headers;
            }
        };
        VolleyController.getInstance().addToRequestQueue(login);
    }
}
