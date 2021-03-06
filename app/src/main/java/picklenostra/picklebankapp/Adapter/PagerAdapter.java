package picklenostra.picklebankapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import picklenostra.picklebankapp.Home.HomeFragment;
import picklenostra.picklebankapp.Nasabah.NasabahFragment;
import picklenostra.picklebankapp.Notifikasi.NotifikasiFragment;

/**
 * Created by Daniya on 3/20/16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                NasabahFragment tab2 = new NasabahFragment();
                return tab2;
            case 2:
                NotifikasiFragment tab3 = new NotifikasiFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
