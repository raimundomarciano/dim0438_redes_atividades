import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URLEncoder;

public class Weather {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Argumentos insuficientes. Use: java Weather <cidade> <estado> <pais>");
            return;
        }

        String city = args[0];
        String state = args[1];
        String country = args[2];
        String apiKey = "9f9a22f23f40bf419777081eed4beb7d";

        Geo geo = new Geo();
        geo.getCoordinates(city, state, country, apiKey);
        System.out.printf("Coordenadas para %s, %s, %s: lat=%.4f, lon=%.4f\n",
                  city, state, country, geo.getLatitude(), geo.getLatitude());

        // Send HTTPS request manually using SSLSocket
        String host = "api.openweathermap.org";
        int port = 443;
        String path = String.format(
            "/data/2.5/weather?lat=%.4f&lon=%.4f&appid=%s&units=metric&lang=pt_br",
            geo.getLatitude(), geo.getLatitude(), apiKey
        );

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try (SSLSocket socket = (SSLSocket) factory.createSocket(host, port)) {
            socket.startHandshake();

            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send GET request
            out.print("GET " + path + " HTTP/1.1\r\n");
            out.print("Host: " + host + "\r\n");
            out.print("User-Agent: JavaSocketClient/1.0\r\n");
            out.print("Connection: close\r\n");
            out.print("\r\n");
            out.flush();

            // Read response
            String line;
            boolean bodyStarted = false;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                if (bodyStarted) {
                    response.append(line).append("\n");
                }
                if (line.isEmpty()) {
                    bodyStarted = true;
                }
            }

            System.out.println("\nDados sobre o clima:");
            System.out.println(response);
        }
    }
}
