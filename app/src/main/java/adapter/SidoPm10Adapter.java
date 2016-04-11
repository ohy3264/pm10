package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pm10.R;

import java.util.ArrayList;
import java.util.List;

import model.StationModel;
import utill.HYFont;

/**
 * <pre>
 * @author        : oh
 * @Day           : 2014. 11. 20.   
 * @Time          : 오전 11:09:37
 * @Explanation   : 시도시별 Data연결 Adapter
 * </pre>
 *
 */
public class SidoPm10Adapter extends BaseAdapter {
	private Context mContext;
	private List<StationModel> mStationAddrRow;
	private HYFont mFont;

	public SidoPm10Adapter(Context context, ArrayList<StationModel> temp) {
		mContext = context;
		mStationAddrRow = temp;
		mFont = new HYFont(mContext);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mStationAddrRow.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mStationAddrRow.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View ret;
		ViewHolder holder;
        String khaiIndex = null;
		if (convertView == null) {
			ret = LayoutInflater.from(mContext)
					.inflate(R.layout.sido_row_pm10, null);
			holder = new ViewHolder();
			holder.txtStation = (TextView) ret.findViewById(R.id.txtStation);
			holder.txtKhaiValue = (TextView) ret
					.findViewById(R.id.txtKhaiVal);

			ret.setTag(holder);
		} else {
			ret = convertView;
			holder = (ViewHolder) ret.getTag();
		}

		try {
			holder.txtStation.setText(mStationAddrRow.get(position).getStationName());
            switch (mStationAddrRow.get(position).getKhaiGrade()) {
                case "1":
                    khaiIndex = mContext.getResources().getString(
                            R.string.air_index_good);
                    holder.txtKhaiValue.setTextColor(mContext.getResources()
                            .getColor(R.color.good));
                    break;
                case "2":
                    khaiIndex = mContext.getResources().getString(
                            R.string.air_index_usually);
                    holder.txtKhaiValue.setTextColor(mContext.getResources()
                            .getColor(R.color.usually));
                    break;

                case "3":/* case "3":
                    khaiIndex = mContext.getResources().getString(
                            R.string.air_index_slightly_bad);
                    holder.txtKhaiValue.setTextColor(mContext.getResources()
                            .getColor(R.color.slightly_bad));
                    break;*/
                    khaiIndex = mContext.getResources().getString(
                            R.string.air_index_bad);
                    holder.txtKhaiValue.setTextColor(mContext.getResources()
                            .getColor(R.color.bad));
                    break;
                case "4":
                    khaiIndex = mContext.getResources().getString(
                            R.string.air_index_very_bad);
                    holder.txtKhaiValue.setTextColor(mContext.getResources()
                            .getColor(R.color.very_bad));
                    break;
            }

            holder.txtKhaiValue.setText(khaiIndex);
		} catch (Exception e) {
			// TODO: handle exception
			
		}

		mFont.setGlobalFont((ViewGroup) ret);

		return ret;
	}

	public class ViewHolder {
		public TextView txtStation;
		public TextView txtKhaiValue;
	}
}
