import java.io.*;
import java.net.*;
import java.util.*;


/*SUB CLIENT accesses groups created by the SUPER CLIENT and can start receiving timed messages from
 * the group chosen
*/
public class SubClient 
{
    public static void main(String[] args) throws IOException
    {
        System.setProperty("java.net.preferIPv4Stack", "true");         
        Scanner input = new Scanner(System.in);
        int choice;
        String address = null;
        String clientGreeteServer = "Hello Server, I thank you for the connection. Signed: Sub Client";

        DatagramSocket datagramSocket = new DatagramSocket(); //creating a datagram socket to send and receive messages from the server
        byte[] buffer;  
        String groupName;        

        //sending the server an initial message such that it can obtain the Sub Client's IP address and port number
        buffer = clientGreeteServer.getBytes();
        InetAddress inetAddress1 =  InetAddress.getByName("localhost"); //obtaining the IP address of the server
        DatagramPacket Packet1 = new DatagramPacket(buffer, buffer.length, inetAddress1, 2222); //creating a datagram packet to send the message to the server
        datagramSocket.send(Packet1); //sending the message to the server


       // new DatagramPacket(buffer, buffer.length); //
       

        ArrayList<String> groups = new ArrayList<>();//creating an arraylist to store the group names

        for(int i = 0; i < 5; i++)  
        {
            //receiving the group names from the server
            byte[] bufferx = new byte[1000];
            DatagramPacket d = new DatagramPacket(bufferx, bufferx.length);
            datagramSocket.receive(d);            
            groupName = new String(d.getData(), 0, d.getLength());
            groups.add(groupName); //adding the group names to the arraylist
        }

        do 
        {
            System.out.println();
            System.out.println("Choose the group you want to join. TYPE ITS NUMBER.");
            System.out.println();

            int x = 0, y = 1;

            for (String ignored : groups)
            {
                System.out.print((y++) + " " + groups.get(x++));
                System.out.println();
            }
            System.out.print("Choice : " );//taking the choice from the user
            choice = input.nextInt();
            
        } while (choice < 1 || choice > 5 ); //checking if the choice is valid

       // giving out multicast group addresses depending on the group chosen
        if(choice == 1)
        address = "225.6.7.8";  
        else if(choice == 2)
        address = "226.6.7.8";
        else if(choice == 3)
        address = "227.6.7.8";
        else if(choice == 4)
        address = "228.6.7.8";
        else if(choice == 5)
        address = "229.6.7.8";
        else
        System.out.println("Invalid input.");

        try 
        {
            InetAddress.getByName(address); //obtaining the IP address of the multicast group

            MulticastSocket mSocket = new MulticastSocket(6789); //creating a multicast socket to receive data from the Server
            new InetSocketAddress(address, 2222); //creating a new InetSocketAddress to send the message to the server
            NetworkInterface netIf = NetworkInterface.getByName("bge0"); //obtaining the network interface of the server
            mSocket.joinGroup(new InetSocketAddress(address, 0), netIf); //joining the multicast group on the network interface of the server
            byte[] b = (Integer.toString(choice)).getBytes();
            DatagramPacket p = new DatagramPacket(b, b.length, inetAddress1, 2222);//creating a datagram packet to send the group number to the server
            datagramSocket.send(p);//sending the group number to the server
            System.out.println("You have joined the group called " + groups.get(choice - 1));
            System.out.println("Waiting for messages...");

            while (true)
            {
                byte[] bufferx = new byte[1000];
                DatagramPacket d = new DatagramPacket(bufferx, bufferx.length);//creating a datagram packet to store the data received from the server
                mSocket.receive(d); //receiving the data from the server
                String data = new String(d.getData(), 0, d.getLength());
                System.out.println(data);
            }
                
        } catch (Exception e) { e.printStackTrace(); }
    }
}
    
