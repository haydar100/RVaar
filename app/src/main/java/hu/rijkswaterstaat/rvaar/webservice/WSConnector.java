package hu.rijkswaterstaat.rvaar.webservice;

import android.os.AsyncTask;


/**
 * Created by Berkan on 25-3-2015.
 */
public class WSConnector extends AsyncTask {
    final static String NAMESPACE = "http://tempuri.org/";
    final static String METHOD_NAME = "geefKruispunten";
    final static String SOAP_ACTION = "http://tempuri.org/GetContent";
    final static String URL = "http://www.example.com/XMLGenerator/GenerateXML.asmx";


    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }
}
