package picklenostra.picklebankapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import picklenostra.picklebankapp.Model.ItemTransaksiModel;
import picklenostra.picklebankapp.R;

/**
 * Created by Edwin on 5/14/2016.
 */
public class ItemTransaksiAdapter extends BaseAdapter{
    Activity activity;
    ArrayList<ItemTransaksiModel> lisItemTransaksiModel;

    public ItemTransaksiAdapter(Activity activity, ArrayList<ItemTransaksiModel> lisItemTransaksiModel) {
        this.activity = activity;
        this.lisItemTransaksiModel = lisItemTransaksiModel;
    }

    @Override
    public int getCount() {
        return lisItemTransaksiModel.size();
    }

    @Override
    public Object getItem(int position) {
        return lisItemTransaksiModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view  = convertView;
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.transaksi_listviewitem, null);
            viewHolder.transaksiId = (TextView)view.findViewById(R.id.transaksi_id);
            viewHolder.transaksiNamaNasabah = (TextView)view.findViewById(R.id.transaksi_namaNasabah);
            viewHolder.transaksiNominal = (TextView)view.findViewById(R.id.transaksi_nominalTransaksi);
            viewHolder.transaksiTanggal = (TextView)view.findViewById(R.id.transaksi_tanggal);
            viewHolder.transaksiWaktu = (TextView)view.findViewById(R.id.transaksi_waktu);
            viewHolder.transaksiStatus = (TextView)view.findViewById(R.id.transaksi_status);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }

        ItemTransaksiModel itemTransaksiModel = (ItemTransaksiModel)getItem(position);
        viewHolder.transaksiId.setText(itemTransaksiModel.getId()+"");
        viewHolder.transaksiNamaNasabah.setText(itemTransaksiModel.getNamaNasabah());
        viewHolder.transaksiNominal.setText("Rp " + itemTransaksiModel.getNominalTransaksi() + ",-");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(itemTransaksiModel.getWaktu());
        String date = cal.get(Calendar.DAY_OF_MONTH)+"";
        String month = cal.get(Calendar.MONTH)+"";
        String hour = cal.get(Calendar.HOUR_OF_DAY)+"";
        String minute = cal.get(Calendar.MINUTE)+"";
        if (date.length()==1)
            date = "0"+date;
        if(month.length()==1)
            month = "0"+month;
        if(hour.length()==1)
            hour = "0"+hour;
        if (minute.length()==1)
            minute = "0"+minute;

        viewHolder.transaksiTanggal.setText(date+"/"+month+"/"+cal.get(Calendar.YEAR));
        viewHolder.transaksiWaktu.setText(hour+":"+minute);
        String status;
        if(itemTransaksiModel.getStatus() == 1)
            status = "SELESAI";
        else if(itemTransaksiModel.getStatus() == 2)
            status = "DITOLAK";
        else
            status = "MENUNGGU";
        viewHolder.transaksiStatus.setText(status);

        return view;
    }

    static class ViewHolder{
        TextView transaksiId, transaksiNamaNasabah, transaksiNominal, transaksiTanggal, transaksiWaktu, transaksiStatus;
    }
}
