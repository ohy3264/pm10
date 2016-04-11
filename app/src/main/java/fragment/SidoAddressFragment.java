package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.app.pm10.R;

import java.util.ArrayList;

import adapter.SidoAddressAdapter;
import adapter.SpinnerAdapter;
import dialog.SidoSelectDialog;
import model.StationAddressModel;
import sqlite.DBManager;
import utill.Constant;
import utill.HYFont;
import utill.HYPreference;

/**
 * <pre>
 *
 * @author : oh
 * @Day : 2014. 11. 20.
 * @Time : 오전 11:09:03
 * @Explanation : 시도시별 측정 정보 Fragment
 * </pre>
 */
public class SidoAddressFragment extends Fragment implements OnItemSelectedListener,
        OnItemClickListener, OnClickListener {
    // Log
    private static final String TAG = "SidoFragment";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;
    // View
    private Spinner mSpinner_Sido;
    private LinearLayout mLinActBackground;

    private SidoSelectDialog mSidoSelDig;

    // DataSet
    private String mSido;
    private String[] mSidoArray;
    // ListView
    private ListView mSidoListView;
    private SidoAddressAdapter mSidoAdapter;
    private ArrayList<StationAddressModel> mStationAddressList = new ArrayList<StationAddressModel>();
    // utill
    private HYFont mFont;
    private HYPreference mPref;
    private DBManager mDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        // View Set
        View fragView = inflater.inflate(R.layout.frgment_station_address, container,
                false);
        mLinActBackground = (LinearLayout)fragView.findViewById(R.id.linActBackground);
        mLinActBackground.setBackgroundColor(Constant.ACTIONBAR_COLOR);

        mSidoArray = getResources().getStringArray(R.array.sido);
        mSpinner_Sido = (Spinner) fragView.findViewById(R.id.spinner_sido);
        mSpinner_Sido.setAdapter(new SpinnerAdapter(getActivity(),
                R.layout.spinner_row_default, mSidoArray));
        mSpinner_Sido.setOnItemSelectedListener(this);
        mSidoListView = (ListView) fragView.findViewById(R.id.sido_listView);
        mSidoListView.setOnItemClickListener(this);


        // Utill Set
        mDB = new DBManager(getActivity());
        mPref = new HYPreference(getActivity());
        mFont = new HYFont(getActivity());

        mSpinner_Sido.setSelection(mPref.getValue(mPref.KEY_SIDO_ADDR, 0));

        mFont.setGlobalFont((ViewGroup) fragView);
        mFont.setGlobalFont((ViewGroup) mSpinner_Sido);
        return fragView;
    }

    public void dataSet(String sido) {
        mStationAddressList = mDB.selectSido(sido);
    }

    public void setAdapter() {
        mSidoAdapter = new SidoAddressAdapter(getActivity(), mStationAddressList);
        if (mSidoListView != null) {
            mSidoListView.setAdapter(mSidoAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
       // mPref.put(mPref.KEY_STATION, mStationAddressList.get(position).getStationName());

        try{
            mSidoSelDig = new SidoSelectDialog();
            Bundle args = new Bundle();
            args.putString("SIDO", mStationAddressList.get(position).getStationName());
            args.putString("ADDR", mStationAddressList.get(position).getAddress());
            args.putString("ITEMS", mStationAddressList.get(position).getItems());
            mSidoSelDig.setArguments(args);

            mSidoSelDig.show(getFragmentManager(), "MYTAG");
            // showStationAlert();
        }catch (Exception e){

        }


        //getActivity().finish();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub
        mSido = parent.getItemAtPosition(position).toString();
        mPref.put(mPref.KEY_SIDO_ADDR, position);
        dataSet(mSido);
        setAdapter();


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (INFO)
                Log.i(TAG, "Visible");

        } else {
            if (INFO)
                Log.i(TAG, "InVisible");

        }
    }

}
