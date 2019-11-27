package User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import Message.MessageOuterClass.Message;
import cliente.ClienteUDP;
import proto.LivroOuterClass.Livro;
import proto.LivroOuterClass.Livros;

public class Proxy {

	ClienteUDP cliente = new ClienteUDP();

	int messageID;
	private ArrayList<Livro> livro; 

	//MÉTODO PARA ADICIONAR LIVROS
	public String addLivro(Livro l) {
		System.out.println("PROXY ESTA RECEBENDO: "+ l);
		ByteString args = l.toByteString();
		byte[] response = doOperation("Bd_livros", "addLivro", args);
		String resposta = new String(response);
		System.out.println("PROXY ESTA ENVIANDO: "+ resposta);
		return resposta;
	}

	//MÉTODO PARA LISTAR TODOS OS LIVROS
	public Livros listaLivros() {
		//Livro.Builder l = new Livro.Builder();
		Livro.Builder l = Livro.newBuilder();
		l.setTitulo("listarLivros");
		l.setGenero("listarLivros");
		l.setAutor("listarLivros");
		l.setEditora("listarLivros");
		l.setAno(0);
		l.setId(0);

		ByteString args = l.build().toByteString();

		byte[] response = doOperation("Bd_livros", "listarLivros", args);
		Livros  livros = null;
		try {
			livros = Livros.parseFrom(response);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return livros;
	}

	//MÉTODO PARA REMOVER LIVRO
	public String removeLivro(int id) {
		Livro.Builder l = Livro.newBuilder();

		l.setId(id);
		l.setTitulo("remove");
		l.setGenero("remove");
		l.setAutor("remove");
		l.setEditora("remove");
		l.setAno(0);

		ByteString args = l.build().toByteString();
		byte[] response = doOperation("Bd_livros","removeLivro" , args);

		String resposta = new String(response);

		return resposta;
	}

	//MÉTODO PARA LISTAR DETALHES DE UM LIVRO
	public Livro verLivro(int id) {
		Livro.Builder l = Livro.newBuilder();
		l.setId(id);
		l.setTitulo("titulo");
		l.setGenero("genero");
		l.setAutor("autor");
		l.setEditora("editora");
		l.setAno(l.getAno());
		ByteString msg = l.build().toByteString();

		byte[] response = doOperation("Bd_livros", "verLivro", msg);
		Livro resposta = null;

		try {
			resposta = Livro.parseFrom(response);
		} 
		catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		return resposta;
	}
	
	public Livro buscarLivroTitulo(String titulo) {
		Livro.Builder l = Livro.newBuilder();
		l.setId(0);
		l.setTitulo(titulo);
		l.setGenero("genero");
		l.setEditora("editora");
		l.setAutor("autor");
		l.setAno(l.getAno());
		ByteString msg = l.build().toByteString();

		byte[] response = doOperation("Bd_livros", "buscarLivroTitulo", msg);
		Livro resposta = null;

		try {
			resposta = Livro.parseFrom(response);
		} 
		catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		return resposta;
	}

	public String editarLivro(Livro alterado) {				
		ByteString args = alterado.toByteString();
		
		System.out.println("Proxy recebendo: " + alterado);
		
		byte[] response = doOperation("Bd_livros", "editarLivro", args);
		String resposta = new String(response);
		
		System.out.println("Resposta: " + resposta);
		return resposta;
	}


	public byte[] doOperation(String objectRef, String methodID, ByteString argumentos) {
		byte[] message = empacotaMensagem(objectRef, methodID, argumentos);
		try {
			cliente.sendRequest(message);
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Message messagem = desempacotaMensagem(cliente.getResponse());

		return messagem.getArguments().toByteArray();
	}

	public byte[] empacotaMensagem(String objectRef, String methodID, ByteString argumentos) {
		ByteArrayOutputStream mensagem = new ByteArrayOutputStream(1024);
		try {
			Random r = new Random();
			messageID = r.nextInt();
			Message.newBuilder().setType(0).setId(messageID).setObjReference(objectRef).setMethodId(methodID).setArguments(argumentos).build().writeDelimitedTo(mensagem);		
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return mensagem.toByteArray();
	}

	public Message desempacotaMensagem(byte[] resposta) {
		Message message = null;

		try {
			message = Message.parseDelimitedFrom(new ByteArrayInputStream(resposta));
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		return message;
	}

}
