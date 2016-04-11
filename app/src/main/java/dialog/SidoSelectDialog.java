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


public class SidoSelectDialog extends DialogFragment {

    // Log
    private static final String TAG = "GpsSidoSelectDialog";
    private static final boolean DEBUG = true;
    private static final boolean INFO = true;

    // Utill
    private HYFont mFont;
    private HYPreference mPref;

    private String mSelLocation;
    private String mAddress;
    private String mItems;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        View v = mLayoutInflater.inflate(R.layout.dialog_select_location, null);
        // Utill
        mFont = new HYFont(getActivity());
        mFont.setGlobalFont((ViewGroup) v);
        mPref = new HYPreference(getActivity());

        // Bundle DataSet
        Bundle mArgs = getArguments();
        mSelLocation = mArgs.getString("SIDO");
        mAddress = mArgs.getString("ADDR");
        mItems = mArgs.getString("ITEMS");

        TextView txtSelLocation = (TextView) v.findViewById(R.id.txt_selLocation);
        TextView txtAddress = (TextView) v.findViewById(R.id.txtAddress);
        TextView txtItems = (TextView) v.findViewById(R.id.txtItems);

        TextView btnOk = (TextView) v.findViewById(R.id.btnOk);
        TextView btnCancel = (TextView) v.findViewById(R.id.btnCancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (INFO)
                    Log.i(TAG, "기본 측정소를 변경 하였습니다.");
                mPref.put(mPref.KEY_STATION, mSelLocation);
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


        txtSelLocation.setText(mSelLocation
                + " 측정소");
        txtAddress.setText(mAddress);
        txtItems.setText(mItems);


        mBuilder.setView(v);

        return mBuilder.create();
    }
}
