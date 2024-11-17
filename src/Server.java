import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    static ExecutorService executeIt = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(3345);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Server socket created, command console reader for listen to server commands");

            while (!server.isClosed()) {

                if (br.ready()) {
                    System.out.println("Main Server found any messages in channel, let's look at them.");

                    String serverCommand = br.readLine();
                    if (serverCommand.equalsIgnoreCase("quit")) {
                        System.out.println("Main Server initiate exiting...");
                        server.close();
                        break;
                    }
                }

                System.out.println("Server socket listening on port 3345");
                Socket client = server.accept();
                System.out.println("Client connected");

                executeIt.execute(new MonoThreadHandler(client));
                System.out.println("Connection accepted.");
            }

            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}