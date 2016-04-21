package picklenostra.picklebankapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import picklenostra.picklebankapp.Helper.UserSessionManager;
import picklenostra.picklebankapp.Helper.VolleyController;

public class LoginActivity extends ActionBarActivity {

    private ImageView imgBrandingLogo;
    private EditText etEmail, etPassword;
    private ProgressBar pbProgressBar;
    private Button btnLogin;
    private final String url = "http://private-ba5008-picklesquad.apiary-mock.com/login";

    private final String KEY_ID_BANK = "idBank";
    private final String KEY_NAMA_BANK = "namaBank";
    private final String KEY_RATING_BANK = "ratingBank";
    private final String KEY_TOTAL_NASABAH_BANK = "totalNasabahBank";
    private final String KEY_SAMPAH_PLASTIK_BANK = "sampahPlastikBank";
    private final String KEY_SAMPAH_KERTAS_BANK = "sampahKertasBank";
    private final String KEY_SAMPAH_BOTOL_BANK = "sampahBotolBank";
    private final String KEY_SAMPAH_BESI_BANK = "sampahBesiBank";

    private String email,password;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSessionManager(getApplicationContext());

        imgBrandingLogo = (ImageView) findViewById(R.id.imgBrandingLogo);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                if(validate(email,password)){
                    volleyRequest(email,password);
                }
            }
        });

    }

    private boolean validate(String email, String password){
        boolean isValid = true;
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Enter a valid email address");
            isValid = false;
        }
        else if(password.isEmpty() || password.length() < 8){
            etPassword.setError("Password must be 8 characters or more");
            isValid = false;
        }
        else{
            etEmail.setError(null);
            etPassword.setError(null);
        }
        return isValid;
    }

    private void volleyRequest(final String email, final String password){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject nasabah = response.getJSONObject("nasabah");
                    String id = nasabah.getString("id");
                    String nama = nasabah.getString("nama");
                    int rating = nasabah.getInt("rating");
                    int totalNasabah = nasabah.getInt("totalNasabah");
                    int sampahPlastik = nasabah.getInt("sampahPlastik");
                    int sampahKertas = nasabah.getInt("sampahKertas");
                    int sampahBotol = nasabah.getInt("sampahBotol");
                    int sampahBesi = nasabah.getInt("sampahBesi");

                    //Create Session
                    session.createUserLogin(email,password);

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

}
