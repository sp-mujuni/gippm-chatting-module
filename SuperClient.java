import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/* SUPER CLIENT is responsible for the creation of groups that SUB CLIENTS can join, and determining the time interval 
for the relay of messages in each group*/ 

public class SuperClient 
{
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); 
        System.setProperty("java.net.preferIPv4Stack", "true"); //
        int numberOfGroups = 0;   

        try 
        {
            DatagramSocket datagramSocket = new DatagramSocket();  
            InetAddress inetAddress =  InetAddress.getByName("localhost");
            System.out.println("Create 5 groups by writing their names below: ");
            System.out.println();
            
            while(true)
            {           
            String group = input.nextLine();
            byte[] groupbuffer = group.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(groupbuffer, groupbuffer.length, inetAddress, 2222);
            datagramSocket.send(datagramPacket);
            numberOfGroups++;                 
            System.out.println();
             
             if(numberOfGroups == 5)
             {
                System.out.println();
                System.out.println("That's enough! You can't create more than 5 groups.");
                System.out.println();
                break;
             }
                else
                {
                    System.out.println();
                    System.out.println("To create another group. Write its name below: ");
                    System.out.println();
                }             
            }
            
            System.out.println();
            System.out.print("After what time interval (in seconds) should the sub clients receive messages in their respective groups? :   ");            
            String timeInterval = input.nextLine();    
            byte[] buffer = timeInterval.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, 2222);
            datagramSocket.send(datagramPacket);
            datagramSocket.close();
            System.out.println("------ " + numberOfGroups + " GROUPS CREATED WITH MESSAGE RELAY TIME INTERVAL OF " + timeInterval + " SECONDS --------");
    
        } catch (Exception e)
        {
            e.printStackTrace();            
        }     
    }
}
    