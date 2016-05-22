package picklenostra.picklebankapp.Transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import picklenostra.picklebankapp.Helper.VolleyController;
import picklenostra.picklebankapp.Home.ProfileActivity;
import picklenostra.picklebankapp.R;
import picklenostra.picklebankapp.Util.RestUri;

/**
 * Created by Daniya on 4/20/16.
 */
public class TransactionFormActivity extends AppCompatActivity {

    private EditText etPlastikInput, etPhoneNumbInput, etKertasInput, etLogamInput, etBotolInput, etHargaInput;
    private String idBank;
    private ProgressBar loading;
    SharedPreferences shared;
    private String phoneNumber, harga, plastik, kertas, botol, logam;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_form);

        // Attaching the layout to the toolbar object
        Toolbar toolbar = (Toolbar) findViewById(R.id.transac_form_toolbar);
        toolbar.setTitle("Tambah Transaksi");

        shared = this.getSharedPreferences(getString(R.string.KEY_SHARED_PREF), Context.MODE_PRIVATE);
        idBank = shared.getString(getString(R.string.KEY_ID_BANK), "1");

        // Setting toolbar as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etPlastikInput = (EditText) findViewById(R.id.jumlah_plastik_input);
        etPhoneNumbInput = (EditText) findViewById(R.id.phone_numb_input);
        etKertasInput = (EditText) findViewById(R.id.jumlah_kertas_input);
        etLogamInput = (EditText) findViewById(R.id.jumlah_logam_input);
        etBotolInput = (EditText) findViewById(R.id.jumlah_botol_input);
        etHargaInput = (EditText) findViewById(R.id.transaction_total_input);
        submitButton = (Button) findViewById(R.id.button_transactions);
        loading = (ProgressBar) findViewById(R.id.transac_loading);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionFormActivity.this.onBackPressed();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = etPhoneNumbInput.getText().toString();
                harga = etHargaInput.getText().toString();
                plastik = etPlastikInput.getText().toString();
                kertas = etKertasInput.getText().toString();
                logam = etLogamInput.getText().toString();
                botol = etBotolInput.getText().toString();
                volleyRequest(phoneNumber, harga, plastik, kertas, logam, botol, idBank);
            }
        });

    }

    private void volleyRequest(final String phoneNumber,
                               final String harga,
                               final String plastik,
                               final String kertas,
                               final String logam,
                               final String botol,
                               final String idBank) {
        StringRequest transaksi = new StringRequest(Request.Method.POST, RestUri.transaction.TRANSACTION_NEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            submitButton.setVisibility(View.GONE);
                            loading.setVisibility(View.VISIBLE);

                            JSONObject responseObject = new JSONObject(response);
                            int status = responseObject.getInt("status");

                            if (status == 201) {

                                //Buat intent untuk masuk ke Profile
                                Toast.makeText(getApplicationContext(), "Transaksi berhasil", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(TransactionFormActivity.this, ProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Crashlytics.logException(e);
                            e.printStackTrace();
                        } catch (Exception e) {
                            Crashlytics.logException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Crashlytics.logException(error);
                etPhoneNumbInput.setError(error.getMessage());//Masih belum selesai
            }
        }) {
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("idBank", idBank);
                return headers;
            }
        };
        VolleyController.getInstance().addToRequestQueue(transaksi);
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
