import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;

import java.io.*;
import java.net.URLEncoder;

public class TCPClient {
    private Scanner sc;
    private SSLSocket socket;
    private String URLHost;
    private String acessKey;
    private int networkDoor;
    private InetAddress ip;
    private int userChoice = 0;

    public TCPClient(SSLSocket clientSocket, String key, InetAddress ip, int networkDoorIn, String host) {
        this.socket = clientSocket;
        this.acessKey = key;
        this.ip = ip;
        this.networkDoor = networkDoorIn;
        this.URLHost = host;
    }

    public void Connect() throws Exception {
        try{
        this.socket.startHandshake();
        sc = new Scanner(System.in);
        while(true){
            showMenu();
            System.out.println("");
            System.out.println("Escolha uma opção: (Digite o número da opção escolhida)");
            userChoice = sc.nextInt();
            processChoice(userChoice);
        }
        }catch(IOException e){
            System.out.println("Erro ao conectar com servidor web. Verificar a sua disponibilidade.");
            e.printStackTrace();
        }   
    }

    private void terminateConnection(){
        for(int i =0; i <= 9; i++){
            if(i==9){
                System.out.println("Não consegui encerrar a conexão com " + ip.getHostAddress() + ":" + networkDoor);
                System.out.println("Tente novamente depois.");
            }
            else{
            try {
                System.out.println("Encerrando conexão com: " + ip.getHostAddress() + ":" + networkDoor);
                socket.close();
                sc.close();
                System.out.println("Conexão encerrada com: " + ip.getHostAddress() + ":" + networkDoor);
                System.exit(0);
            } catch (IOException e) {
                System.out.println("Não consegui encerrar a conexão com: " + ip.getHostAddress() + ":" + networkDoor);
                System.out.println("Tentando novamente pela " + (i + 1) + " vez.");
                e.printStackTrace();
            }  
        }
        }      
    }
    
    private void findByName(String cityName, String stateCode, String countryCode) {
        Geo localization = new Geo();
        try{
        localization.getCoordinates(cityName, stateCode, countryCode, this.acessKey);
        findByCoordinates(localization.getLatitude(), localization.getLongitude());
        }catch(Exception e){
            System.out.println("Erro ao enviar requisição HTTP/GET via socket para " + ip.getHostAddress() + ":" + networkDoor);
        }
    }

    private void findByZIPCode(String ZIPCode){
        System.out.println("Enviando requisição HTTP/GET via Socket para: " + ip.getHostAddress() + ":" + networkDoor);
        Geo localization = new Geo();
        try {
            localization.coordinatesByZIP(ZIPCode, acessKey);
                    findByCoordinates(localization.getLatitude(), localization.getLongitude());
        } catch (Exception e) {
            System.out.println("Erro ao enviar requisição HTTP/GET via socket para " + ip.getHostAddress() + ":" + networkDoor);
        }

    }

    private void findByCoordinates(double longitude, double latitude) throws Exception {
        System.out.println("Enviando requisição HTTP/GET via Socket para: " + ip.getHostAddress() + ":" + networkDoor);
        Geo geo = new Geo();
        String path = String.format(
            "/data/2.5/weather?lat=%.4f&lon=%.4f&appid=%s&units=metric&lang=pt_br",
            longitude, latitude, acessKey
        );

        
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send GET request
            out.print("GET " + path + " HTTP/1.1\r\n");
            out.print("Host: " + URLHost + "\r\n");
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

    private void showMenu(){
        System.out.println("___________________________________________________");
        System.out.println("-_-_-_-_-_-_-_-_-_-|WHEATER.AI|-_-_-_-_-_-_-_-_-_-");
        System.out.println("1 - Consultar temperatura por código postal");
        System.out.println("2 - Consultar temperatura por nome da localização");
        System.out.println("3 - Consultar temperatura por coordenadas geográficas");
        System.out.println("4 - Terminar conexão");
        System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");
        System.out.println("___________________________________________________");

    }

    private void processChoice(int userChoice) throws Exception {
        System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");
        switch (userChoice) {
            case 1:
                sc.nextLine();
                String ZIPCode;
                System.out.println("Digite o código postal da cidade");
                ZIPCode = sc.nextLine();
                findByZIPCode(ZIPCode);
                break;
            case 2:
                sc.nextLine();
                String cityName, countryCode = "", stateCode = "";
                System.out.println("Digite o nome da cidade (obrigatório)");
                cityName = sc.nextLine();
                System.out.println("Digite o código do estado (opcional)");
                stateCode = sc.nextLine();
                System.out.println("Digite o código do país (opcional)");
                countryCode = sc.nextLine();
                findByName(cityName, stateCode, countryCode);
                break;
            case 3:
                sc.nextLine();
                double latitude, longitude;
                System.out.println("Digite a latitude da localização a ser consultada");
                latitude = sc.nextDouble();
                sc.nextLine();
                System.out.println("Digite a longitude da localização a ser consultada");
                longitude = sc.nextDouble();
                sc.nextLine();
                findByCoordinates(longitude, latitude);
                break;
            case 4:
                sc.nextLine();
                terminateConnection();
                break;
            default:
                sc.nextLine();
                System.out.println("Opção inválida");
                break;
        }
    }
}
