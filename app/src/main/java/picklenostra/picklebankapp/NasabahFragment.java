package picklenostra.picklebankapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import picklenostra.picklebankapp.Adapter.NasabahAdapter;
import picklenostra.picklebankapp.Helper.VolleyController;
import picklenostra.picklebankapp.Model.NasabahModel;

/**
 * Created by Daniya on 3/20/16.
 */
public class NasabahFragment extends Fragment{

    private ListView listView;
    private EditText searchInput;
    private String URL = "http://private-ba5008-picklesquad.apiary-mock.com/bank/%1$s/nasabah/";
    private ArrayList<NasabahModel> listNasabah;
    private NasabahAdapter adapter;
    SharedPreferences shared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nasabah_fragment ,container,false);

        //Instansiasi
        listView = (ListView)view.findViewById(R.id.lv_nasabah);
        searchInput = (EditText)view.findViewById(R.id.search_nasabah);
        listNasabah = new ArrayList<>();
        shared = getActivity().getSharedPreferences("PICKLEBANK", Context.MODE_PRIVATE);

        volleyRequest("000001");
        adapter = new NasabahAdapter(getActivity(), listNasabah);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String iduser = (String) listNasabah.get(position).getId();
                Intent intent = new Intent(NasabahFragment.this.getActivity(),NasabahDetailActivity.class);
                intent.putExtra("iduser","1");
                startActivity(intent);
            }
        });

        //Search Functionality
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void volleyRequest(String idBank){
        String params = String.format(URL,idBank);
        final StringRequest request = new StringRequest(Request.Method.GET, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseAPI = new JSONObject(response);
                    JSONArray arrayNasabah = responseAPI.getJSONArray("nasabah");

                    for(int i = 0; i < arrayNasabah.length(); i++){
                        NasabahModel nasabahModel = new NasabahModel();
                        JSONObject objectNasabah = arrayNasabah.getJSONObject(i);
                        String id = objectNasabah.getString("id");
                        String nama = objectNasabah.getString("name");
                        String photoUrl = objectNasabah.getString("photo");
                        String joinDate = objectNasabah.getString("join_date");

                        nasabahModel.setId(id);
                        nasabahModel.setNama(nama);
                        nasabahModel.setPhotoUrl(photoUrl);
                        nasabahModel.setJoinDate(joinDate);
                        listNasabah.add(nasabahModel);
                        adapter.notifyDataSetChanged();
                    }
                    Log.d("Array empty", listNasabah.toString());
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

