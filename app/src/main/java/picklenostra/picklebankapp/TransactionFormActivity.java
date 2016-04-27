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
import android.widget.EditText;
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
 * Created by Daniya on 4/20/16.
 */
public class TransactionFormActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private EditText etPlastikInput, etPhoneNumbInput, etKertasInput, etLogamInput, etBotolInput, etHargaInput;
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

        etPlastikInput = (EditText) findViewById(R.id.jumlah_plastik_input);
        etPhoneNumbInput = (EditText) findViewById(R.id.phone_numb_input);
        etKertasInput = (EditText) findViewById(R.id.jumlah_kertas_input);
        etLogamInput = (EditText) findViewById(R.id.jumlah_logam_input);
        etBotolInput = (EditText) findViewById(R.id.jumlah_botol_input);
        etHargaInput = (EditText) findViewById(R.id.transaction_total_input);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionFormActivity.this.onBackPressed();
            }
        });


    }

    private void volleyRequest(final String phoneNumber, final String harga, final String plastik, final String kertas, final String logam, final String botol){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    int status = response.getInt("status");

                    if(status == 201){
                        //Buat intent untuk masuk ke Profile
                        Toast.makeText(getApplicationContext(), "Transaksi berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TransactionFormActivity.this,ProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else{
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                etPhoneNumbInput.setError(error.getMessage());//Masih belum selesai
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phoneNumber", phoneNumber);
                params.put("kertas", kertas);
                params.put("plastik", plastik);
                params.put("logam", logam);
                params.put("botol", botol);
                params.put("totalHarga", harga);
                return params;
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("idBank", shared.getString("idBank",""));
//                return headers;
//            }
        };
        VolleyController.getInstance().addToRequestQueue(request);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    }

}
