package picklenostra.picklebankapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import picklenostra.picklebankapp.Adapter.ItemTransaksiAdapter;
import picklenostra.picklebankapp.Model.ItemTransaksiModel;


public class TransaksiFragment extends Fragment {

    private ListView listView;
    private ArrayList<ItemTransaksiModel> listItemTransaksiModel;
    private String URL = "";
    private ItemTransaksiAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaksi_fragment, container, false);

        listView = (ListView)view.findViewById(R.id.transaksi_listview);
        listItemTransaksiModel = new ArrayList<>();

        adapter = new ItemTransaksiAdapter(this.getActivity(), listItemTransaksiModel);
        listView.setAdapter(adapter);
        return view;
    }

    private void volleyRequest(){

    }

}
