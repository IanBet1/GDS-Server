package Server;

import Controller.ProdutoDAO;
import Controller.UsuarioDAO;
import Model.Produto;
import Model.ProdutoDados;
import Model.Relatorio;
import Model.Usuario;
import java.net.*;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiServerThread extends Thread {

    private Socket socket = null;
    private int requisicaoClient = -1;
    private final ProdutoDAO produtoDAO;
    private final UsuarioDAO usuarioDAO;

    public MultiServerThread(Socket socket) {
        super("MultiServerThread");
        this.socket = socket;
        this.produtoDAO = new ProdutoDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    public void run() {

        try {
            DataOutputStream outFromServer = new DataOutputStream(socket.getOutputStream());
            DataInputStream inFromClient1 = new DataInputStream(socket.getInputStream());
            this.requisicaoClient = inFromClient1.readInt();

            //Determinar o tipo de Objeto e depois o tipo de solicitação
            switch (this.requisicaoClient) {
                case 0: {
                    int contagem = this.produtoDAO.contagemProdutos();
                    outFromServer.writeUTF(String.valueOf(contagem));
                }
                break;
                case 1: {
                    outFromServer.writeBoolean(true);
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    Object o = inFromClient2.readObject();
                    this.produtoDAO.adicionarProduto((Produto) o);
                    outFromServer.writeUTF(this.produtoDAO.getMsg());
                }
                break;
                case 2: {
                    outFromServer.writeBoolean(true);
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    Object o = inFromClient2.readObject();
                    this.usuarioDAO.adicionarUsuario((Usuario) o);
                    outFromServer.writeUTF(this.usuarioDAO.getMsg());
                }
                break;
                case 3: {
                    outFromServer.writeBoolean(true);
                    Usuario validado = null;
                    ObjectOutputStream outFromServer2 = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    Object o = inFromClient2.readObject();
                    validado = this.usuarioDAO.validarUsuario((Usuario) o);
                    outFromServer.writeUTF(this.usuarioDAO.getMsg());
                    outFromServer2.writeObject(validado);
                }
                break;
                case 4: {
                    outFromServer.writeBoolean(true);
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    Object o = inFromClient2.readObject();
                    this.usuarioDAO.deslogarUsuario((Usuario) o);
                    outFromServer.writeUTF(this.usuarioDAO.getMsg());
                }
                break;
                case 5: {
                    outFromServer.writeBoolean(true);
                    ObjectOutputStream outFromServer2 = new ObjectOutputStream(socket.getOutputStream());
                    outFromServer2.writeObject(this.produtoDAO.listagemProdutos());
                    outFromServer.writeUTF(this.produtoDAO.getMsg());
                }
                break;
                case 6: {
                    int contagem = this.produtoDAO.contagemProdutoDados();
                    outFromServer.writeUTF(String.valueOf(contagem));
                }
                break;
                case 7: {
                    outFromServer.writeBoolean(true);
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    Object o = inFromClient2.readObject();
                    outFromServer.writeBoolean(this.produtoDAO.validarProdutoDados((ProdutoDados) o));
                    outFromServer.writeUTF(this.produtoDAO.getMsg());
                }
                break;
                case 8: {
                    outFromServer.writeBoolean(true);
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    ProdutoDados pd = null;
                    pd = (ProdutoDados) inFromClient2.readObject();
                    this.produtoDAO.adicionarProdutoDadosInexistente(pd);
                    outFromServer.writeUTF(this.produtoDAO.getMsg());
                }
                break;
                case 9: {
                    outFromServer.writeBoolean(true);
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    ProdutoDados pd = null;
                    Usuario u = null;
                    pd = (ProdutoDados) inFromClient2.readObject();
                    u = (Usuario) inFromClient2.readObject();
                    this.produtoDAO.adicionarProdutoDadosExistente(pd, u);
                    outFromServer.writeUTF(this.produtoDAO.getMsg());
                }
                break;
                case 10: {
                    outFromServer.writeBoolean(true);
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    ProdutoDados pd = null;
                    Usuario u = null;
                    pd = (ProdutoDados) inFromClient2.readObject();
                    u = (Usuario) inFromClient2.readObject();
                    this.produtoDAO.removerProdutoDadosExistente(pd, u);
                    outFromServer.writeUTF(this.produtoDAO.getMsg());
                }
                break;
                case 11: {
                    outFromServer.writeBoolean(true);
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    ProdutoDados pd = null;
                    pd = (ProdutoDados) inFromClient2.readObject();
                    outFromServer.writeInt(this.produtoDAO.qtdDisp(pd));
                    outFromServer.writeUTF(this.produtoDAO.getMsg());
                }
                break;
                case 12: {
                    outFromServer.writeBoolean(true);
                    ObjectOutputStream outFromServer2 = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    Produto p = null;
                    p = (Produto) inFromClient2.readObject();
                    List<ProdutoDados> lista = this.produtoDAO.listagemProdutosValidade(p);
                    outFromServer2.writeObject(lista);
                    outFromServer.writeUTF(this.produtoDAO.getMsg());
                    outFromServer2.close();
                    inFromClient2.close();
                }
                case 13: {
                    outFromServer.writeBoolean(true);
                    ObjectOutputStream outFromServer2 = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    Relatorio r = null;
                    r = (Relatorio) inFromClient2.readObject();
                    outFromServer2.writeObject(this.produtoDAO.relatorioSaida(r.getDate1(), r.getDate2()));
                    outFromServer.writeUTF(this.produtoDAO.getMsg());
                }
                break;
                case 14: {
                    outFromServer.writeBoolean(true);
                    ObjectOutputStream outFromServer2 = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream inFromClient2 = new ObjectInputStream(socket.getInputStream());
                    Relatorio r = null;
                    r = (Relatorio) inFromClient2.readObject();
                    outFromServer2.writeObject(this.produtoDAO.relatorioValidade(r.getDate1(), r.getDate2()));
                    outFromServer.writeUTF(this.produtoDAO.getMsg());
                }
                break;
                case 15: {
                    outFromServer.writeBoolean(true);
                    outFromServer.writeBoolean(this.produtoDAO.venceEmSete());
                    outFromServer.writeUTF(this.produtoDAO.getMsg());
                }
                break;
                default: {
                }
                break;
            }
            inFromClient1.close();
            outFromServer.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MultiServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
