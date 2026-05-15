/**
 * 
 */
package es.gva.edu.iesjuandegaray.bicis;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
/**
 * 
 */
public class DatosJSon {

	private static String API_URL;
    private String datos = "";
    private String[] values;
    private int numEst;

    public DatosJSon(int nE) {
        numEst = nE;
        datos = "";
        API_URL =
            "https://geoportal.valencia.es/server/rest/services/OPENDATA/Trafico/MapServer/228/query"
            + "?where=1%3D1"
            + "&outFields=*"
            + "&returnGeometry=true"
            + "&f=json";

        values = new String[numEst];
        for (int i = 0; i < numEst; i++)
            values[i] = "";
    }

    public void mostrarDatos(int nE) {
        numEst = nE;
        datos = "";
        API_URL =
            "https://geoportal.valencia.es/server/rest/services/OPENDATA/Trafico/MapServer/228/query"
            + "?where=1%3D1"
            + "&outFields=*"
            + "&returnGeometry=true"
            + "&f=json";

     
        String nombre;
        int bicis;
        int anclajes;
        double x, y;
        values = new String[numEst];
        for (int i = 0; i < numEst; i++)
            values[i] = "";

        if (API_URL.isEmpty()) {
            setDatos(getDatos().concat("La URL de la API no está especificada."));
            return;
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray features = jsonObject.getJSONArray("features");

                    // BUCLE SENCILLO - aquí hay que completarlo
                    for (int i = 0; i < numEst; i++) {
                        JSONObject feature = features.getJSONObject(i);
                        JSONObject attributes = feature.getJSONObject("attributes");
                        JSONObject geometry = feature.getJSONObject("geometry");

                        nombre = attributes.optString("name", "Sin nombre");
                        bicis = attributes.optInt("available", 0);
                        anclajes = attributes.optInt("free", 0);
                        x = geometry.optDouble("x", 0);
                        y = geometry.optDouble("y", 0);

                        // Convertir coordenadas UTM a GPS
                        String coords = ConversionGeoLongLat.conversion(x, y);
                        String[] partes = coords.split(",");
                        String lat2 = partes[0].trim();
                        String lon2 = partes[1].trim();

                        // Guardar datos para mostrar en JTextArea
                        setDatos(getDatos().concat(
                            "Estación: " + nombre + "\n" +
                            "Bicicletas disponibles: " + bicis + "\n" +
                            "Espacios disponibles: " + anclajes + "\n" +
                            "Ubicacion_lat: " + lat2 + "\n" +
                            "Ubicacion_lon: " + lon2 + "\n\n"
                        ));

                        // Guardar datos para insertar en MySQL
                        values[i] = nombre + "," + bicis + "," + anclajes + "," + lat2 + "," + lon2;
                    }

                } catch (org.json.JSONException e) {
                    setDatos(getDatos().concat("Error al procesar los datos JSON: " + e.getMessage()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public String getDatos() { return datos; }
    public void setDatos(String datos) { this.datos = datos; }
    public String[] getValues() { return values; }
    public void setValues(String[] values) { this.values = values; }
    public int getNumEst() { return numEst; }
    public void setNumEst(int numEst) { this.numEst = numEst; }
}