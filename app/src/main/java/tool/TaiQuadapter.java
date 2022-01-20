package tool;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import tool.TaiQuModel;
public class TaiQuadapter extends ArrayAdapter<TaiQuModel> {
    public TaiQuadapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
