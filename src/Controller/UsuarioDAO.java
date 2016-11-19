package Controller;

import Model.Usuario;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    private Connection con;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public UsuarioDAO() {
        this.con = ConexaoMySQL.getConnection();
    }

    public void adicionarUsuario(Usuario u) {
        String sql = "INSERT INTO usuario VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setInt(1, u.getMatricula());
            stmte.setString(2, u.getNome());
            stmte.setString(3, u.getSenha());
            stmte.setBoolean(4, false);
            stmte.execute();
            this.msg = "Usuário criado com sucesso!";
        } catch (Exception e) {
            this.msg = "Erro ao criar usuário: " + e.getMessage();
        }
    }

    public Usuario validarUsuario(Usuario usuario) {
        if (usuarioExiste(usuario) == true) {
            if (usuarioLogado(usuario) == true) {
                String sql = "SELECT * FROM usuario WHERE matricula = ? AND senha = ? AND logged = 0";
                String sql2 = "UPDATE usuario SET logged = 1 WHERE matricula = ? AND senha = ?";
                try {
                    PreparedStatement stmte = this.con.prepareStatement(sql);
                    PreparedStatement stmte2 = this.con.prepareStatement(sql2);
                    stmte.setInt(1, usuario.getMatricula());
                    stmte.setString(2, usuario.getSenha());
                    stmte2.setInt(1, usuario.getMatricula());
                    stmte2.setString(2, usuario.getSenha());
                    ResultSet rs = stmte.executeQuery();
                    rs.first();

                    Usuario validado = new Usuario();
                    validado.setMatricula(rs.getInt("matricula"));
                    validado.setNome(rs.getString("nome"));
                    validado.setSenha("");

                    stmte2.execute();

                    this.msg = "Usuário validado com sucesso!";
                    return validado;
                } catch (SQLException | NoSuchAlgorithmException e) {
                    this.msg = "Erro ao criar usuário: " + e.getMessage();
                    return null;
                }
            } else {
                this.msg = "Usuário já está logado!";
                return null;
            }
        } else {
            this.msg = "Usuário não encontrado.\nPor gentileza, verificar matrícula e senha digitados.";
            return null;
        }
    }

    public boolean usuarioLogado(Usuario usuario) {
        String sql = "SELECT * FROM usuario WHERE matricula = ? AND senha = ? AND logged = 1";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setInt(1, usuario.getMatricula());
            stmte.setString(2, usuario.getSenha());
            ResultSet rs = stmte.executeQuery();
            if (rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            this.msg = "Erro ao procurar usuário: " + e.getMessage();
            return false;
        }
    }

    public boolean usuarioExiste(Usuario usuario) {
        String sql = "SELECT * FROM usuario WHERE matricula = ? AND senha = ?;";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setInt(1, usuario.getMatricula());
            stmte.setString(2, usuario.getSenha());
            ResultSet rs = stmte.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            this.msg = "Erro ao procurar usuário: " + e.getMessage();
            return false;
        }
    }

    public void deslogarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET logged = 0 WHERE matricula = ?";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setInt(1, usuario.getMatricula());
            stmte.execute();
            this.msg = "Usuário deslogado com sucesso!";
        } catch (Exception e) {
            this.msg = "Erro ao deslogar usuário: " + e.getMessage();
        }
    }
}
