package picklenostra.picklebankapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


/**
 * Created by Daniya on 5/8/16.
 */
public class NotifikasiDetailActivity extends AppCompatActivity{

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasabah_detail);

        toolbar = (Toolbar) findViewById(R.id.notifikasi_detail_toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle("Detil Nasabah");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifikasiDetailActivity.this.onBackPressed();
            }
        });
    }
}