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

import model.StationAddressModel;
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
public class SidoAddressAdapter extends BaseAdapter {
	private Context mContext;
	private List<StationAddressModel> mStationAddrRow;
	private HYFont mFont;

	public SidoAddressAdapter(Context context, ArrayList<StationAddressModel> temp) {
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
		if (convertView == null) {
			ret = LayoutInflater.from(mContext)
					.inflate(R.layout.sido_row_address, null);
			holder = new ViewHolder();
			holder.txtStation = (TextView) ret.findViewById(R.id.txtStation);

			ret.setTag(holder);
		} else {
			ret = convertView;
			holder = (ViewHolder) ret.getTag();
		}

		try {
			holder.txtStation.setText(mStationAddrRow.get(position).getStationName());

		} catch (Exception e) {
			// TODO: handle exception
			
		}

		mFont.setGlobalFont((ViewGroup) ret);

		return ret;
	}

	public class ViewHolder {
		public TextView txtStation;
	}

}
