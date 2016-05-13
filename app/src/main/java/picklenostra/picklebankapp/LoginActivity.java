package picklenostra.picklebankapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import picklenostra.picklebankapp.Helper.UserSessionManager;
import picklenostra.picklebankapp.Helper.VolleyController;

public class LoginActivity extends ActionBarActivity {

    private ImageView imgBrandingLogo;
    private EditText etPhoneNumber, etPassword;
    private ProgressBar pbProgressBar;
    private Button btnLogin;
    //private final String url = "http://private-ba5008-picklesquad.apiary-mock.com/login";
    private final String url = "http://104.155.206.184:8080/pickle-0.1/bank/login";

    private final String KEY_ID_BANK = "idBank";
    private final String KEY_NAMA_BANK = "namaBank";
    private final String KEY_RATING_BANK = "ratingBank";
    private final String KEY_TOTAL_NASABAH_BANK = "totalNasabahBank";
    private final String KEY_SAMPAH_PLASTIK_BANK = "sampahPlastikBank";
    private final String KEY_SAMPAH_KERTAS_BANK = "sampahKertasBank";
    private final String KEY_SAMPAH_BOTOL_BANK = "sampahBotolBank";
    private final String KEY_SAMPAH_BESI_BANK = "sampahBesiBank";

    private String phoneNumber,password;
    UserSessionManager session;

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
                if(validate(phoneNumber,password)){
                    btnLogin.setVisibility(View.GONE);
                    pbProgressBar.setVisibility(View.VISIBLE);
                    volleyRequest(phoneNumber,password);
                }
            }
        });
    }

    private boolean validate(String phoneNumber, String password){
        boolean isValid = true;
//        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            etEmail.setError("Enter a valid email address");
//            isValid = false;
//        }
        if(password.isEmpty() || password.length() < 8){
            etPassword.setError("Password must be 8 characters or more");
            isValid = false;
        }
        else{
            etPhoneNumber.setError(null);
            etPassword.setError(null);
        }
        return isValid;
    }

    private void volleyRequest(final String phoneNumber, final String password){
        StringRequest login =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("jsonLogin", response);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    if(responseObject.getString("message").equals("Gagal")){
                        Toast.makeText(getApplicationContext(), "No HP atau Password salah!", Toast.LENGTH_LONG).show();
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

                        //Buat intent untuk masuk ke Profile
                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Log.e("nama",nama);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
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

}
