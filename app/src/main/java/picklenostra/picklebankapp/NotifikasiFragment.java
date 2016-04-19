package picklenostra.picklebankapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import picklenostra.picklebankapp.Adapter.NotifikasiAdapter;
import picklenostra.picklebankapp.Helper.VolleyController;
import picklenostra.picklebankapp.Model.NasabahModel;
import picklenostra.picklebankapp.Model.NotifikasiModel;

/**
 * Created by Daniya on 3/20/16.
 */
public class NotifikasiFragment extends Fragment {

    private ListView listView;
    private ArrayList<NotifikasiModel> listNotifikasi;
    private final String URL = "http://private-ba5008-picklesquad.apiary-mock.com/bank/%1$s/notification";
    SharedPreferences shared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifikasi_fragment ,container,false);

        listView = (ListView)view.findViewById(R.id.lv_notifikasi);
        listNotifikasi = new ArrayList<>();
        shared = getActivity().getSharedPreferences("PICKLEBANK", Context.MODE_PRIVATE);

        volleyRequest("1");
        Log.e("LEN",listNotifikasi.size()+"");
        final NotifikasiAdapter notifikasiAdapter = new NotifikasiAdapter(getActivity(),listNotifikasi);
        listView.setAdapter(notifikasiAdapter);
        
        return view;
    }

    private void volleyRequest(String idBank){
        String params = String.format(URL,idBank);
        Log.e("params",params);
        StringRequest request = new StringRequest(Request.Method.GET, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseAPI = new JSONObject(response);
                    JSONArray arrayNotifikasi = responseAPI.getJSONArray("notification");
                    Log.e("Response",arrayNotifikasi.toString());
                    for (int i = 0; i < arrayNotifikasi.length(); i++){
                        NotifikasiModel notifikasiModel = new NotifikasiModel();
                        JSONObject objectNotifikasi = arrayNotifikasi.getJSONObject(i);
                        String notificationId = objectNotifikasi.getString("notificationId");
                        String namaUser = objectNotifikasi.getString("namaUser");
                        int jumlahTagihan = objectNotifikasi.getInt("jumlah");
                        String status = objectNotifikasi.getString("status");
                        String date = objectNotifikasi.getString("tanggal") + ", " + objectNotifikasi.getString("waktu");

                        Log.e("Response2",objectNotifikasi.toString());

                        notifikasiModel.setNotificationId(notificationId);
                        notifikasiModel.setUserName(namaUser);
                        notifikasiModel.setTotalPrice(jumlahTagihan);
                        notifikasiModel.setStatus(status);
                        notifikasiModel.setDate(date);
                        listNotifikasi.add(notifikasiModel);
//                        Log.e("LEN",listNotifikasi.size()+" " + notifikasiModel.getNotificationId());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleyController.getInstance().addToRequestQueue(request);
    }

}
