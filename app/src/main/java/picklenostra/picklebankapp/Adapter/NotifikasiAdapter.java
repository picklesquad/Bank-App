package picklenostra.picklebankapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import picklenostra.picklebankapp.Model.NotifikasiModel;
import picklenostra.picklebankapp.R;

/**
 * Created by Edwin on 4/17/2016.
 */
public class NotifikasiAdapter extends BaseAdapter{

    ArrayList<NotifikasiModel> listNotifikasi;
    Activity activity;

    public NotifikasiAdapter(Activity activity, ArrayList<NotifikasiModel> listNotifikasi){
        this.activity = activity;
        this.listNotifikasi = listNotifikasi;
    }

    @Override
    public int getCount() {
        return listNotifikasi.size();
    }

    @Override
    public Object getItem(int position) {
        return listNotifikasi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.notifikasi_listviewitem,null);
            viewHolder.idNotifikasi = (TextView)view.findViewById(R.id.id_notif);
            viewHolder.nameNotifikasi = (TextView)view.findViewById(R.id.tv_user_notifikasi);
            viewHolder.dateNotifikasi = (TextView)view.findViewById(R.id.tv_date_notifikasi);
            viewHolder.priceNotifikasi = (TextView)view.findViewById(R.id.tv_price_notifikasi);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)view.getTag();
        }

        NotifikasiModel notifikasiModel = (NotifikasiModel)getItem(position);
        viewHolder.idNotifikasi.setText(notifikasiModel.getNotificationId());
        viewHolder.nameNotifikasi.setText(notifikasiModel.getUserName());
        viewHolder.dateNotifikasi.setText(notifikasiModel.getDate());
        viewHolder.priceNotifikasi.setText(notifikasiModel.getTotalPrice()+"");

        return view;
    }


    static class ViewHolder{
        TextView idNotifikasi, nameNotifikasi, dateNotifikasi, priceNotifikasi;
    }
}
