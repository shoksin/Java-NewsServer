import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Client {

    public static void main(String[] args) throws InterruptedException {

        boolean isAdmin = false;
        try(Socket socket = new Socket("localhost", 3345);
            BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            DataInputStream ois = new DataInputStream(socket.getInputStream()); )
        {
            System.out.println("Client connected to socket.");
            System.out.println("Command:\ndd.mm.yyyy - Get News for dd.mm.yyyy\nadd_news - Add News");

            while(!socket.isOutputShutdown()){

                if(br.ready()){

                    System.out.println("Client start writing in channel...");
                    Thread.sleep(1000);
                    String clientCommand = br.readLine();

                    if(clientCommand.equals("add_news")){
                        if(!isAdmin) {
                            System.out.println("Enter password:");
                            String password = br.readLine();
                            if (password.equals("admin")){
                                isAdmin = true;
                            } else {
                                System.out.println("Wrong password");
                                continue;
                            }
                        }

                        System.out.println("Enter title:");
                        String title = br.readLine();
                        System.out.println("Enter author:");
                        String author = br.readLine();
                        System.out.println("Enter content:");
                        String content = "";
                        String temp = "";
                        while(!(temp = br.readLine()).equals("end")){
                            content += temp + " ";
                            System.out.println(content);
                        }

                        LocalDate today = LocalDate.now();

                        int year = today.getYear();
                        int month = today.getMonthValue();
                        int day = today.getDayOfMonth();
                        String date = day + "." + month + "." + year;
                        News news = new News(date, title, author, content);
                        oos.writeUTF(news.toString2());
                        oos.flush();


                    }
                    else {
                        if (!isValidDate(clientCommand)) {
                            System.out.println("Invalid date format. Please use dd.mm.yyyy.");
                            continue;
                        }
                        oos.writeUTF(clientCommand);
                        oos.flush();
                    }

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

    private static boolean isValidDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}