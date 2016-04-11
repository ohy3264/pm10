package sqlite;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import model.StationAddressModel;

/**
 * Created by oh on 2015-02-21.
 */
public class DBManager extends SQLiteOpenHelper {
    private static final String TAG = "DBManager";
    public static final boolean DBUG = false;
    public static final boolean INFO = true;
    // Database Version
    private static final int DATABASE_VERSION = 4;
    // Database Name
    private static final String DATABASE_NAME = "pm10.db";
    // Table Name
    private final String mTable_name = "tSidoSelect";
    private static  String PACKAGE_DIR = "/data/data/";

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        initialize(context);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (INFO)
            Log.i(TAG, "onCreate DataBase");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade DataBase");

    }

    public static void initialize(Context ctx) {
        PACKAGE_DIR = PACKAGE_DIR + ctx.getPackageName() + "/databases";
        File folder = new File(PACKAGE_DIR);
        folder.mkdirs();

        File outfile = new File(PACKAGE_DIR + "/" + DATABASE_NAME);


        //if (outfile.length() <= 0) {
        AssetManager assetManager = ctx.getResources().getAssets();
        try {
            InputStream is = assetManager.open(DATABASE_NAME, AssetManager.ACCESS_BUFFER);
            long filesize = is.available();
            byte[] tempdata = new byte[(int) filesize];
            is.read(tempdata);
            is.close();
            outfile.createNewFile();
            FileOutputStream fo = new FileOutputStream(outfile);
            fo.write(tempdata);
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //}
    }


    // 데이터 전체 검색
    public ArrayList<StationAddressModel> selectSido(String sido) {
        if (INFO)
            Log.i(TAG, "selectAll DataBase");
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from " + mTable_name + " where sido = '" + sido + "';";

        ArrayList<StationAddressModel> infos = new ArrayList<StationAddressModel>();
        try {

            Cursor results = db.rawQuery(sql, null);

            results.moveToFirst();

            while (! results.isAfterLast()) {
                StationAddressModel info = new StationAddressModel(results.getString(1), results.getString(2),
                        results.getString(3), results.getString(4));
                infos.add(info);
                results.moveToNext();
                if (DBUG) {
                    Log.d("0", results.getString(0));
                    Log.d("1", results.getString(1));
                    Log.d("2", results.getString(2));
                    Log.d("3", results.getString(3));
                    Log.d("4", results.getString(4));
                }
            }
            results.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return infos;
    }


}
