package picklenostra.picklebankapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import picklenostra.picklebankapp.Adapter.PagerAdapter;
import picklenostra.picklebankapp.Helper.UserSessionManager;

/**
 * Created by Daniya on 4/20/16.
 */
public class TransactionFormActivity extends AppCompatActivity{

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_form);

        toolbar = (Toolbar) findViewById(R.id.transac_form_toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle("Tambah Transaksi");

        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionFormActivity.this.onBackPressed();
            }
        });
        //toolbar.setNavigationIcon(R.mipmap.ic_back_arrow);
        /**toolbar.setNavigationOnClickListener( new View.OnClickListener() {
                    public void OnClick(View view){
                        public void onBackPressed()
                        {
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            fm.popBackStack();
                        }
                    }
                }
        ); **/


    }


    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_main, menu);
    //    return true;
    //}

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
