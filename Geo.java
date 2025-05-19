import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Geo {

    public double latitude;
    public double longitude;

        public Geo() {}

    public void getCoordinates(String city, String state, String country, String apiKey) throws Exception {
        String query = city;
        if (!state.isEmpty()) query += "," + state;         // opcional, mas recomendável
        if (!country.isEmpty()) query += "," + country;     // opcional, mas recomendável

        String url = String.format(
            "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s",
            query.replace(" ", "%20"), apiKey
        );

        // chamada da api
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        // busca no json
        int latIndex = body.indexOf("\"lat\":");
        int lonIndex = body.indexOf("\"lon\":");

        if (latIndex == -1 || lonIndex == -1) {
            throw new RuntimeException("Problemas com as coordenadas:\n" + body);
        }

        double lat = Double.parseDouble(body.substring(latIndex + 6, body.indexOf(",", latIndex)).trim());
        double lon = Double.parseDouble(body.substring(lonIndex + 6, body.indexOf(",", lonIndex)).trim());

        this.latitude = lat;
        this.longitude = lon;
    }

    public void coordinatesByZIP(String ZIPCode){

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    
}