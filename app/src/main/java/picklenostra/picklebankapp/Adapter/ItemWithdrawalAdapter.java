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

import picklenostra.picklebankapp.Model.ItemWithdrawalModel;
import picklenostra.picklebankapp.R;

/**
 * Created by Edwin on 5/14/2016.
 */
public class ItemWithdrawalAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<ItemWithdrawalModel> lisItemWithdrawalModel;

    public ItemWithdrawalAdapter(Activity activity, ArrayList<ItemWithdrawalModel> lisItemWithdrawalModel) {
        this.activity = activity;
        this.lisItemWithdrawalModel = lisItemWithdrawalModel;
    }

    @Override
    public int getCount() {
        return lisItemWithdrawalModel.size();
    }

    @Override
    public Object getItem(int position) {
        return lisItemWithdrawalModel.get(position);
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
            view = inflater.inflate(R.layout.withdrawal_listviewitem, null);
            viewHolder.withdrawalId = (TextView)view.findViewById(R.id.withdrawal_id);
            viewHolder.withdrawalNamaNasabah = (TextView)view.findViewById(R.id.withdrawal_namaNasabah);
            viewHolder.withdrawalNominal = (TextView)view.findViewById(R.id.withdrawal_nominalWithdrawal);
            viewHolder.withdrawalTanggal = (TextView)view.findViewById(R.id.withdrawal_tanggal);
            viewHolder.withdrawalWaktu = (TextView)view.findViewById(R.id.withdrawal_waktu);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }

        ItemWithdrawalModel itemWithdrawalModel = (ItemWithdrawalModel)getItem(position);
        viewHolder.withdrawalId.setText(itemWithdrawalModel.getId()+"");
        viewHolder.withdrawalNamaNasabah.setText(itemWithdrawalModel.getNamaNasabah());
        viewHolder.withdrawalNominal.setText("Rp " + itemWithdrawalModel.getNominalWithdrawal() + ",-");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(itemWithdrawalModel.getWaktu());
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

        viewHolder.withdrawalTanggal.setText(date+"/"+month+"/"+cal.get(Calendar.YEAR));
        viewHolder.withdrawalWaktu.setText(hour+":"+minute);
        return view;
    }

    static class ViewHolder{
        TextView withdrawalId, withdrawalNamaNasabah, withdrawalNominal, withdrawalTanggal, withdrawalWaktu;
    }
}
