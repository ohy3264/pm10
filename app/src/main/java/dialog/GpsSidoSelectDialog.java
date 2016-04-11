package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.pm10.R;

import utill.Constant;
import utill.HYFont;
import utill.HYPreference;


public class GpsSidoSelectDialog extends DialogFragment {

    // Log
    private static final String TAG = "GpsSidoSelectDialog";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;

    // Utill
    private HYFont mFont;
    private HYPreference mPref;

    private String mNearLocation;


    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        View v = mLayoutInflater.inflate(R.layout.dialog_select_location_gps, null);
        // Utill
        mFont = new HYFont(getActivity());
        mFont.setGlobalFont((ViewGroup) v);
        mPref = new HYPreference(getActivity());

        // Bundle DataSet
        Bundle mArgs = getArguments();
        mNearLocation = mArgs.getString("NEAR");

        TextView txtMaseage = (TextView)v.findViewById(R.id.txt_msg);
        TextView btnOk = (TextView)v.findViewById(R.id.btnOk);
        TextView btnCancel = (TextView)v.findViewById(R.id.btnCancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (INFO)
                    Log.i(TAG, "기본 측정소를 변경 하였습니다.");
                mPref.put(mPref.KEY_STATION, mNearLocation);
                Constant.REQUEST_STATE_CHANGE = true;
                getActivity().finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (INFO)
                    Log.i(TAG, "기존 측정소를 유지합니다.");
                dismiss();
            }
        });


        txtMaseage.setText("기본도시를 " + mNearLocation
                + "로 설정하시 겠습니까?");

        mBuilder.setView(v);

        return mBuilder.create();
	}
}
