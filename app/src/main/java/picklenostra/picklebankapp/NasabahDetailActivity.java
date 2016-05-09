package picklenostra.picklebankapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import picklenostra.picklebankapp.Adapter.PagerAdapter;
import picklenostra.picklebankapp.Helper.UserSessionManager;
import picklenostra.picklebankapp.Helper.VolleyController;
import picklenostra.picklebankapp.Model.NotifikasiModel;

/**
 * Created by Daniya on 4/28/16.
 */
public class NasabahDetailActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private final String URL = "http://private-ba5008-picklesquad.apiary-mock.com/nasabah/%1$s";
    //SharedPreferences shared;
    private TextView name, id, email, phone, address, totalTrash, balance;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi_detail);

        toolbar = (Toolbar) findViewById(R.id.transac_form_toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle("Detil Nasabah");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        volleyRequest("1");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NasabahDetailActivity.this.onBackPressed();
            }
        });
    }

    private void volleyRequest(final String idNasabah){
        String params = String.format(URL,idNasabah);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject nasabah = response.getJSONObject("nasabah");
                            String id = nasabah.getString("id");
                            String name = nasabah.getString("name");
                            String email = nasabah.getString("email");
                            String phone = nasabah.getString("phone");
                            String location = nasabah.getString("location");
                            int saldo = nasabah.getInt("saldo");
                            int sampahPlastik = nasabah.getInt("sampahPlastik");
                            int sampahKertas = nasabah.getInt("sampahKertas");
                            int sampahBotol = nasabah.getInt("sampahBotol");
                            int sampahBesi = nasabah.getInt("sampahBesi");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /** Needs to be define further more**/
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("name",name);
                //params.put("email", email);

                return params;
            }
        };
        VolleyController.getInstance().addToRequestQueue(request);
    }


}
