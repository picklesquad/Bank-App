package picklenostra.picklebankapp.Notifikasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import picklenostra.picklebankapp.Util.RupiahFormatter;


/**
 * Created by Daniya on 5/8/16.
 */
public class NotifikasiDetailActivity extends AppCompatActivity{

    private TextView tvId, tvNama, tvStatus, tvBalance, tvDate, tvTime;
    private Button accept, reject, confirm;
    private RelativeLayout notifikasi;
    private ProgressBar loading = null;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.notifikasi_detail_toolbar);
        toolbar.setTitle("Detil Withdraw");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvId = (TextView) findViewById(R.id.notifikasi_id);
        tvStatus = (TextView) findViewById(R.id.notifikasi_status);
        tvNama = (TextView) findViewById(R.id.notifikasi_nama);
        tvBalance = (TextView) findViewById(R.id.notifikasi_balance);
        tvDate = (TextView) findViewById(R.id.notifikasi_date);
        tvTime = (TextView) findViewById(R.id.notifikasi_time);
        accept = (Button) findViewById (R.id.approval_btn);
        reject =  (Button) findViewById (R.id.disapproval_btn);
        confirm = (Button) findViewById (R.id.konfirmasi_btn);
        loading = (ProgressBar) findViewById (R.id.notifikasi_loader);
        notifikasi = (RelativeLayout) findViewById(R.id.detail);

        id = getIntent().getStringExtra("id");

        volleyRequest(id);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifikasiDetailActivity.this.onBackPressed();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = RestUri.withdraw.WITHDRAW_ACCEPT;
                volleyRequestButton(id,url);
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = RestUri.withdraw.WITHDRAW_REJECT;
                volleyRequestButton(id,url);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = RestUri.withdraw.WITHDRAW_COMPLETE;
                volleyRequestButton(id,url);
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(getIntent().getStringExtra("type") != null){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }else{
            finish();
        }

    }

    private void volleyRequest(final String id){
        String params = String.format(RestUri.withdraw.WITHDRAW_DETAIL,id+"");
        StringRequest request =  new StringRequest(Request.Method.GET, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loading.setVisibility(View.GONE);
                    notifikasi.setVisibility(View.VISIBLE);
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject data = responseObject.getJSONObject("data");

                    String nama = data.getString("nama");
                    String jumlah = RupiahFormatter.format(data.getInt("saldo"));
                    int status = data.getInt("status");
                    long date = data.getLong("waktu");

                    tvNama.setText(nama);
                    tvId.setText(id);
                    tvBalance.setText(jumlah);
                    if(status == 0){
                        tvStatus.setText(getString(R.string.withdraw_pending));
                    }else if(status == 1){
                        tvStatus.setText(getString(R.string.withdraw_accept));
                    }else if(status == -1){
                        tvStatus.setText(getString(R.string.withdraw_reject));
                    }else{
                        tvStatus.setText(getString(R.string.withdraw_complete));
                    }
                    tvDate.setText(DateFormat.format("dd/MM/yyyy",date) + "");
                    tvTime.setText(DateFormat.format("HH:mm",date)+ "");

                    if(status==0){
                        accept.setVisibility(View.VISIBLE);
                        reject.setVisibility(View.VISIBLE);
                    }else if(status==1){
                        confirm.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    Crashlytics.logException(e);
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
        });
        VolleyController.getInstance().addToRequestQueue(request);
    }

    private void volleyRequestButton(final String id, final String url){
        StringRequest request =  new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    String message = responseObject.getString("message");

                    if(message.equals("Success") && url.equals("accept")){
                        Toast.makeText(getApplicationContext(), "Withdraw telah disetujui", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }else if (message.equals("Success") && url.equals("reject")){
                        Toast.makeText(getApplicationContext(), "Withdraw telah ditolak", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }else{
                        Toast.makeText(getApplicationContext(), "Withdraw telah selesai", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(getIntent());
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
            public Map<String, String> getHeaders(){
                Map<String,String> headers = new HashMap<>();
                headers.put("id", id);
                return headers;
            }
        };
        VolleyController.getInstance().addToRequestQueue(request);
    }

}