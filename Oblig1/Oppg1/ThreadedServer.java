package Oblig1.Oppg1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedServer {

    public static void main(String[] args) throws IOException {
        final int portnr = 2000;


        ServerSocket serverSocket = new ServerSocket(portnr);

        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("Connection established: " + socket.getInetAddress().getHostAddress());

            ClientHandler clientHandler = new ClientHandler(socket);
            new Thread(clientHandler).start();
        }
    }
}



class ClientHandler implements Runnable{
    private final Socket clientSocket;
    int sum = 0;
    int a = 0;
    int b = 0;

    public ClientHandler(Socket socket){
        clientSocket = socket;
    }

    public void run(){
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;

        try{
            // output from server
            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

            // input from client
            bufferedReader = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
            Integer answer = Integer.parseInt(bufferedReader.readLine());

            while (answer != null){
                a = Integer.parseInt(bufferedReader.readLine());
                b = Integer.parseInt(bufferedReader.readLine());
                sum = 0;

                if (answer == 1){
                    sum = a + b;
                } else if (answer == 2)  {
                    sum = a - b;
                }
                System.out.println("a: " + a + "b: " + b);
                printWriter.println(sum);
                answer = Integer.parseInt(bufferedReader.readLine());

            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}





