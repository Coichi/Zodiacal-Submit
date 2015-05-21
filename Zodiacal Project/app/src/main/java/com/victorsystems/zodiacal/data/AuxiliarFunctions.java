package com.victorsystems.zodiacal.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.victorsystems.zodiacal.R;

public class AuxiliarFunctions {

    public static String getPersonalSign(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_signos_key), context.getString(R.string.pref_signos_aries));
    }

    public static int personalSignToSignoId(String personal) {
        switch (personal) {
            case "Ar":
                return 1;
            case "Ta":
                return 2;
            case "Ge":
                return 3;
            case "Ca":
                return 4;
            case "Le":
                return 5;
            case "Vi":
                return 6;
            case "Li":
                return 7;
            case "Es":
                return 8;
            case "Sa":
                return 9;
            case "Cp":
                return 10;
            case "Ac":
                return 11;
            case "Pi":
                return 12;
        }

        return 1;
    }

    public static int personalSignToSignoId(Context context) {
        String personal = AuxiliarFunctions.getPersonalSign(context);
        switch (personal) {
            case "Ar":
                return 1;
            case "Ta":
                return 2;
            case "Ge":
                return 3;
            case "Ca":
                return 4;
            case "Le":
                return 5;
            case "Vi":
                return 6;
            case "Li":
                return 7;
            case "Es":
                return 8;
            case "Sa":
                return 9;
            case "Cp":
                return 10;
            case "Ac":
                return 11;
            case "Pi":
                return 12;
        }

        return 1;
    }

    public static int getIconResourceForSignos(int signoId) {
        switch (signoId) {
            case 1:
                return R.drawable.icon_aries;
            case 2:
                return R.drawable.icon_tauro;
            case 3:
                return R.drawable.icon_gemini;
            case 4:
                return R.drawable.icon_cancer;
            case 5:
                return R.drawable.icon_leo;
            case 6:
                return R.drawable.icon_virgo;
            case 7:
                return R.drawable.icon_libra;
            case 8:
                return R.drawable.icon_escorpio;
            case 9:
                return R.drawable.icon_sagitario;
            case 10:
                return R.drawable.icon_capricornio;
            case 11:
                return R.drawable.icon_acuario;
            case 12:
                return R.drawable.icon_piscis;
        }
        return -1;
    }

    public static int getArtResourceForSignos(int signoId) {
        switch (signoId) {
            case 1:
                return R.drawable.art_aries;
            case 2:
                return R.drawable.art_tauro;
            case 3:
                return R.drawable.art_gemini;
            case 4:
                return R.drawable.art_cancer;
            case 5:
                return R.drawable.art_leo;
            case 6:
                return R.drawable.art_virgo;
            case 7:
                return R.drawable.art_libra;
            case 8:
                return R.drawable.art_escorpio;
            case 9:
                return R.drawable.art_sagitario;
            case 10:
                return R.drawable.art_capricornio;
            case 11:
                return R.drawable.art_acuario;
            case 12:
                return R.drawable.art_piscis;
        }
        return -1;
    }

    public static boolean checkInternetConnection(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityMng = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connectivityMng.getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                isConnected = true;
            }
        }
        return isConnected;
    }



}
