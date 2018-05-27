
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Server {

    static String ipAdress = "127.0.0.1";
    static int port = 6666;

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;

    private static List<ClientThread> allClientThreads;

    public static void main(String[] args) {

        try {

            allClientThreads = new ArrayList<>();


            serverSocket = new ServerSocket(port);


            while (true) {

                clientSocket = serverSocket.accept();

                ClientThread newClient = new ClientThread(clientSocket, allClientThreads);

                allClientThreads.add(newClient);

                newClient.start();

                System.out.println("New client....");
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}


class ClientThread extends Thread {

    private PrintWriter out;
    private BufferedReader in;
    final private Socket clientSocket;
    private String clientName;
    private List<ClientThread> allClientThreads;

    ClientThread(Socket clientSocket, List<ClientThread> allClientThreads) {

        this.clientSocket = clientSocket;
        this.allClientThreads = allClientThreads;

        try {
            in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            out = new PrintWriter(this.clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {

        try {

            while (true) {
                out.println("Enter your name > ");
                out.flush();
                clientName = in.readLine().trim();


                if (!clientName.equals("")) {
                    out.println("Hello! We are glad to see you in our chat!");
                    out.flush();
                    break;
                }

                out.print("Impossible name. Try one more time! ");
            }

            for (ClientThread clientThread : allClientThreads) {
                if (clientThread != this) {
                    PrintWriter clOut = clientThread.getOut();
                    clOut.println(clientName + " join to the chat!");
                    clOut.flush();
                }
            }


            while (true) {

                String msg = in.readLine();


                for (ClientThread clientThread : allClientThreads) {
                    if (clientThread != this) {
                        PrintWriter clOut = clientThread.getOut();
                        clOut.println(clientName + ": " + msg);
                        clOut.flush();
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getOut() {
        return out;
    }
}


