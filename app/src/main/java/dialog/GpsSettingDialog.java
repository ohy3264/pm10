package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.pm10.R;

import utill.Constant;
import utill.HYFont;


public class GpsSettingDialog extends DialogFragment {

	// Log
	private static final String TAG = "GpsSettingDialog";
	private static final boolean DEBUG = true;
	private static final boolean INFO = true;

	// Utill
	private HYFont mFont;


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        View v = mLayoutInflater.inflate(R.layout.dialog_gps_setting, null);
        // Utill
        mFont = new HYFont(getActivity());
        mFont.setGlobalFont((ViewGroup) v);

        TextView btnOk = (TextView)v.findViewById(R.id.btnOk);
        TextView btnCancel = (TextView)v.findViewById(R.id.btnCancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (INFO)
                    Log.i(TAG, "위치정보 설정창으로 이동합니다.");
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(intent);
                Constant.REQUEST_STATE_CHANGE = true;
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (INFO)
                    Log.i(TAG, "위치정보를 설정하지 않습니다. ");
                dismiss();
            }
        });

        mBuilder.setView(v);
        return mBuilder.create();
	}
}
