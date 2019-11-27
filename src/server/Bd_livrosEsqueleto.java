package server;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import BD.Bd_livros;
import proto.LivroOuterClass.Livro;
import proto.LivroOuterClass.Livros;

public class Bd_livrosEsqueleto {

	//Criando instância do banco
	Bd_livros livros = new Bd_livros();

	public ByteString addLivro(ByteString msg) {
		Livro livro = null;
		try {

			livro = Livro.parseFrom(msg);
		} 
		catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		String resposta = livros.addLivro(livro);
		System.out.println("Esqueleto recebendo: "+ resposta);
		ByteString res = ByteString.copyFromUtf8(resposta);

		return res;
	}

	public ByteString listarLivros(ByteString msg) throws InvalidProtocolBufferException{
		//Livro op = null;
		//op = Livro.parseFrom(msg);
		//System.out.println(op.getTitulo());


		Livros livrosB = livros.listarLivros();

		return livrosB.toByteString();

	}

	public ByteString removeLivro(ByteString msg) {
		Livro livro = null;
		try {
			livro = Livro.parseFrom(msg);
		} 
		catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		String resposta = livros.removeLivro(livro.getId());

		ByteString res = ByteString.copyFromUtf8(resposta);

		return res;
	}

	public ByteString verLivro(ByteString msg){
		Livro livro = null;
		try {
			livro = Livro.parseFrom(msg);
		} 
		catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		Livro resposta = livros.verLivro(livro.getId());

		return resposta.toByteString();
	}
	
	public ByteString buscarLivroTitulo(ByteString msg) {

		Livro livro = null;
		try {
			livro = Livro.parseFrom(msg);
		} 
		catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		Livro resposta = livros.buscarLivroTitulo(livro.getTitulo());

		return resposta.toByteString();
	}
	
	public ByteString editarLivro(ByteString msg) {
		Livro livro = null;
		try {
			livro = Livro.parseFrom(msg);
		} 
		catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		String resposta = livros.editarLivro(livro);
		System.out.println("Esqueleto recebendo: "+ resposta);
		ByteString res = ByteString.copyFromUtf8(resposta);

		return res;
	}

}



