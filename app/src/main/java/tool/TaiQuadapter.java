package tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.reportofpowercut.R;

import java.util.List;

public class TaiQuadapter extends ArrayAdapter<TaiQuModel> {
    private final int id;
    public TaiQuadapter(Context context, int resource, List<TaiQuModel> list) {
        super(context,resource,list);
        id = resource;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        TaiQuModel taiQuModel = getItem(position);
        @SuppressLint("ViewHolder") View view1 = LayoutInflater.from(getContext()).inflate(id,viewGroup,false);
        TextView taiqu = (TextView)view1.findViewById(R.id.taiqu_name);
        TextView num = (TextView) view1.findViewById(R.id.taiqu_num);
        taiqu.setText(taiQuModel.taiqu);
        num.setText("低压户数："+taiQuModel.num);
        return view1;
    }

}
