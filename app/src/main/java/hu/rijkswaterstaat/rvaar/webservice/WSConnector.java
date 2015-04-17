package hu.rijkswaterstaat.rvaar.webservice;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
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
    final static String URL = "http://145.89.186.168/WebserviceRvaar/Service1.svc?wsdl";

    //     public void saveLocationOfUser(String id, double x, double y) {
    //     public ArrayList<MarkerOptions> getUserLocations(String id) {


    public void saveLocationOfUser(String id, double x, double y) {
        SoapObject request = new SoapObject(NAMESPACE, "SaveLocationOfUser");
        PropertyInfo p = new PropertyInfo();
        p.setName("id");
        p.setValue(id);
        p.setType(String.class);
        p.setName("x");
        p.setValue(x);
        p.setType(double.class);
        p.setName("y");
        p.setValue(y);
        p.setType(double.class);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

    }


    public ArrayList<MarkerOptions> getMarkers() {
        ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
        SoapObject request = new SoapObject(NAMESPACE, "geefKruispunten");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "MarkerOptions", new MarkerOptions().getClass());

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call("http://tempuri.org/IService1/geefKruispunten", envelope);
            SoapObject result = (SoapObject) envelope.getResponse();
            final int PropertyCount = result.getPropertyCount();
            Log.d("Propertycount", "" + result.getPropertyCount());
            if (result != null) {
                for (int i = 0; i < PropertyCount; i++) {
                    SoapObject soapResult = (SoapObject) result.getProperty(i);
                    String name = soapResult.getPrimitiveProperty("naam").toString();
                    double latitude = Double.parseDouble(soapResult.getProperty("latitude").toString());
                    double longitude = Double.parseDouble(soapResult.getProperty("longitude").toString());
                    String cemt = soapResult.getPrimitivePropertyAsString("cemt").toString();
                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latitude, longitude)).title(name);
                    markerOptions.snippet(cemt);
                    //    Log.i("Created Markeroption", "markeroption created wcf");
                    markers.add(markerOptions);
                }
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


        return markers;
    }


    @Override
    protected ArrayList<MarkerOptions> doInBackground(String... params) {
        Log.d("doInBackground", "doInBackground !");
        return getMarkers();
    }
}