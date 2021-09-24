package Oblig2.Oppg1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Scanner;

public class UDPClient {

    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket();
        Scanner s = new Scanner(System.in);
        byte[] buffer;
        DatagramPacket sendPacket;
        DatagramPacket receivedPacket;

        while(true){
            System.out.println(" ");
            System.out.println("please enter numbers with following format:");
            System.out.println("number operand number");
            System.out.println("ex: 5 + 5  or  4 * 5");
            String input = s.nextLine();

            // empty line == exit
            if (input.equals("")){
                System.out.println("program exited");
                break;
            }

            // converting string to byte array
            buffer = input.getBytes();

            // Sending packet to server for calculation
            sendPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 5000);
            socket.send(sendPacket);


            // receiving packet with answer to calculation
            buffer = new byte[65535];
            receivedPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivedPacket);

            System.out.println("Ans: " + new String(buffer, 0, buffer.length));
        }
    }
}
