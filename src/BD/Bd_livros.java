package BD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexaoBD.ConnectionBD;
import proto.LivroOuterClass.Livro;
import proto.LivroOuterClass.Livros;


public class Bd_livros {

	private Connection conexao;

	public String addLivro(Livro livro) {

		String sql = "INSERT INTO livro(titulo, genero, autor, editora, ano) VALUES (?,?,?,?,?)";
		System.out.println(sql);
		this.conexao = new ConnectionBD().getConnection();

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setString(1, livro.getTitulo());
			stmt.setString(2, livro.getGenero());
			stmt.setString(3, livro.getAutor());
			stmt.setString(4, livro.getEditora());
			//System.out.println(livro.getAno());
			stmt.setInt(5, livro.getAno());

			//executar instrução
			int retorno = stmt.executeUpdate();
			stmt.close();
			if(retorno > 0)
				return "true";
			return "false";
		}

		catch(SQLException e) {
			System.err.println(e.getMessage());
		}

		finally {
			try {
				this.conexao.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return "false";
	}
	
	public Livros listarLivros() {

		String sql = "SELECT * FROM livro";
		Livros.Builder livros = Livros.newBuilder();
		this.conexao = new ConnectionBD().getConnection();

		try {

			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet resultado = stmt.executeQuery();

			while(resultado.next()) {
				int id = resultado.getInt("id");
				String titulo = resultado.getString("titulo");
				String genero = resultado.getString("genero");
				String autor = resultado.getString("autor");
				String editora = resultado.getString("editora");
				int ano = resultado.getInt("ano");

				Livro.Builder montarLivro = Livro.newBuilder();

				montarLivro.setId(id);
				montarLivro.setTitulo(titulo);
				montarLivro.setGenero(genero);
				montarLivro.setAutor(autor);
				montarLivro.setEditora(editora);
				montarLivro.setAno(ano);

				livros.addLivros(montarLivro.build());		
			}
			stmt.close();
		}

		catch(SQLException e) {
			System.err.println(e.getMessage());
		}

		finally {
			try {
				this.conexao.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return livros.build();
	}

	public String removeLivro(int id) {

		String sql = "DELETE FROM livro where  id = ?";

		this.conexao = new ConnectionBD().getConnection();

		try {

			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);

			int resultado = stmt.executeUpdate();

			if(resultado > 0 )
				return "true";
			return "false";
		} 
		catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		finally {
			try {
				this.conexao.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "false";
	}

	public String editarLivro(Livro livro) {

		String sql = "UPDATE livro SET titulo = ?, genero = ?, autor = ?, editora = ?, ano = ? WHERE id = ?";
		System.out.println(sql);
		this.conexao = new ConnectionBD().getConnection();
		//Livro.Builder livro = Livro.newBuilder();
		try {

			PreparedStatement stmt = conexao.prepareStatement(sql);
			
			stmt.setString(1, livro.getTitulo());
			stmt.setString(2, livro.getGenero());
			stmt.setString(3, livro.getAutor());
			stmt.setString(4, livro.getEditora());
			stmt.setInt(5, livro.getAno());
			stmt.setInt(6, livro.getId());
			
			int resultado = stmt.executeUpdate();
			stmt.close();

			if(resultado > 0)
				return "true";
			return "false";


		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		finally {
			try {
				this.conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return "false";
	}
	public Livro verLivro(int id) {
		String sql = "SELECT * FROM livro WHERE id = ?";
		this.conexao = new ConnectionBD().getConnection();

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setInt(1, id);
			;

			ResultSet rs = stmt.executeQuery();
			rs.next();

			Livro.Builder livro = Livro.newBuilder();
			livro.setId(id);
			livro.setTitulo(rs.getString("titulo"));
			livro.setGenero(rs.getString("genero"));
			livro.setAutor(rs.getString("autor"));
			livro.setEditora(rs.getString("editora"));
			livro.setAno(rs.getInt("ano"));

			stmt.close();

			return livro.build();
		}

		catch(SQLException e) {
			System.err.println(e.getMessage());
		}

		finally {
			try {
				this.conexao.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public Livro buscarLivroTitulo(String titulo) {
		String sql = "SELECT * FROM livro WHERE titulo = ?";
		this.conexao = new ConnectionBD().getConnection();

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);

			stmt.setString(1, titulo);

			ResultSet rs = stmt.executeQuery();

			//rs.next();

			if (!rs.next()) {
				Livro.Builder livro = Livro.newBuilder();
				livro.setTitulo("livro");
				livro.setGenero("genero");
				livro.setEditora("editora");
				livro.setAutor("autor");
				livro.setAno(livro.getAno());
				livro.setId(0);
				return livro.build();

			}else {


				int id = Integer.parseInt(rs.getString("id"));

				Livro novo = verLivro(id);

				stmt.close();
				return novo;
			}


		}

		catch(SQLException e) {
			System.err.println(e.getMessage());
		}

		finally {
			try {
				this.conexao.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
