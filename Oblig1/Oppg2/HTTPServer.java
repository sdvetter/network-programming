package Oblig1.Oppg2;

// import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;



public class HTTPServer {

    public static void main(String[] args) throws IOException {

        try(ServerSocket serverSocket = new ServerSocket(80)){
            while (true){
                // accept client
                try(Socket socket = serverSocket.accept()){

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    OutputStream outputStream = socket.getOutputStream();

                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while (! (line = bufferedReader.readLine()).equals("")){
                        stringBuilder.append("<li>" + line + "</li>");
                    }

                    String request = stringBuilder.toString();

                    outputStream.write("HTTP/1.0 200 OK \n".getBytes());
                    outputStream.write("Content-Type: text/html; charset=utf-8 \n".getBytes());
                    outputStream.write("\n".getBytes());
                    outputStream.write(("<HTML><BODY> <H1> Hilsen. Du har koblet deg opp til min enkle web-tjener </h1>\n " +
                            "Header fra klient er: <UL> " + request +
                            " </UL> " +
                            "</BODY></HTML> \n").getBytes());
                    outputStream.flush();
                }
                serverSocket.close();
            }
        }
    }
}
