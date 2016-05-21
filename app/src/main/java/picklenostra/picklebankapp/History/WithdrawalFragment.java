package picklenostra.picklebankapp.History;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import picklenostra.picklebankapp.Adapter.ItemWithdrawalAdapter;
import picklenostra.picklebankapp.Adapter.ItemWithdrawalAdapter;
import picklenostra.picklebankapp.Helper.VolleyController;
import picklenostra.picklebankapp.Model.ItemWithdrawalModel;
import picklenostra.picklebankapp.Model.ItemWithdrawalModel;
import picklenostra.picklebankapp.R;
import picklenostra.picklebankapp.Util.RestUri;

/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawalFragment extends Fragment {

    private ArrayList<ItemWithdrawalModel> listItemWithdrawalModel;
    private ItemWithdrawalAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.withdrawal_fragment, container, false);
        ListView listView = (ListView) view.findViewById(R.id.withdrawal_listview);
        listItemWithdrawalModel = new ArrayList<>();

        volleyRequest();

        adapter = new ItemWithdrawalAdapter(this.getActivity(), listItemWithdrawalModel);
        listView.setAdapter(adapter);
        return view;
    }

    private void volleyRequest(){

        StringRequest request = new StringRequest(Request.Method.GET, RestUri.withdraw.WITHDRAW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objectResponse = new JSONObject(response);
                    Log.e("Response", objectResponse.toString());
                    JSONArray datas = objectResponse.getJSONArray("data");
                    for(int i = 0; i < datas.length(); i++){
                        JSONObject withdrawal = (JSONObject)datas.get(i);
                        int idWithdrawal = withdrawal.getInt("id");
                        String namaNasabah = withdrawal.getString("nama");
                        long waktu = withdrawal.getLong("waktu");
                        int harga = withdrawal.getInt("harga");
                        
                        ItemWithdrawalModel itemWithdrawalModel = new ItemWithdrawalModel();
                        itemWithdrawalModel.setId(idWithdrawal);
                        itemWithdrawalModel.setNamaNasabah(namaNasabah);
                        itemWithdrawalModel.setWaktu(waktu);
                        itemWithdrawalModel.setNominalWithdrawal(harga);
                        listItemWithdrawalModel.add(itemWithdrawalModel);
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
        })
        {
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
