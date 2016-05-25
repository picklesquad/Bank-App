package picklenostra.picklebankapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.BaseAdapter;

import picklenostra.picklebankapp.History.*;

/**
 * Created by Edwin on 5/14/2016.
 */
public class HistoryPagerAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;

    public HistoryPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TransaksiFragment transaksiFragment = new TransaksiFragment();
                return transaksiFragment;
            case 1:
                WithdrawalFragment withdrawalFragment = new WithdrawalFragment();
                return withdrawalFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
