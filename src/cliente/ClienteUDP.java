package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClienteUDP {

	DatagramSocket aSocket;	
	int serverPort = 6789;
	
	DatagramPacket request = null;
	
	public void sendRequest(byte[] message) throws UnknownHostException {
		InetAddress aHost = InetAddress.getByName("localhost");
		request = new DatagramPacket(message, message.length, aHost, serverPort);
		try {
			aSocket = new DatagramSocket();
			aSocket.setSoTimeout(2000);
			aSocket.send(request);
			//aSocket.send(request); //testando duplicidade
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] getResponse(){
		byte[] buffer = new byte[1000];
		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
		int count = 3;
		while (count >= 0) {
		try{
			aSocket.receive(reply);
			aSocket.close();
			if(aSocket.isClosed()) {count = -1;}
		}
		catch (IOException e) {
			e.printStackTrace();
			if (count > 0) { 
				System.out.println("\n\nERROR:Reenviando dados...");
			}
			if (count == 0) {
				System.out.println("\n\nERROR: Erro na conexão");
			}
			try {
				aSocket.send(request);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
		}
		count --;
		}
		return reply.getData();
	}
}