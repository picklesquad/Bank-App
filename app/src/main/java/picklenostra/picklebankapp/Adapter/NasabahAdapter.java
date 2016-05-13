package picklenostra.picklebankapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import picklenostra.picklebankapp.Model.NasabahModel;
import picklenostra.picklebankapp.R;

/**
 * Created by Edwin on 4/14/2016.
 */
public class NasabahAdapter extends BaseAdapter implements Filterable {

    ArrayList<NasabahModel> listNasabah;
    ArrayList<NasabahModel> defaultListNasabah;//Data list yang tidak akan dirubah
    Activity activity;
    Filter nasabahFilter;

    public NasabahAdapter(Activity activity, ArrayList<NasabahModel> listNasabah){
        this.activity = activity;
        this.listNasabah = listNasabah;
        this.defaultListNasabah = listNasabah;
    }

    @Override
    public int getCount() {
        return listNasabah.size();
    }

    @Override
    public Object getItem(int position) {
        return listNasabah.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        if (view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.nasabah_listviewitem,null);
            holder.nasabahName = (TextView)view.findViewById(R.id.nasabah_name);
            holder.nasabahMemberSince = (TextView)view.findViewById(R.id.nasabah_join_date);
            holder.nasabahPhoto = (ImageView)view.findViewById(R.id.nasabah_photo);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder)view.getTag();
        }

        NasabahModel nasabah = (NasabahModel)getItem(position);
        holder.nasabahName.setText(nasabah.getNama());
        holder.nasabahMemberSince.setText("Berlangganan Sejak: " + nasabah.getMemberSince());
        if(nasabah.getPhotoUrl() == null || nasabah.getPhotoUrl().equals("")) {
            holder.nasabahPhoto.setImageResource(R.mipmap.ic_user);
        }
        else {
            Picasso.with(activity).load(nasabah.getPhotoUrl()).into(holder.nasabahPhoto);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        if (nasabahFilter == null){
            nasabahFilter = new NasabahFilter();
        }
        return nasabahFilter;
    }

    //Search Functionality
    private class NasabahFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint == null || constraint.length() <= 1){
                    filterResults.values = defaultListNasabah;
                    filterResults.count = defaultListNasabah.size();
            }
            else{
                ArrayList<NasabahModel> listNasabahFiltered = new ArrayList<>();
                for (NasabahModel n : listNasabah){
                    if (n.getNama().toLowerCase().contains(constraint.toString()
                            .toLowerCase())) {
                        listNasabahFiltered.add(n);
                    }
                }
                filterResults.values = listNasabahFiltered;
                filterResults.count = listNasabahFiltered.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0){
               notifyDataSetInvalidated();
            }
            else{
                listNasabah = (ArrayList<NasabahModel>) results.values;
                NasabahAdapter.this.notifyDataSetChanged();
            }
        }
    }
    static class ViewHolder{
        ImageView nasabahPhoto;
        TextView nasabahName, nasabahMemberSince;
    }
}
