package picklenostra.picklebankapp.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import picklenostra.picklebankapp.Helper.UserSessionManager;
import picklenostra.picklebankapp.Helper.VolleyController;
import picklenostra.picklebankapp.R;
import picklenostra.picklebankapp.Util.RestUri;
import picklenostra.picklebankapp.Util.Util;

public class LoginActivity extends ActionBarActivity {

    private ImageView imgBrandingLogo;
    private EditText etPhoneNumber, etPassword;
    private ProgressBar pbProgressBar;
    private Button btnLogin;
    private final String KEY_ID_BANK = "idBank";
    private final String KEY_NAMA_BANK = "namaBank";
    private final String KEY_RATING_BANK = "ratingBank";
    private final String KEY_TOTAL_NASABAH_BANK = "totalNasabahBank";
    private final String KEY_SAMPAH_PLASTIK_BANK = "sampahPlastikBank";
    private final String KEY_SAMPAH_KERTAS_BANK = "sampahKertasBank";
    private final String KEY_SAMPAH_BOTOL_BANK = "sampahBotolBank";
    private final String KEY_SAMPAH_BESI_BANK = "sampahBesiBank";
    private String phoneNumber,password, regid;
    UserSessionManager session;
    private GoogleCloudMessaging gcm;
    private SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSessionManager(getApplicationContext());

        imgBrandingLogo = (ImageView) findViewById(R.id.imgBrandingLogo);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        pbProgressBar = (ProgressBar) findViewById(R.id.login_loading);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = etPhoneNumber.getText().toString();
                password = etPassword.getText().toString();

                    btnLogin.setVisibility(View.GONE);
                    pbProgressBar.setVisibility(View.VISIBLE);
                    volleyRequest(phoneNumber,password);

            }
        });
    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean validate(String phoneNumber, String password){
        boolean isValid = true;
        if(phoneNumber.isEmpty() || (phoneNumber.length() >= 10 & phoneNumber.length() <= 12)){
            etPhoneNumber.setError("No HP tidak valid");
            isValid = false;
        }
        if(password.isEmpty() || password.length() < 8){
            etPassword.setError("Password harus lebih dari 8 karakter");
            isValid = false;
        }
        else{
            etPhoneNumber.setError(null);
            etPassword.setError(null);
        }
        return isValid;
    }

    private void volleyRequest(final String phoneNumber, final String password){
        StringRequest login =  new StringRequest(Request.Method.POST, RestUri.login.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbProgressBar.setVisibility(View.GONE);
                    JSONObject responseObject = new JSONObject(response);

                    if(responseObject.getString("message").equals("Login gagal")){
                        Toast.makeText(getApplicationContext(), "No HP atau Password salah!", Toast.LENGTH_LONG).show();
                        session.logoutUser();
                        finish();
                    }else{
                        JSONObject bank = responseObject.getJSONObject("data");
                        String id = bank.getString("id");
                        String nama = bank.getString("nama");
                        int rating = bank.getInt("rating");
                        int totalNasabah = bank.getInt("totalNasabah");
                        int sampahPlastik = bank.getInt("sampahPlastik");
                        int sampahKertas = bank.getInt("sampahKertas");
                        int sampahBotol = bank.getInt("sampahBotol");
                        int sampahBesi = bank.getInt("sampahBesi");

                        //Create Session
                        session.createUserLogin(phoneNumber, password);

                        //Simpan data nasabah di Shared Pref
                        SharedPreferences shared = getSharedPreferences(getResources().getString(R.string.KEY_SHARED_PREF), MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString(KEY_ID_BANK, id);
                        editor.putString(KEY_NAMA_BANK, nama);
                        editor.putInt(KEY_RATING_BANK, rating);
                        editor.putInt(KEY_TOTAL_NASABAH_BANK, totalNasabah);
                        editor.putInt(KEY_SAMPAH_PLASTIK_BANK, sampahPlastik);
                        editor.putInt(KEY_SAMPAH_KERTAS_BANK, sampahKertas);
                        editor.putInt(KEY_SAMPAH_BOTOL_BANK, sampahBotol);
                        editor.putInt(KEY_SAMPAH_BESI_BANK, sampahBesi);
                        editor.commit();


                        getRegId(id);
                        //Buat intent untuk masuk ke Profile
                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }  catch (Exception e){
                    Crashlytics.logException(e);
                }
            }
        }, new Response.ErrorListener(){
        @Override
            public void onErrorResponse(VolleyError error) {
                etPhoneNumber.setError(error.getMessage());
                Crashlytics.logException(error);
                /** Needs to be define further more**/
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phoneNumber",phoneNumber);
                params.put("password",password);
                return params;
            }
        };
        VolleyController.getInstance().addToRequestQueue(login);
    }

    public void getRegId(final String idBank){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params ) {
                String msg = "";
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }
                try{
                    regid = gcm.register(Util.PROJECT_NUMBER);
                }catch (IOException e){
                    msg = "Error " + e.getMessage();
                    Log.e("msg", msg);
                }
                msg = "Device registered, registration ID=" + regid;
                Log.i("GCM",  msg);

                volleySaveGcmId(regid, idBank);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.e("msg", msg + "\n");
            }
        }.execute(null, null, null);
    }

    private void volleySaveGcmId(final String gcmid, final String idBank){
        StringRequest request =  new StringRequest(Request.Method.PUT, RestUri.login.GCM_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    if(responseObject.get("data") == null){
                        Log.e("data", "Null return");
                    }
                    else {
                        String message = responseObject.getString("data");
                        Log.i("data",message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", gcmid);
                return params;
            }

            @Override
            public Map<String, String> getHeaders(){
                Map<String,String> headers = new HashMap<String ,String>();
                headers.put("id", idBank);
                return headers;
            }
        };
        VolleyController.getInstance().addToRequestQueue(request);
    }
}
