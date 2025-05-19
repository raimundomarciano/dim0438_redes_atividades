import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.InetAddress;

public class App {

    private static final String[] protocols = new String[]{"TLSv1.3"};
    private static final String[] cipherSuites = new String[]{"TLS_AES_128_GCM_SHA256"};
    
    public static void main(String[] args) {
        String host = "api.openweathermap.org";
        String key = "9f9a22f23f40bf419777081eed4beb7d";
        int porta = 443;
        SSLSocket socket = null;
        InetAddress ip = null;
        for(int i = 0; i <= 9; i++){
            if(i==9){
                System.out.println("Conexão não estabelecida");
                System.out.println("Encerrando a tentativa de contato");
            }
            else{
                try{
                    socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(host, porta);
                    ip = InetAddress.getByName(host);
                    System.out.println("Tentando conexão à: " + ip.getHostAddress() + ":" + porta);
                    socket.setEnabledProtocols(protocols);
                    socket.setEnabledCipherSuites(cipherSuites);
                    System.out.println("Conectado à: " + ip.getHostAddress() + ":" + porta);
                    break;
                }catch(IOException e){
                    System.out.println("O endereço IP do site: " + host + " não foi encontrado");
                    System.out.println("Tentando conexão novamente");
                    return;
                }
            }
        }
        TCPClient client = new TCPClient(socket, key, ip, porta, host);
        client.Connect();
    }
}
