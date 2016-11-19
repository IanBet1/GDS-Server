package Controller;

import Model.Produto;
import Model.ProdutoDados;
import Model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProdutoDAO {

    private Connection con;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public ProdutoDAO() {
        this.con = ConexaoMySQL.getConnection();
    }

    public int contagemProdutos() {
        int contagem = 0;
        String sql = "SELECT COUNT(*) FROM produto";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            ResultSet rs = stmte.executeQuery();
            rs.first();

            contagem = rs.getInt("count(*)");

            return contagem;
        } catch (Exception e) {
            this.msg = "Impossível realizar contagem. Erro: " + e.getMessage();
            return 0;
        }
    }

    public void adicionarProduto(Produto p) {
        String sql = "INSERT INTO produto VALUES(?, ?, ?)";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setInt(1, p.getId());
            stmte.setString(2, p.getDescricao());
            stmte.setInt(3, p.getMatricula().getMatricula());
            stmte.execute();
            this.msg = "Produto inserido com sucesso!";
        } catch (Exception e) {
            this.msg = "Erro ao inserir produto: " + e.getMessage();
        }
    }

    public List<Produto> listagemProdutos() {
        String sql = "SELECT * FROM produto";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            ResultSet rs = stmte.executeQuery();
            List<Produto> lista = new ArrayList();
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id_produto"));
                p.setDescricao(rs.getString("descricao"));
                p.setMatricula(null);
                lista.add(p);
            }
            this.msg = "Produtos listados com sucesso!";
            return lista;
        } catch (Exception e) {
            this.msg = "Erro ao listar produtos: " + e.getMessage();
            return null;
        }
    }

    public int contagemProdutoDados() {
        int contagem = 0;
        String sql = "SELECT COUNT(*) FROM produto_dados";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            ResultSet rs = stmte.executeQuery();
            rs.first();

            contagem = rs.getInt("count(*)");

            this.msg = "Dados de produtos contados com sucesso!";
            return contagem;
        } catch (Exception e) {
            this.msg = "Impossível realizar contagem. Erro: " + e.getMessage();
            return 0;
        }
    }

    public boolean validarProdutoDados(ProdutoDados pd) {
        boolean validacao = false;
        int contagem = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(pd.getDatavalidade());
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH) + 1;
        int ano = cal.get(Calendar.YEAR);
        String data = ano + "-" + mes + "-" + dia;

        String sql = "SELECT COUNT(*) FROM produto_dados WHERE data_validade = ? AND produto_id_produto = ?";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setString(1, data);
            stmte.setInt(2, pd.getIdproduto().getId());
            ResultSet rs = stmte.executeQuery();
            rs.first();

            contagem = rs.getInt("count(*)");
            if (contagem == 0) {
                validacao = false;
                this.msg = "Dados de produtos inexistentes!";
            } else {
                validacao = true;
                this.msg = "Dados de produtos validados com sucesso!";
            }

            return validacao;
        } catch (Exception e) {
            this.msg = "Impossível realizar contagem. Erro: " + e.getMessage();
            return false;
        }
    }

    public void adicionarProdutoDadosInexistente(ProdutoDados pd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(pd.getDatavalidade());
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH) + 1;
        int ano = cal.get(Calendar.YEAR);
        String data = ano + "-" + mes + "-" + dia;

        String sql = "INSERT INTO produto_dados VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setInt(1, contagemProdutoDados());
            stmte.setInt(2, pd.getQuantidade());
            stmte.setString(3, data);
            stmte.setInt(4, pd.getIdproduto().getId());
            stmte.execute();
            this.msg = "Unidades adicionadas com sucesso!";
        } catch (Exception e) {
            this.msg = "Erro ao inserir dados de produto: " + e.getMessage();
        }
    }

    public void adicionarProdutoDadosExistente(ProdutoDados pd, Usuario u) {
        String sql = "INSERT INTO usuario_adiciona_produto_dados VALUES(?, ?, NOW(), NOW(), ?)";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setInt(1, u.getMatricula());
            stmte.setInt(2, produtoDadosExistente(pd));
            stmte.setInt(3, pd.getQuantidade());
            stmte.execute();
            this.msg = "Unidades adicionadas com sucesso!";
        } catch (Exception e) {
            this.msg = "Erro ao inserir dados de produto: " + e.getMessage();
        }
    }

    public int produtoDadosExistente(ProdutoDados pd) {
        int idprodutodados = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(pd.getDatavalidade());
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH) + 1;
        int ano = cal.get(Calendar.YEAR);
        String data = ano + "-" + mes + "-" + dia;

        String sql = "SELECT * FROM produto_dados WHERE data_validade = ? AND produto_id_produto = ?";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setString(1, data);
            stmte.setInt(2, pd.getIdproduto().getId());
            ResultSet rs = stmte.executeQuery();
            rs.first();

            idprodutodados = rs.getInt("id_produto_dados");

            this.msg = "Id de dados de produtos obtido.";
            return idprodutodados;
        } catch (Exception e) {
            this.msg = "Impossível encontrar id com os dados atuais. Erro: " + e.getMessage();
            return 0;
        }
    }

    public void removerProdutoDadosExistente(ProdutoDados pd, Usuario u) {
        String sql = "INSERT INTO usuario_retira_produto_dados VALUES(?, ?, NOW(), NOW(), ?)";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setInt(1, u.getMatricula());
            stmte.setInt(2, produtoDadosExistente(pd));
            stmte.setInt(3, pd.getQuantidade());
            stmte.execute();
            this.msg = "Unidades removidas com sucesso!";
        } catch (Exception e) {
            this.msg = "Erro ao inserir dados de produto: " + e.getMessage();
        }
    }

    public int qtdDisp(ProdutoDados pd) {
        int qntDisp = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(pd.getDatavalidade());
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH) + 1;
        int ano = cal.get(Calendar.YEAR);
        String data = ano + "-" + mes + "-" + dia;
        
        String sql = "SELECT quantidade FROM produto_dados WHERE data_validade = ? AND produto_id_produto = ?";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setString(1, data);
            stmte.setInt(2, pd.getIdproduto().getId());
            ResultSet rs = stmte.executeQuery();
            rs.first();

            qntDisp = rs.getInt("quantidade");

            this.msg = "Contagem de produtos disponíveis realizada.";
            return qntDisp;
        } catch (Exception e) {
            this.msg = "Impossível realizar contagem de produtos disponíveis. Erro: " + e.getMessage();
            return -1;
        }
    }

    public List<ProdutoDados> listagemProdutosValidade(Produto p) {
        String sql = "SELECT * FROM produto_dados WHERE produto_id_produto = ?";
        try {
            PreparedStatement stmte = this.con.prepareStatement(sql);
            stmte.setInt(1, p.getId());
            ResultSet rs = stmte.executeQuery();
            List<ProdutoDados> lista = new ArrayList();
            while (rs.next()) {
                ProdutoDados pd = new ProdutoDados();
                pd.setIdprodutodados(rs.getInt("id_produto_dados"));
                pd.setQuantidade(rs.getInt("quantidade"));
                pd.setDatavalidade(rs.getDate("data_validade"));
                pd.setIdproduto(null);
                lista.add(pd);
            }
            this.msg = "Validades listadas com sucesso!";
            return lista;
        } catch (Exception e) {
            this.msg = "Erro ao listar validades: " + e.getMessage();
            return null;
        }
    }
}
