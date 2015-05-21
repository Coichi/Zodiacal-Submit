package com.victorsystems.zodiacal.data;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class Structures {

    public static class Signos implements Serializable{
        int signoID;
        String signoDescripcion;
        String amor;
        String salud;
        String dinero;

        public Signos(SoapObject object) {
            if (object.hasProperty("signoID")) {
                this.signoID = Integer.parseInt(object.getProperty("signoID").toString());
                this.signoDescripcion = object.getProperty("signoDescripcion").toString();
                this.amor = object.getProperty("amor").toString();
                this.salud = object.getProperty("salud").toString();
                this.dinero = object.getProperty("dinero").toString();
            }

        }

        public Signos(int signoID, String signoDescripcion, String amor, String salud, String dinero) {
            this.signoID = signoID;
            this.signoDescripcion = signoDescripcion;
            this.amor = amor;
            this.salud = salud;
            this.dinero = dinero;
        }

        public int getSignoID() {
            return signoID;
        }

        public void setSignoID(int signoID) {
            this.signoID = signoID;
        }

        public String getSignoDescripcion() {
            return signoDescripcion;
        }

        public void setSignoDescripcion(String signoDescripcion) {
            this.signoDescripcion = signoDescripcion;
        }

        public String getAmor() {
            return amor;
        }

        public void setAmor(String amor) {
            this.amor = amor;
        }

        public String getSalud() {
            return salud;
        }

        public void setSalud(String salud) {
            this.salud = salud;
        }

        public String getDinero() {
            return dinero;
        }

        public void setDinero(String dinero) {
            this.dinero = dinero;
        }

    }

    public static class Cualidades implements Serializable {
        int signoID;
        String cualidad;

        public Cualidades(SoapObject object) {
            if (object.hasProperty("signoID")) {
                this.signoID = Integer.parseInt(object.getProperty("signoID").toString());
                this.cualidad = object.getProperty("cualidad").toString();
            }
        }

        public Cualidades(int signoID, String cualidad) {
            this.signoID = signoID;
            this.cualidad = cualidad;
        }

        public int getSignoID() {
            return signoID;
        }

        public void setSignoID(int signoID) {
            this.signoID = signoID;
        }

        public String getCualidad() {
            return cualidad;
        }

        public void setCualidad(String cualidad) {
            this.cualidad = cualidad;
        }


    }

}
