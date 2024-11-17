import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) throws InterruptedException {

        try(Socket socket = new Socket("localhost", 3345);
            BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            DataInputStream ois = new DataInputStream(socket.getInputStream()); )
        {

            System.out.println(socket.toString());
            System.out.println("Client connected to socket.");
            System.out.println();
            System.out.println("Client writing channel = oos & reading channel = ois initialized.");
            System.out.println("Command:\ndd.mm.yyyy");

            while(!socket.isOutputShutdown()){

                if(br.ready()){

                    System.out.println("Client start writing in channel...");
                    Thread.sleep(1000);
                    String clientCommand = br.readLine();

                    oos.writeUTF(clientCommand);
                    oos.flush();
                    System.out.println("Message " + clientCommand + " sended to server.");
                    Thread.sleep(1000);

                    if(clientCommand.equalsIgnoreCase("quit")){

                        System.out.println("Connection killed");
                        Thread.sleep(2000);

                        if(ois.read() > -1)     {
                            System.out.println("reading...");
                            String in = ois.readUTF();
                            System.out.println(in);
                        }

                        break;
                    }

                    System.out.println("start waiting for data from server...");
                    Thread.sleep(2000);

                    System.out.println("reading...");
                    String in = ois.readUTF();
                    System.out.println(in);

                }
            }
            System.out.println("Connection closed.");

        } catch (UnknownHostException e) {
            System.out.println("Unknown host.");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to connect to the socket");
            e.printStackTrace();
        }
    }
}