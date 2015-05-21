package com.victorsystems.zodiacal.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.victorsystems.zodiacal.R;
import com.victorsystems.zodiacal.data.AuxiliarFunctions;
import com.victorsystems.zodiacal.data.SignosContract;
import com.victorsystems.zodiacal.data.Structures;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String HOROSCOPE_SHARE_HASHTAG = " #Zodiacal";
    private ShareActionProvider mShareActionProvider;
    private String mHoroscopeStr;

    private static final int DETAIL_LOADER = 0;
    public static final String DETAIL_URI = "URI";

    private static final String[] DETAIL_COLUMNS = {
            SignosContract.SignosEntry.TABLE_NAME + "." + SignosContract.SignosEntry._ID,
            SignosContract.SignosEntry.COLUMN_SIGNO_ID,
            SignosContract.SignosEntry.COLUMN_SIGNO_DESCRIPCION,
            SignosContract.SignosEntry.COLUMN_AMOR,
            SignosContract.SignosEntry.COLUMN_SALUD,
            SignosContract.SignosEntry.COLUMN_DINERO
    };

    private static final String[] CUALIDADES_COLUMNS = {
            SignosContract.CualidadesEntry.TABLE_NAME + "." + SignosContract.SignosEntry._ID,
            SignosContract.CualidadesEntry.COLUMN_SIGNO_ID,
            SignosContract.CualidadesEntry.COLUMN_CUALIDAD
    };

    static final int COL_ID = 0;
    static final int COL_SIGNO_ID = 1;
    static final int COL_SIGNO_DESCRIPCION = 2;
    static final int COL_AMOR = 3;
    static final int COL_SALUD = 4;
    static final int COL_DINERO = 5;

    static final int COL_CUALIDAD_ID = 0;
    static final int COL_CUALIDAD_SIGNO_ID = 1;
    static final int COL_CUALIDAD_DESCRIPCION = 2;

    private ImageView mSignoImageView;
    private TextView mSignoNameView;
    private TextView mAmorView;
    private TextView mSaludView;
    private TextView mDineroView;
    private TextView mCualidad1View;
    private TextView mCualidad2View;
    private TextView mCualidad3View;
    private Uri mUri;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mSignoImageView = (ImageView) rootView.findViewById(R.id.signo_image);
        mSignoNameView = (TextView) rootView.findViewById(R.id.signo_name_textview);
        mAmorView = (TextView) rootView.findViewById(R.id.signo_amor_desc);
        mSaludView = (TextView) rootView.findViewById(R.id.signo_salud_desc);
        mDineroView = (TextView) rootView.findViewById(R.id.signo_dinero_desc);
        mCualidad1View = (TextView) rootView.findViewById(R.id.cualidad_1_textview);
        mCualidad2View = (TextView) rootView.findViewById(R.id.cualidad_2_textview);
        mCualidad3View = (TextView) rootView.findViewById(R.id.cualidad_3_textview);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detailfragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mHoroscopeStr != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mHoroscopeStr + HOROSCOPE_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /*public void onPersonalSignChanged(String newSign) {
        Uri uri = mUri;
        if (null != uri) {
            long signoID = SignosContract.SignosEntry.getIdFromUri(uri);
            Uri updatedUri = SignosContract.SignosEntry.buildSignosUri(signoID);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            return new CursorLoader(getActivity(), mUri, DETAIL_COLUMNS, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            mSignoNameView.setText(data.getString(COL_SIGNO_DESCRIPCION));

            int signoId = data.getInt(COL_SIGNO_ID);
            mSignoImageView.setImageResource(AuxiliarFunctions.getArtResourceForSignos(signoId));
            mSignoNameView.setContentDescription(mSignoNameView.getText());

            mAmorView.setText(data.getString(COL_AMOR));
            mSaludView.setText(data.getString(COL_SALUD));
            mDineroView.setText(data.getString(COL_DINERO));

            Cursor cQualities = getActivity().getContentResolver().query(SignosContract.CualidadesEntry.CONTENT_URI,
                    CUALIDADES_COLUMNS, "signoID = ?", new String[] {String.valueOf(signoId)}, null);


            List<String> listCualidades = new ArrayList<String>();
            cQualities.moveToFirst();
            for (int i = 0; i < cQualities.getCount(); i++) {
                cQualities.moveToPosition(i);
                listCualidades.add(cQualities.getString(COL_CUALIDAD_DESCRIPCION));
            }
            try {
                mCualidad1View.setText(listCualidades.get(0));
                mCualidad2View.setText(listCualidades.get(1));
                mCualidad3View.setText(listCualidades.get(2));
            } catch (Exception e) {
                e.printStackTrace();
            }


            mHoroscopeStr = mSignoNameView.getText() + "\n\n" +
                            "Amor: " + data.getString(COL_AMOR) + "\n\n" +
                            "Salud: " + data.getString(COL_SALUD) + "\n\n" +
                            "Dinero: " + data.getString(COL_DINERO) + "\n\n";

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
