package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import Message.MessageOuterClass.Message;

public class ServidorUDP {
	public static void main(String[] args) {
		DatagramSocket aSocket = null;
		Despachante despachante = new Despachante();
		try {
			aSocket = new DatagramSocket(6789);
			byte[] buffer = new byte[1000];
			int newId;
			int oldId = 0;
			byte[] pacoteRep = null;
			
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				
				newId = desempacotaRequisicao(request.getData()).getId();
				//System.out.println(desempacotaRequisicao(request.getData()).getObjReference());
				//System.out.println("novo" + newId);
				
				if (newId == oldId) {
					System.out.println("Repetição detectada");
					DatagramPacket res = new DatagramPacket(pacoteRep, pacoteRep.length, request.getAddress(), request.getPort());
					aSocket.send(res);
					
					
				}else{
					ByteString response = despachante.selecionaEqueleto(desempacotaRequisicao(request.getData()));
					//System.out.println(response);
					byte[] pacote = empacotaResposta(response.toByteArray(),desempacotaRequisicao(request.getData()).getId());
					//guarda para caso de duplicação
					pacoteRep = pacote;
					oldId = desempacotaRequisicao(request.getData()).getId();
					
					DatagramPacket res = new DatagramPacket(pacote, pacote.length, request.getAddress(), request.getPort());
					
					aSocket.send(res);
				}
			}

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}

	}

	public static Message desempacotaRequisicao(byte[] requisicao) throws IOException {
		Message mensagem = null;

		ByteArrayInputStream entrada = new ByteArrayInputStream(requisicao);

		try {
			mensagem = Message.parseDelimitedFrom(entrada);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return mensagem;
	}

	public static byte[] empacotaResposta(byte[] msg, int requestID) {
		ByteArrayOutputStream mensagem = new ByteArrayOutputStream(1024);
		try {
			Message.newBuilder().setType(1).setArguments(ByteString.copyFrom(msg)).setId(requestID).setObjReference("").setMethodId("").build()
					.writeDelimitedTo(mensagem);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mensagem.toByteArray();
	}

}
