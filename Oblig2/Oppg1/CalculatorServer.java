package Oblig2.Oppg1;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.StringTokenizer;

public class CalculatorServer {

    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(2500);
        byte[] buffer;
        double sum = 0;
        DatagramPacket packet;
        while (true){
            // Datapacket to receive data
            buffer = new byte[65535];
            packet = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(packet);


            // receiving input from client
            String clientInput = new String(buffer, 0, buffer.length);

            if (clientInput.equals(null)) break;

            // splitting string into numbers/operand
            StringTokenizer stringTokenizer = new StringTokenizer(clientInput);
            double i1 = Double.parseDouble(stringTokenizer.nextToken());
            String c = stringTokenizer.nextToken();
            double i2 = Double.parseDouble(stringTokenizer.nextToken());


            // calculating result based on operand
            if (c.equals("+")){
                sum = i1 + i2;
            } else if (c.equals("-")){
                sum = i1 - i2;
            } else if (c.equals("/")){
                sum = i1 / i2;
            } else if (c.equals("*")) {
                sum = i1 * i2;
            }

            // data for new packet to send to client
            String result = Double.toString(sum);
            buffer = result.getBytes();

            // sending packet to client
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), packet.getPort());
            datagramSocket.send(sendPacket);

        }
        datagramSocket.close();
    }
}
