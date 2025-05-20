import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.InetAddress;

public class App {

    private static final String[] protocols = new String[]{"TLSv1.3"};
    private static final String[] cipherSuites = new String[]{"TLS_AES_128_GCM_SHA256"};
    
    public static void main(String[] args) throws Exception {
        String host = "api.openweathermap.org";
        String key = "9f9a22f23f40bf419777081eed4beb7d";
        int porta = 443;
        TCPClient client = new TCPClient(key, porta, host);
        client.Connect();
    }
}
