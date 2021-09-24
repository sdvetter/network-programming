package Oblig1.Oppg1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 2000);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        Scanner s = new Scanner(System.in);

        System.out.println("1: Addition");
        System.out.println("2: Subtraction");
        System.out.println("3: Quit");
        int choice = s.nextInt();
        out.println(choice);

        int a = 0;
        int b = 0;
        int rep = 0;

        while (choice == 1 || choice == 2){
            switch(choice){
                case 1:
                    System.out.println("Enter numbers you want to add");
                    a = s.nextInt();
                    s.nextLine();
                    b = s.nextInt();
                    out.println(a);
                    out.println(b);
                    rep = Integer.parseInt(in.readLine());
                    System.out.println("sum of numbers from server: " + rep);
                    break;
                case 2:
                    System.out.println("Enter numbers you want to subtract");
                    a = s.nextInt();
                    s.nextLine();
                    b = s.nextInt();
                    out.println(a);
                    out.println(b);
                    rep = Integer.parseInt(in.readLine());
                    System.out.println("subtraction of numbers from server: " + rep);
                    break;
                default:
                    System.out.println("snagges");
            }
            System.out.println("Enter 1 for addition, 2 for subtraction");
            choice = s.nextInt();
            out.println(choice);
        }
    }
}
