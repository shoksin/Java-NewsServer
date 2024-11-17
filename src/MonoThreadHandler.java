import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;

public class MonoThreadHandler implements Runnable {

    private static Socket clientDialog;

    public MonoThreadHandler(Socket client) {
        MonoThreadHandler.clientDialog = client;
    }

    @Override
    public void run() {

        try {
            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            System.out.println("Client: " + clientDialog.toString());

            DataInputStream in = new DataInputStream(clientDialog.getInputStream());
            System.out.println("DataInputStream created");

            System.out.println("DataOutputStream  created");

            while (!clientDialog.isClosed()) {
                System.out.println("Server reading from channel");

                String date = in.readUTF();

                System.out.println("READ from clientDialog message - " + date);

                if (date.equalsIgnoreCase("quit")) {

                    System.out.println("Client initialize connections suicide ...");
                    out.writeUTF("Server reply - " + date + " - OK");
                    Thread.sleep(3000);
                    break;
                }

                List<News> news = NewsReader.readNewsFromFile("./src/news.txt",date);

                System.out.println("Server try writing to channel");
                out.writeUTF("Server reply - news: " + news + " - OK");
                System.out.println("Server Wrote message to clientDialog.");


                out.flush();

            }

            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            in.close();
            out.close();

            clientDialog.close();

            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}