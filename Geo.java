import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;

public class Geo {

    private double latitude;
    private double longitude;

    public static class Coordinates {
        public double latitude;
        public double longitude;

        public Coordinates(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public void getCoordinates(String city, String state, String country, String apiKey) throws Exception {
        String query = URLEncoder.encode(city, "UTF-8");
        if (!state.isEmpty()) query += "," + URLEncoder.encode(state, "UTF-8");
        if (!country.isEmpty()) query += "," + URLEncoder.encode(country, "UTF-8");

        String path = String.format("/geo/1.0/direct?q=%s&limit=1&appid=%s", query, apiKey);
        String body = sendHttpRequest("api.openweathermap.org", 80, path);
        Coordinates coords = parseLatLon(body);

        this.latitude = coords.latitude;
        this.longitude = coords.longitude;
    }

    public void coordinatesByZIP(String zipCode, String apiKey) throws Exception {
        String country = "br";
        String query = URLEncoder.encode(zipCode, "UTF-8") + "," + URLEncoder.encode(country, "UTF-8");
        String path = String.format("/geo/1.0/zip?zip=%s&appid=%s", query, apiKey);
        String body = sendHttpRequest("api.openweathermap.org", 80, path);
        Coordinates coords = parseLatLon(body);

        this.latitude = coords.latitude;
        this.longitude = coords.longitude;
    }

    private String sendHttpRequest(String host, int port, String path) throws IOException {
        try (Socket socket = new Socket(host, port)) {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.print("GET " + path + " HTTP/1.1\r\n");
            out.print("Host: " + host + "\r\n");
            out.print("Connection: close\r\n");
            out.print("\r\n");
            out.flush();

            StringBuilder response = new StringBuilder();
            String line;
            boolean bodyStarted = false;
            while ((line = in.readLine()) != null) {
                if (bodyStarted) {
                    response.append(line);
                }
                if (line.isEmpty()) {
                    bodyStarted = true;
                }
            }

            return response.toString();
        }
    }

    private Coordinates parseLatLon(String body) {
        int latIndex = body.indexOf("\"lat\":");
        int lonIndex = body.indexOf("\"lon\":");

        if (latIndex == -1 || lonIndex == -1) {
            throw new RuntimeException("Erro ao obter coordenadas:\n" + body);
        }

        double lat = Double.parseDouble(body.substring(latIndex + 6, body.indexOf(",", latIndex)).trim());
        double lon = Double.parseDouble(body.substring(lonIndex + 6, body.indexOf(",", lonIndex)).trim());

        return new Coordinates(lat, lon);
    }

    // Getters for client usage
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
