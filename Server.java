import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.net.*;

/*SERVER coordinates the flow of messages between the SUPER CLIENT and the SUB CLIENTS */

public class Server 
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
       
    
        System.setProperty("java.net.preferIPv4Stack", "true");  

       

        System.out.println("SERVER IS WAITING FOR SUPER CLIENT TO CONNECT AND IT RECEIVES GROUPS CREATED....");
        System.out.println();
        System.out.println("-----------THESE ARE THE GROUPS CREATED BY THE SUPER CLIENT----------");
        System.out.println();
        System.out.println("----------------------Loading, please wait---------------------");
        System.out.println();

         

         byte[] buffer = new byte[1000];
         byte[] buffer1 = new byte[1000];
        
         ArrayList<String> groups = new ArrayList<>(); //creating an arraylist to store the groups that the client creates
         DatagramSocket  Socket1 = new DatagramSocket(2222);

            while(true)
            {
                try 
                {

                        DatagramPacket Packet1 = new DatagramPacket(buffer, buffer.length); //creating a datagram packet to store the data received from the client

                        Socket1.receive(Packet1); //receiving the group name from the client

                        String group = new String(Packet1.getData(), 0, Packet1.getLength());

                        groups.add(group);
                        System.out.println(group);
                      
                         if(groups.size() == 5) //if the client sends 5 groups, the server will stop receiving messages from the client
                        {
                            break;
                        }  
                    
                    } catch (Exception e) {e.printStackTrace();  break;}
           
                }

                System.out.println();                      
                DatagramPacket datagramPacket = new DatagramPacket(buffer1, buffer1.length); //creating a datagram packet to store the time received from the Super client
                Socket1.receive(datagramPacket); //receiving the time from the Super client             
                String messageFromClient = new String(datagramPacket.getData(), 0, datagramPacket.getLength()); //storing the time received from the Super client in a string


            System.out.println();
            System.out.println("Time interval between sending of messages  : " + messageFromClient);
            System.out.println();
            
            
            System.out.println();
            System.out.println("GROUPS RECEIVED! NOW WAITING FOR SUB CLIENT TO CHOOSE A GROUP");
            System.out.println();
            
            
                byte[] databuffer = new byte[1000];//creating a buffer to be used to extract the data (address and port) from the datagram packet received from the Sub client
                datagramPacket = new DatagramPacket(databuffer, databuffer.length);
                Socket1.receive(datagramPacket);
                InetAddress clientAdress = datagramPacket.getAddress();//getting the Sub client's address
                int clientPort = datagramPacket.getPort();//getting the port number of the Sub client

                int i = 0;
                for(i = 0; i < 5; i++)
                {
                    byte[] clientbuffer = new byte[1000];

                    clientbuffer = groups.get(i).getBytes();

                    datagramPacket = new DatagramPacket(clientbuffer, clientbuffer.length, clientAdress, clientPort);

                    Socket1.send(datagramPacket);//sending the groups to the client, to receive group choice
                }
         
                byte bi[] = new byte[256];//creating a buffer to store the client group of choice
                DatagramPacket clientChoice = new DatagramPacket(bi, bi.length);
                Socket1.receive(clientChoice);//receiving the client's group of choice
                String groupChosenString = new String(clientChoice.getData(), 0, clientChoice.getLength()); //converting the group of choice to a string

                int groupChosen = Integer.parseInt(groupChosenString); 
                InetAddress address = null;
                String message = null; 

                if(groupChosen == 1)
                {
                    address = InetAddress.getByName("225.6.7.8");
                    message = "If you see this message 5 times, then this group is working fine";

                }
                else if(groupChosen == 2)
                {
                    address = InetAddress.getByName("226.6.7.8"); 
                    message = "If you see this message 5 times, then this group is working fine";

                }
                else if(groupChosen == 3)
                {
                    address = InetAddress.getByName("227.6.7.8"); 
                    message = "If you see this message 5 times, then this group is working fine";

                }
                else if (groupChosen == 4)
                {
                    address = InetAddress.getByName("228.6.7.8"); 
                    message = "If you see this message 5 times, then this group is working fine";

                }
                else if (groupChosen == 5)
                {
                    address = InetAddress.getByName("229.6.7.8"); 
                    message = "If you see this message 5 times, then this group is working fine";

                }

                System.out.println();
                System.out.println("OPENING MULTICAST SOCKET.......");
                System.out.println();

                MulticastSocket socket = new MulticastSocket(); //creating a multicast socket to send the message(s) to the connected Sub client

                byte[] messagebuffer = message.getBytes();
                DatagramPacket datagramPacketm = new DatagramPacket(messagebuffer, messagebuffer.length, address, 6789);

                for(int j = 0; j < 5; j++)
                {
                    socket.send(datagramPacketm);  //sending the message to the Sub client                          
                    Thread.sleep((Integer.parseInt(messageFromClient))*1000); //sending the message to the Sub client every time, using the time interval specified by the Super client
                }
                        
    }
}
