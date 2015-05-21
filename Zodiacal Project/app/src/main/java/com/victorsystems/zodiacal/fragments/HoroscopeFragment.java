package com.victorsystems.zodiacal.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.victorsystems.zodiacal.R;
import com.victorsystems.zodiacal.adapters.HoroscopeAdapter;
import com.victorsystems.zodiacal.data.SignosContract;

public class HoroscopeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String SELECTED_KEY = "selected_position";
    private static final int HOROSCOPE_LOADER = 0;

    private static final String[] HOROSCOPE_COLUMNS = {
            SignosContract.SignosEntry.TABLE_NAME + "." + SignosContract.SignosEntry._ID,
            SignosContract.SignosEntry.COLUMN_SIGNO_ID,
            SignosContract.SignosEntry.COLUMN_SIGNO_DESCRIPCION,
            SignosContract.SignosEntry.COLUMN_AMOR,
            SignosContract.SignosEntry.COLUMN_SALUD,
            SignosContract.SignosEntry.COLUMN_DINERO
    };

    public static final int COL_ID = 0;
    public static final int COL_SIGNO_ID = 1;
    public static final int COL_SIGNO_DESCRIPCION = 2;
    public static final int COL_AMOR = 3;
    public static final int COL_SALUD = 4;
    public static final int COL_DINERO = 5;

    private HoroscopeAdapter mHoroscopeAdapter;
    private int mPosition = ListView.INVALID_POSITION;
    private ListView mListView;
    private boolean mUseSpecialLayout;
    private String mLocation;

    public interface Callback {

        public void onItemSelected(Uri singUri);
    }

    public HoroscopeFragment() {
    }

    public void setUseSpecialLayout(boolean useSpecialLayout) {
        mUseSpecialLayout = useSpecialLayout;
        if (mHoroscopeAdapter != null) {
            mHoroscopeAdapter.setUseSpecialLayout(mUseSpecialLayout);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHoroscopeAdapter = new HoroscopeAdapter(getActivity(), null, 0);
        mHoroscopeAdapter.setUseSpecialLayout(mUseSpecialLayout);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview_horoscope);
        mListView.setAdapter(mHoroscopeAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity()).onItemSelected(SignosContract.SignosEntry.buildSignosUri(cursor.getLong(COL_ID)));
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(HOROSCOPE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void onPersonalSignChanged( ) {
        getLoaderManager().restartLoader(HOROSCOPE_LOADER, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = SignosContract.SignosEntry.COLUMN_SIGNO_ID + " ASC";
        Uri horoscopeUri = SignosContract.SignosEntry.buildHoroscopeUri();

        return new CursorLoader(getActivity(),
                horoscopeUri,
                HOROSCOPE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mHoroscopeAdapter.swapCursor(cursor);
        if (mPosition != ListView.INVALID_POSITION) {

            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mHoroscopeAdapter.swapCursor(null);
    }
}
