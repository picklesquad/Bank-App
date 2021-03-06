package picklenostra.picklebankapp.Notifikasi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import picklenostra.picklebankapp.Adapter.NotifikasiAdapter;
import picklenostra.picklebankapp.Helper.VolleyController;
import picklenostra.picklebankapp.Model.NotifikasiModel;
import picklenostra.picklebankapp.R;
import picklenostra.picklebankapp.Util.RestUri;
import picklenostra.picklebankapp.Util.RupiahFormatter;

/**
 * Created by Daniya on 3/20/16.
 */
public class NotifikasiFragment extends Fragment {

    private ListView listView;
    private ArrayList<NotifikasiModel> listNotifikasi;
    private NotifikasiAdapter notifikasiAdapter;
    private String idBank;
    SharedPreferences shared;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifikasi_fragment ,container,false);

        listView = (ListView)view.findViewById(R.id.lv_notifikasi);
        listNotifikasi = new ArrayList<>();

        shared = getActivity().getSharedPreferences(getString(R.string.KEY_SHARED_PREF), Context.MODE_PRIVATE);
        idBank = shared.getString(getString(R.string.KEY_ID_BANK),"1");
        final String apiToken = shared.getString(getString(R.string.KEY_API_TOKEN),"");

        volleyRequest(idBank, apiToken);

//        Log.e("LEN",listNotifikasi.size()+"");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Refreshing data on server
                volleyRequest(idBank, apiToken);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        notifikasiAdapter = new NotifikasiAdapter(getActivity(),listNotifikasi);
        listView.setAdapter(notifikasiAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idNotification = "" + listNotifikasi.get(position).getNotificationId();
                Intent intent = new Intent(NotifikasiFragment.this.getActivity(),NotifikasiDetailActivity.class);
                intent.putExtra("id",idNotification);
                startActivity(intent);
            }
        });

        return view;
    }

    private void volleyRequest(final String idBank, final String apiToken){
        StringRequest request = new StringRequest(Request.Method.GET, RestUri.notifikasi.NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseAPI = new JSONObject(response);
                    JSONArray arrayNotifikasi = responseAPI.getJSONArray("data");
                    listNotifikasi.clear();
                    for (int i = 0; i < arrayNotifikasi.length(); i++){
                        NotifikasiModel notifikasiModel = new NotifikasiModel();
                        JSONObject objectNotifikasi = arrayNotifikasi.getJSONObject(i);
                        String notificationId = ""+ objectNotifikasi.getInt("id");
                        String nama = objectNotifikasi.getString("nama");
                        String harga = RupiahFormatter.format(objectNotifikasi.getInt("harga"));
                        int status = objectNotifikasi.getInt("status");
                        long dateTemp = objectNotifikasi.getLong("waktu");

//                        Log.e("Response2",objectNotifikasi.toString());

                        notifikasiModel.setNotificationId(notificationId);
                        notifikasiModel.setNama(nama);
                        notifikasiModel.setHarga(harga);
                        notifikasiModel.setStatus(status);
                        notifikasiModel.setDate(""+ DateFormat.format("dd/MM/yyyy",dateTemp));
                        listNotifikasi.add(notifikasiModel);
                        notifikasiAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }  catch (Exception e){
                    Crashlytics.logException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Crashlytics.logException(error);
            }
        }){
            @Override
            public Map<String,String> getHeaders() {
                Map<String, String> headers = new HashMap<String,String>();
                headers.put("idBank",idBank);
                headers.put("apiToken", apiToken);
                return headers;
            }
        };
        VolleyController.getInstance().addToRequestQueue(request);
    }

}
