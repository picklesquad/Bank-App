package picklenostra.picklebankapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
 * Created by Daniya on 4/20/16.
 */
public class TransactionFormActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private final String URL = "http://private-ba5008-picklesquad.apiary-mock.com/bank/%1$s/transaction";
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_form);

        toolbar = (Toolbar) findViewById(R.id.transac_form_toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle("Tambah Transaksi");

        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionFormActivity.this.onBackPressed();
            }
        });


    }

    /**private void volleyRequest(final String email, final String password){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject bank = response.getJSONObject("bank");
                    String id = bank.getString("id");
                    String nama = bank.getString("nama");
                    int rating = bank.getInt("rating");
                    int totalNasabah = bank.getInt("totalNasabah");
                    int sampahPlastik = bank.getInt("sampahPlastik");
                    int sampahKertas = bank.getInt("sampahKertas");
                    int sampahBotol = bank.getInt("sampahBotol");
                    int sampahBesi = bank.getInt("sampahBesi");


                    //Simpan data nasabah di Shared Pref
                    SharedPreferences shared = getSharedPreferences(getResources().getString(R.string.KEY_SHARED_PREF), MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("idBank",id);
                    editor.putString(KEY_NAMA_BANK,nama);
                    editor.putInt(KEY_RATING_BANK, rating);
                    editor.putInt(KEY_TOTAL_NASABAH_BANK, totalNasabah);
                    editor.putInt(KEY_SAMPAH_PLASTIK_BANK, sampahPlastik);
                    editor.putInt(KEY_SAMPAH_KERTAS_BANK, sampahKertas);
                    editor.putInt(KEY_SAMPAH_BOTOL_BANK, sampahBotol);
                    editor.putInt(KEY_SAMPAH_BESI_BANK,sampahBesi);
                    editor.commit();

                    //Buat intent untuk masuk ke Profile
                    Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                etEmail.setError(error.getMessage());//Masih belum selesai
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        VolleyController.getInstance().addToRequestQueue(request);
    }



    /**@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_main, menu);
    //    return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }**/



}
