package com.victorsystems.zodiacal.data;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import com.victorsystems.zodiacal.data.Structures.*;

public class WebServiceHandler {
    public static final String NAMESPACE = "http://ws.com/";
    public static final String URL = "http://45.55.128.188:8080/ZodiacalWS/Zodiacal";

    public static final String METHOD_GET_SIGNOS = "getSignos";
    public static final String METHOD_GET_CUALIDADES = "getCualidades";

    public String sError;

    private SoapObject request = null;
    private SoapSerializationEnvelope envelope = null;
    private Object resultRequestObject = null;

    public ArrayList<Signos> signos() {
        try {
            request = new SoapObject(NAMESPACE, METHOD_GET_SIGNOS);

            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                transporte.call(NAMESPACE + METHOD_GET_SIGNOS, envelope);
                resultRequestObject = envelope.getResponse();

            } catch (IOException e) {
                sError = e.toString();
                return null;

            } catch (XmlPullParserException e) {
                sError = e.toString();
                return null;
            }

            return Deserialization.signosDeserialization(resultRequestObject);

        } catch (Exception e) {
            sError = e.toString();
            return null;
        }
    }

    public ArrayList<Cualidades> cualidades() {
        try {
            request = new SoapObject(NAMESPACE, METHOD_GET_CUALIDADES);

            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                transporte.call(NAMESPACE + METHOD_GET_CUALIDADES, envelope);
                resultRequestObject = envelope.getResponse();

            } catch (IOException e) {
                sError = e.toString();
                return null;

            } catch (XmlPullParserException e) {
                sError = e.toString();
                return null;
            }

            return Deserialization.cualidadesDeserialization(resultRequestObject);

        } catch (Exception e) {
            sError = e.toString();
            return null;
        }
    }

}
