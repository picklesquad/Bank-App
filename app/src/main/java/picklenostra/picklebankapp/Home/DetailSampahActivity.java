package picklenostra.picklebankapp.Home;

/**
 * Created by marteinstein on 15/05/2016.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import picklenostra.picklebankapp.R;


public class DetailSampahActivity extends AppCompatActivity {

    TextView sampahKertas, sampahBesi, sampahBotol, sampahPlastik;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sampah);
//        getSupportActionBar().setTitle("Detail Sampah");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sampahKertas = (TextView) findViewById(R.id.jumlah_sampah_kertas);
        sampahPlastik = (TextView)findViewById(R.id.jumlah_sampah_plastik);
        sampahBesi = (TextView) findViewById(R.id.jumlah_sampah_besi);
        sampahBotol = (TextView) findViewById(R.id.jumlah_sampah_botol);
        display();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void display() {
        SharedPreferences shared = getSharedPreferences(getResources().getString(R.string.KEY_SHARED_PREF), MODE_PRIVATE);
        sampahPlastik.setText(shared.getInt(getString(R.string.KEY_SAMPAH_PLASTIK_BANK), 0)+" kg");
        sampahBesi.setText(shared.getInt(getString(R.string.KEY_SAMPAH_BESI_BANK), 0)+" kg");
        sampahKertas.setText(shared.getInt(getString(R.string.KEY_SAMPAH_KERTAS_BANK), 0)+" kg");
        sampahBotol.setText( shared.getInt(getString(R.string.KEY_SAMPAH_BOTOL_BANK), 0)+ " buah");
    }

}

