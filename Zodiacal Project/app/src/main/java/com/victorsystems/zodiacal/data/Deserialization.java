package com.victorsystems.zodiacal.data;

import com.victorsystems.zodiacal.data.Structures.*;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Vector;

public class Deserialization {
    public static ArrayList<Signos> signosDeserialization(Object object) {
        ArrayList<Signos> list = new ArrayList<Signos>();
        Vector v = (Vector)object;
        try {
            for (int i = 0; i < v.size(); i++) {
                SoapObject soap = (SoapObject)v.get(i);
                Signos sign = new Signos(soap);
                list.add(sign);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ArrayList<Cualidades> cualidadesDeserialization(Object object) {
        ArrayList<Cualidades> list = new ArrayList<Cualidades>();
        Vector v = (Vector)object;
        try {
            for (int i = 0; i < v.size(); i++) {
                SoapObject soap = (SoapObject)v.get(i);
                Cualidades quality = new Cualidades(soap);
                list.add(quality);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
