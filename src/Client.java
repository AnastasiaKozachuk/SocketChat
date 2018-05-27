
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    static String ipAdress = "127.0.0.1";
    static int port = 6666;


    public static void main(String[] args) {

        try {
            final Socket client = new Socket(ipAdress, port);
            System.out.println("Just connected to " + ipAdress + " on port " + port);


            final PrintWriter out = new PrintWriter(client.getOutputStream());
            final BufferedReader in = new BufferedReader (new InputStreamReader (client.getInputStream()));

            final Scanner sc = new Scanner(System.in);


            Thread sendMessage  = new Thread(new Runnable() {

                String msg;

                @Override
                public void run() {

                    while (true) {
                        msg = sc.nextLine();
                        out.println(msg);
                        out.flush();
                    }

                }
            });
            sendMessage.start();

            Thread receiveMessage = new Thread(new Runnable() {

                String msg;

                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while (!msg.contains("null")) {

                            if(msg.contains(">")){
                                System.out.print(msg);
                            }else{
                                System.out.println(msg);
                            }


                            msg = in.readLine();
                        }

                        sc.close();
                        out.close();
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiveMessage.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


