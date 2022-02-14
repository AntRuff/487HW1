import java.io.*;
import java.net.*;
import java.util.*;

public class ClientThread {
    
    //driver
    public static void main(String[] args) {
        //establish connection
        try (Socket socket = new Socket("127.0.0.1", 8888)){
            //write to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            //Reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //object of scanner
            Scanner sc = new Scanner(System.in);
            String line = null;

            while (!"exit".equalsIgnoreCase(line)) {
                //read from user
                line = sc.nextLine();
                //send user input to server
                out.println(line);
                out.flush();
                //display server reply
                System.out.println("Server replied " + in.readLine());
            }

            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
