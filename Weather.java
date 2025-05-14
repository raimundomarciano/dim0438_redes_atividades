public class Weather {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Argumentos insuficientes. Use: java Weather <cidade> <estado> <pais>");
            return;
        }

        String city = args[0];
        String state = args[1];
        String country = args[2];
        String apiKey = Config.getApiKey();

        Geo.Coordinates coords = Geo.getCoordinates(city, state, country);
        System.out.printf("Coordenadas para %s, %s, %s: lat=%.4f, lon=%.4f\n",
                          city, state, country, coords.latitude, coords.longitude);

        String url = String.format(
            "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric",
            coords.latitude, coords.longitude, apiKey
        );

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        System.out.println("\nDados sobre o clima:");
        System.out.println(response.body());
    }
}