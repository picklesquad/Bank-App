package picklenostra.picklebankapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import picklenostra.picklebankapp.Helper.VolleyController;
import picklenostra.picklebankapp.Util.RupiahFormatter;


/**
 * Created by Daniya on 5/8/16.
 */
public class NotifikasiDetailActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private TextView tvId, tvNama, tvStatus, tvBalance, tvDate, tvTime;
    private Button accept, reject, confirm;
    private RelativeLayout notifikasi;
    private ProgressBar loading = null;
    private int loadingStatus = 0;
    private String id;
    private final String URL = "http://104.155.206.184:8080/pickle-0.1/bank/withdraw/%1$s";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi_detail);

        toolbar = (Toolbar) findViewById(R.id.notifikasi_detail_toolbar); // Attaching the layout to the toolbar object
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
    }

    private void volleyRequest(final String id){
        String params = String.format(URL,id+"");
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
                        tvStatus.setText("Menunggu Konfirmasi");
                    } else{
                        tvStatus.setText("Menunggu Pembayaran");
                    }
                    tvDate.setText(DateFormat.format("dd/MM/yyyy",date) + "");
                    tvTime.setText(DateFormat.format("HH:mm",date)+ "");

                    if(status==0){
                        accept.setVisibility(View.VISIBLE);
                        reject.setVisibility(View.VISIBLE);
                    }else{
                        confirm.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleyController.getInstance().addToRequestQueue(request);
    }

}