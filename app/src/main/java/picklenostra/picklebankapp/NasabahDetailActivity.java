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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;

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
import picklenostra.picklebankapp.Util.RupiahFormatter;

/**
 * Created by Daniya on 4/28/16.
 */
public class NasabahDetailActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private TextView tvName, tvNasabahId, tvEmail, tvPhoneNumber, tvAddress, tvBalance, tvPlastik, tvKertas, tvBesi, tvBotol;
    private String iduser;
    private ProgressBar loading;
    private ScrollView sv_nasabah;
    private final String URL = "http://104.155.206.184:8080/pickle-0.1/bank/nasabah/%1$s";
    //SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasabah_detail);

        toolbar = (Toolbar) findViewById(R.id.nasabah_detail_toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle("Detil Nasabah");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvName = (TextView) findViewById(R.id.name);
        tvNasabahId = (TextView) findViewById(R.id.nasabah_id);
        tvEmail = (TextView) findViewById(R.id.nasabah_email);
        tvPhoneNumber = (TextView) findViewById(R.id.nasabah_phone_numb);
        tvAddress = (TextView) findViewById(R.id.nasabah_address);
        tvBalance = (TextView) findViewById(R.id.nasabah_balance);
        tvPlastik = (TextView) findViewById(R.id.nasabah_trash_plastik);
        tvKertas = (TextView) findViewById(R.id.nasabah_trash_kertas);
        tvBesi = (TextView) findViewById(R.id.nasabah_trash_besi);
        tvBotol = (TextView) findViewById(R.id.nasabah_trash_botol);
        loading = (ProgressBar) findViewById(R.id.nasabah_loader);
        sv_nasabah = (ScrollView) findViewById(R.id.scrollView_nasabah);

        iduser = getIntent().getStringExtra("iduser");

        volleyRequest(iduser);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NasabahDetailActivity.this.onBackPressed();
            }
        });
    }

    private void volleyRequest(final String iduser){
        String params = String.format(URL,iduser+"");
        StringRequest request =  new StringRequest(Request.Method.GET, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loading.setVisibility(View.GONE);
                    sv_nasabah.setVisibility(View.VISIBLE);
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject bank = responseObject.getJSONObject("data");
                    String name = bank.getString("nama");
                    String email = bank.getString("email");
                    String phone = bank.getString("phoneNumber");
                    String location = bank.getString("alamat");
                    String saldo = RupiahFormatter.format(bank.getInt("saldo"));
                    double plastik = bank.getDouble("sampahPlastik");
                    double kertas = bank.getDouble("sampahKertas");
                    double besi = bank.getDouble("sampahBesi");
                    double botol = bank.getDouble("sampahBotol");

                    tvName.setText(name);
                    tvNasabahId.setText(iduser);
                    tvEmail.setText(email);
                    tvPhoneNumber.setText(phone);
                    tvAddress.setText(location);
                    tvBalance.setText(saldo);
                    tvKertas.setText(kertas + " Kg");
                    tvPlastik.setText(plastik + " Kg");
                    tvBesi.setText(besi + " Kg");
                    tvBotol.setText(botol + " Buah");


                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Crashlytics.logException(error);
            }
        });
        VolleyController.getInstance().addToRequestQueue(request);
    }


}
