package picklenostra.picklebankapp.History;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import picklenostra.picklebankapp.Adapter.ItemTransaksiAdapter;
import picklenostra.picklebankapp.Helper.VolleyController;
import picklenostra.picklebankapp.Model.ItemTransaksiModel;
import picklenostra.picklebankapp.Model.ItemWithdrawalModel;
import picklenostra.picklebankapp.R;
import picklenostra.picklebankapp.Util.RestUri;


public class TransaksiFragment extends Fragment {

    private ArrayList<ItemTransaksiModel> listItemTransaksiModel;
    private ItemTransaksiAdapter adapter;
    private SharedPreferences shared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaksi_fragment, container, false);

        ListView listView = (ListView) view.findViewById(R.id.transaksi_listview);
        listItemTransaksiModel = new ArrayList<>();

        shared = this.getActivity().getSharedPreferences(getString(R.string.KEY_SHARED_PREF), Context.MODE_PRIVATE);
        String idBank = shared.getString(getString(R.string.KEY_ID_BANK),"");
        String apiToken = shared.getString(getString(R.string.KEY_API_TOKEN),"");

        volleyRequest(idBank, apiToken);

        adapter = new ItemTransaksiAdapter(this.getActivity(), listItemTransaksiModel);
        listView.setAdapter(adapter);
        return view;
    }

    private void volleyRequest(final String idBank, final String apiToken){
        StringRequest request = new StringRequest(Request.Method.GET, RestUri.transaction.TRANSACTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objectResponse = new JSONObject(response);
                    JSONArray datas = objectResponse.getJSONArray("data");
                    for (int i = 0; i < datas.length(); i++){
                        JSONObject transaction = (JSONObject)datas.get(i);
                        int idTransaction = transaction.getInt("id");
                        String namaNasabah = transaction.getString("nama");
                        long waktu = transaction.getLong("waktu");
                        int harga = transaction.getInt("harga");
                        int status = transaction.getInt("status");

                        ItemTransaksiModel itemTransaksilModel = new ItemTransaksiModel();
                        itemTransaksilModel.setId(idTransaction);
                        itemTransaksilModel.setNamaNasabah(namaNasabah);
                        itemTransaksilModel.setWaktu(waktu);
                        itemTransaksilModel.setNominalTransaksi(harga);
                        itemTransaksilModel.setStatus(status);
                        listItemTransaksiModel.add(itemTransaksilModel);
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("idBank", idBank);
                headers.put("apiToken", apiToken);
                return headers;
            }
        };
        VolleyController.getInstance().addToRequestQueue(request);
    }

}
