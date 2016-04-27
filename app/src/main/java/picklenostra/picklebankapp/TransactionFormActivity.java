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



/**
 * Created by Daniya on 4/20/16.
 */
public class TransactionFormActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private final String URL = "http://private-ba5008-picklesquad.apiary-mock.com/bank/%1$s/transaction";
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_form);

        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.transac_form_toolbar);
        toolbar.setTitle("Tambah Transaksi");

        // Setting toolbar as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionFormActivity.this.onBackPressed();
            }
        });


    }





}
