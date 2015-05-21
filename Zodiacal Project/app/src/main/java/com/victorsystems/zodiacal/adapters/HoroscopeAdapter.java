package com.victorsystems.zodiacal.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.victorsystems.zodiacal.R;
import com.victorsystems.zodiacal.data.AuxiliarFunctions;
import com.victorsystems.zodiacal.fragments.HoroscopeFragment;

public class HoroscopeAdapter extends CursorAdapter {

    private final int VIEW_TYPE_SPECIAL = 0;
    private final int VIEW_TYPE_NORMAL = 1;
    private boolean mUseSpecialLayout;

    public HoroscopeAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public void setUseSpecialLayout(boolean useTodayLayout) {
        mUseSpecialLayout = useTodayLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseSpecialLayout) ? VIEW_TYPE_SPECIAL : VIEW_TYPE_NORMAL;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        if (viewType == VIEW_TYPE_SPECIAL) {
            layoutId = R.layout.list_item_horoscope_selected;
        } else if (viewType == VIEW_TYPE_NORMAL) {
            layoutId = R.layout.list_item_horoscope;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();

        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_SPECIAL: {
                viewHolder.signoIconView.setImageResource(AuxiliarFunctions.getArtResourceForSignos(
                        cursor.getInt(HoroscopeFragment.COL_SIGNO_ID)));
                viewHolder.todayLuckView.setText(cursor.getString(HoroscopeFragment.COL_AMOR));
                break;
            }
            case VIEW_TYPE_NORMAL: {
                viewHolder.signoIconView.setImageResource(AuxiliarFunctions.getIconResourceForSignos(
                        cursor.getInt(HoroscopeFragment.COL_SIGNO_ID)));
                break;
            }
        }

        viewHolder.signoDescView.setText(cursor.getString(HoroscopeFragment.COL_SIGNO_DESCRIPCION));
    }

    public static class ViewHolder {
        public final ImageView signoIconView;
        public final TextView signoDescView;
        public final TextView todayLuckView;

        public ViewHolder(View view) {
            signoIconView = (ImageView)view.findViewById(R.id.signo_icon);
            signoDescView = (TextView)view.findViewById(R.id.signo_desc);
            todayLuckView = (TextView)view.findViewById(R.id.signo_today_luck);
        }
    }
}
