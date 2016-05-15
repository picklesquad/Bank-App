package picklenostra.picklebankapp;


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


public class TransaksiFragment extends Fragment {

    private ListView listView;
    private ArrayList<ItemTransaksiModel> listItemTransaksiModel;
    private String URL = "http://104.155.206.184:8080/pickle-0.1/bank/transaction";
    private ItemTransaksiAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaksi_fragment, container, false);

        listView = (ListView)view.findViewById(R.id.transaksi_listview);
        listItemTransaksiModel = new ArrayList<>();

        volleyRequest();

        adapter = new ItemTransaksiAdapter(this.getActivity(), listItemTransaksiModel);
        listView.setAdapter(adapter);
        return view;
    }

    private void volleyRequest(){
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
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

                        ItemTransaksiModel itemTransaksilModel = new ItemTransaksiModel();
                        itemTransaksilModel.setId(idTransaction);
                        itemTransaksilModel.setNamaNasabah(namaNasabah);
                        itemTransaksilModel.setWaktu(waktu);
                        itemTransaksilModel.setNominalTransaksi(harga);
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
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("idBank", "1");
                return headers;
            }
        };
        VolleyController.getInstance().addToRequestQueue(request);
    }

}
