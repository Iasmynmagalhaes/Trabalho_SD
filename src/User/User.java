package User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.Time;
import java.util.Scanner;

import proto.LivroOuterClass.Livro;
import proto.LivroOuterClass.Livros;

public class User {

	static Proxy proxy = new Proxy();
	static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));


	public static void main(String[] args) throws IOException {
		String op = "";
		PrintStream stdout = System.out;

		while(!op.equals("0")) {

			stdout.print("|-----------Menu----------|\n");
			stdout.print("|   1 - Inserir Livro     |\n");
			stdout.print("|   2 - Listar Livros     |\n");
			stdout.print("|   3 - Remover Livro     |\n");
			stdout.print("|   4 - Ver Livro         |\n");
			stdout.print("|   5 - Atualizar Livro   |\n");
			stdout.print("|   0 - Sair              |\n");
			stdout.print("|-------------------------|\n");

			stdout.print("\nSua escolha: ");
			op = stdin.readLine();

			switch (op) {
			case "0":
				System.out.print("\nValeu, falou\n");
				break;
			case "1":
				addLivro();
				break;
			case "2":
				listarLivro();
				break;
			case "3":
				removeLivro();
				break;
			case "4":
				verLivro();
				break;
			case "5":
				editarLivro();
			default:
				System.out.println("\nOpção Inválida\n");
				//stdout.print("\nOpção inválida.\n");
				break;
			}

		}
	}
	
	public static void addLivro() throws IOException {
		Livro.Builder livro = Livro.newBuilder();
		//Livro.Builder livro = new Livro.Builder();

		String titulo, genero, autor, editora;
		String ano;

		System.out.println("\nAdicionando livro");
		System.out.print("Título: ");
		titulo = stdin.readLine();
		System.out.print("Gênero: ");
		genero = stdin.readLine();
		System.out.print("Autor/Autora: ");
		autor = stdin.readLine();
		System.out.print("Editora: ");
		editora = stdin.readLine();
		System.out.print("Ano: ");
		ano = stdin.readLine();

		livro.setTitulo(titulo);
		livro.setGenero(genero);
		livro.setAutor(autor);
		livro.setEditora(editora);
		livro.setAno(Integer.parseInt(ano));
		livro.setId(0);

		System.out.println("USUARIO ESTA MANDANDO: "+ livro);
		Livro buscarLivro = proxy.buscarLivroTitulo(titulo);
		if (!buscarLivro.getTitulo().equals("livro")) {
			System.out.println("\n\nEsse livro ja existe.\n\n");
		}
		else
		{

			if ( proxy.addLivro(livro.build()).equals("true")){
				System.out.println("Livro adicionado com sucesso.\n");
			}
			else {
				System.out.println("Erro ao adicionar o livro	.\n");
			}
		}

		/*if (proxy.addLivro(livro.build()).equals("true")){
			System.out.println("\nLivro adicionado com sucesso.\n");
			System.out.println("");
		}
		else {
			System.out.println("Erro ao adicionar o livro.\n");
		}*/
	}

	public static void listarLivro() throws IOException {
		Livros livros = proxy.listaLivros();
		Livro.Builder listarLivro = Livro.newBuilder();

		//System.out.println(livros.getLivrosList());

		if(livros.getLivrosList().size() == 0) {
			System.out.println("Não há livros cadastrados\n");			
		}
		else {
			int count = 1;
			for(Livro livro : livros.getLivrosList()) {

				System.out.println(count+"° Livro ==> Título: " + livro.getTitulo());
				System.out.println("");
				//System.out.println("Titulo: " + livro.getTitulo());
				//System.out.println("Genero: " + livro.getGenero());
				//System.out.println("Autor/Autora: " + livro.getAutor());
				//System.out.println("Editora: " + livro.getEditora());
				//System.out.println("Ano: " + livro.getAno());
				System.out.println("\n*******************************\n");
				count ++;
			}
		}
	}

	public static void removeLivro() {

		Livros livros = proxy.listaLivros();
		System.out.println("\nLista de livros cadastrados");

		for (Livro livro: livros.getLivrosList()) {
			System.out.println("ID = "+livro.getId()+"--"+livro.getTitulo());
		}

		int id;
		Scanner sc = new Scanner(System.in);
		System.out.print("ID do livro que será deletado: ");
		id = sc.nextInt();
		//sc.nextLine();

		boolean idNaoEncontrado = true;
		for (Livro livro : livros.getLivrosList()) {
			if (id == livro.getId()) {
				idNaoEncontrado = false;
			}
		}

		if (idNaoEncontrado) {
			System.out.println("\nO ID que você passou não existe\n");
		}
		else {
			if (proxy.removeLivro(id).equals("true")){
				System.out.println("Livro removido.\n");
			}
			else {
				System.out.println("Erro ao remover o livro.\n");
			}
		}
	}

	public static void verLivro() {

		Livros livros = proxy.listaLivros();
		System.out.println("Livros cadastrados");


		for(Livro livro : livros.getLivrosList()) {
			System.out.println("Título: "+ livro.getTitulo()+" -- ID: "+ livro.getId());
		}

		System.out.println("\nDetalhando livro pelo ID");
		int id;
		Scanner sc = new Scanner(System.in);
		System.out.print("ID do livro: ");
		id = sc.nextInt();
		sc.nextLine();

		boolean idNaoEncontrado = true;

		for(Livro livro : livros.getLivrosList()) {
			if(id == livro.getId()) {
				idNaoEncontrado = false;
			}

		}

		if(idNaoEncontrado) {
			System.out.print("ID não encontrado.\n");

			return;
		}else {
			Livro listar = proxy.verLivro(id);

			System.out.println("Titulo: "+listar.getTitulo());
			System.out.println("Gênero: "+listar.getGenero());
			System.out.println("Editora: "+listar.getEditora());
			System.out.println("Autor: "+listar.getAutor());
			System.out.println("Ano: "+listar.getAno());
			System.out.println("********************************");
		}

	}
	
	public static void editarLivro() throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("--- Atualizar livro ---");
		
		int id;
		String titulo, genero, editora, autor, ano;

		Livros livros = proxy.listaLivros();
		System.out.println("Livros cadastrados");
		for(Livro livro : livros.getLivrosList()) {
			System.out.println("Título: "+ livro.getTitulo()+" -- ID: "+ livro.getId());
		}

		System.out.print("\nDigite o ID do livro que será editado: ");
		id = sc.nextInt();

		boolean idNaoEcontrado = true;
		for(Livro livro : livros.getLivrosList()) {
			if(id == livro.getId()) {
				idNaoEcontrado = false;
			}
		}
		if(idNaoEcontrado) {
			System.out.println("ID não encontrado");
		}else {
			Livro alterar = null;
			for(Livro livro: livros.getLivrosList()) {
				if(livro.getId() == id){
					alterar = livro;
					break;
				}
			}
			
			//Criar novo livro com o livro escolhido
			Livro.Builder alterado = Livro.newBuilder();
		
			alterado.setTitulo(alterar.getTitulo());
			alterado.setGenero(alterar.getGenero());
			alterado.setAutor(alterar.getAutor());
			alterado.setEditora(alterar.getEditora());
			alterado.setAno(alterar.getAno());
			alterado.setId(alterar.getId());
			
			System.out.print("Alterar título: ");
			titulo = stdin.readLine();
			System.out.print("Alterar gênero: ");
			genero = stdin.readLine();
			System.out.print("Alterar autor/autora: ");
			autor = stdin.readLine();
			System.out.print("Alterar editora: ");
			editora = stdin.readLine();
			System.out.print("Ano: ");
			ano = stdin.readLine();

			alterado.setTitulo(titulo);
			alterado.setGenero(genero);
			alterado.setAutor(autor);
			alterado.setEditora(editora);
			alterado.setAno(Integer.parseInt(ano));
			
			System.out.println("Usuario mandando: " + alterado);
			if (proxy.editarLivro(alterado.build()).equals("true")){
				System.out.println("Livro editado com sucesso.\n");
			}
			else {
				System.out.println("Erro ao editar Livro.\n");
			}
		}	
	}
}


