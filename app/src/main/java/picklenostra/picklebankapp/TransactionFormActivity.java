package picklenostra.picklebankapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
public class TransactionFormActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etPlastikInput, etPhoneNumbInput, etKertasInput, etLogamInput, etBotolInput, etHargaInput;
    private final String URL = "http://104.155.206.184:8080/pickle-0.1/bank/transaction/addNew";
    private String idBank;
    SharedPreferences shared;

    private String phoneNumber, harga, plastik, kertas, botol, logam;
    private Button submitButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_form);

        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.transac_form_toolbar);
        toolbar.setTitle("Tambah Transaksi");

        shared = this.getSharedPreferences(getString(R.string.KEY_SHARED_PREF), Context.MODE_PRIVATE);
        idBank = shared.getString(getString(R.string.KEY_ID_BANK),"1");

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
                volleyRequest(phoneNumber, harga, plastik, kertas, logam, botol,idBank);
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void volleyRequest(final String phoneNumber,
                               final String harga,
                               final String plastik,
                               final String kertas,
                               final String logam,
                               final String botol,
                               final String idBank) {
        StringRequest transaksi = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("json", "masuk");
                            JSONObject responseObject = new JSONObject(response);
                            int status = responseObject.getInt("status");

                            if (status == 201) {
                                Log.e("201", "masuk");
                                //Buat intent untuk masuk ke Profile
                                Toast.makeText(getApplicationContext(), "Transaksi berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TransactionFormActivity.this, ProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TransactionForm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://picklenostra.picklebankapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TransactionForm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://picklenostra.picklebankapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
