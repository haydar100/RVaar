package hu.rijkswaterstaat.rvaar.webservice;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.MarkerOptions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Berkan on 25-3-2015.
 */
public class WSConnector extends AsyncTask<String, Void, ArrayList<MarkerOptions>> {
    final static String NAMESPACE = "http://tempuri.org/";
    final static String URL = "http://145.89.159.136:8733/Design_Time_Addresses/RVaarWebService//RVaarServiceImpl/?wsdl";


    public ArrayList<MarkerOptions> getMarkers() {
        SoapObject request = new SoapObject(NAMESPACE, "geefKruispunten");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call("http://tempuri.org/RVaarService/geefKruispunten", envelope);
            SoapObject result = (SoapObject) envelope.getResponse();
            final int PropertyCount = result.getPropertyCount();
            Log.d("Propertycount", "" + result.getPropertyCount());
            if (result != null) {

            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected ArrayList<MarkerOptions> doInBackground(String... params) {
        Log.d("doInBackground", "doInBackground !");
        return getMarkers();
    }
}
