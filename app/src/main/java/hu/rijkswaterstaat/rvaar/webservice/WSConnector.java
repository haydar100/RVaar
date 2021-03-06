package hu.rijkswaterstaat.rvaar.webservice;

import android.os.AsyncTask;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.ArrayList;

import hu.rijkswaterstaat.rvaar.domain.UserLocation;

public class WSConnector extends AsyncTask<String, Void, ArrayList<MarkerOptions>> {
    private final static String NAMESPACE = "http://tempuri.org/";
    private final static String URL = "http://145.89.186.168/WebserviceRvaar/Service1.svc?wsdl";

    //     public void saveLocationOfUser(String id, double x, double y) {
    //     public ArrayList<MarkerOptions> getUserLocations(String id) {


    public void removeUserLocation(String id) {
        SoapObject request = new SoapObject(NAMESPACE, "DeleteUser");
        request.addProperty("id", id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        try {
            HttpTransportSE httpTransport = new HttpTransportSE(URL);
            httpTransport.call("http://tempuri.org/IService1/DeleteUser", envelope);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void saveLocationOfUser(String id, double x, double y, String bootnaam, float direction, String boottype, String niveau) {
        SoapObject request = new SoapObject(NAMESPACE, "SaveLocationOfUser");
        request.addProperty("id", id);
        request.addProperty("x", String.valueOf(x));
        request.addProperty("y", String.valueOf(y));
        request.addProperty("bootnaam", bootnaam);
        request.addProperty("richting", String.valueOf(direction));
        request.addProperty("boottype", boottype);
        request.addProperty("niveau", niveau);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        //envelope.implicitTypes = true;
        envelope.setOutputSoapObject(request);

        try {
            HttpTransportSE httpTransport = new HttpTransportSE(URL);
            httpTransport.call("http://tempuri.org/IService1/SaveLocationOfUser", envelope);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<MarkerOptions> getMarkers() {
        ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
        SoapObject request = new SoapObject(NAMESPACE, "geefKruispunten");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "MarkerOptions", MarkerOptions.class);

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call("http://tempuri.org/IService1/geefKruispunten", envelope);
            SoapObject result = (SoapObject) envelope.getResponse();
            final int PropertyCount = result.getPropertyCount();

            for (int i = 0; i < PropertyCount; i++) {
                SoapObject soapResult = (SoapObject) result.getProperty(i);
                String name = soapResult.getPrimitiveProperty("naam").toString();
                double latitude = Double.parseDouble(soapResult.getProperty("latitude").toString());
                double longitude = Double.parseDouble(soapResult.getProperty("longitude").toString());
                String cemt = soapResult.getPrimitivePropertyAsString("cemt");
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Kruispunt: " + name);
                markerOptions.snippet("Binnenvaartschipklasse: " + cemt);
                markers.add(markerOptions);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return markers;
    }

    public ArrayList<UserLocation> getUserLocations(String id) {
        ArrayList<UserLocation> markers = new ArrayList<UserLocation>();
        SoapObject request = new SoapObject(NAMESPACE, "geefUserLocaties");
        request.addProperty("id", id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        envelope.addMapping(NAMESPACE, "MarkerOptions", MarkerOptions.class);

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call("http://tempuri.org/IService1/geefUserLocaties", envelope);
            SoapObject result = (SoapObject) envelope.getResponse();
            final int PropertyCount = result.getPropertyCount();

            for (int i = 0; i < PropertyCount; i++) {
                SoapObject soapResult = (SoapObject) result.getProperty(i);
                double x = Double.parseDouble(soapResult.getProperty("x").toString());
                double y = Double.parseDouble(soapResult.getProperty("y").toString());
                String bootnaam = soapResult.getPrimitivePropertyAsString("bootnaam");
                String idenitity = soapResult.getPrimitivePropertyAsString("ID");
                String bootType = soapResult.getPrimitivePropertyAsString("boottype");
                String niveau = soapResult.getPrimitivePropertyAsString("niveau");
                float direction = Float.parseFloat(soapResult.getProperty("richting").toString());


                UserLocation userLoc = new UserLocation(idenitity, bootnaam, bootType, x, y, direction, niveau);
                markers.add(userLoc);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return markers;
    }


    @Override
    protected ArrayList<MarkerOptions> doInBackground(String... params) {
        return getMarkers();
    }


}