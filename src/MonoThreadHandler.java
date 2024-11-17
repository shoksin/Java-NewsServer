import java.io.*;
import java.net.Socket;
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

                String message = in.readUTF();

                System.out.println("READ from clientDialog message - " + message);

                if (message.equalsIgnoreCase("quit")) {

                    System.out.println("Client initialize connections suicide ...");
                    out.writeUTF("Server reply - " + message + " - OK");
                    Thread.sleep(3000);
                    break;
                }

                if (message.length() > 10) {
                    System.out.println("New news - " + message);

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/news.txt", true))) {
                        writer.newLine();
                        writer.write(message);
                        System.out.println("New line added to the file.");
                    }


                    out.writeUTF("Server reply - "  + "big ball" + " - OK");
                }
                else {

                    List<News> news = NewsReader.readNewsFromFile("./src/news.txt", message);

                    System.out.println("Server try writing to channel");
                    out.writeUTF("Server reply - news: " + news + " - OK");
                    System.out.println("Server Wrote message to clientDialog.");
                }

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