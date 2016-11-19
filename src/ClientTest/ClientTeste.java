/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientTest;

import Model.Produto;
import java.net.*;
import java.io.*;

/**
 *
 * @author ianbe
 */
public class ClientTeste {

    public static void main(String[] args) throws Exception {
        //Linha para pegar o objeto
        Produto produtoCriado = new Produto();
        String mensagem = null;
        int contagem = 0;

        try (Socket clientSocket = new Socket("localhost", 1094)) {
           System.out.println("Cliente solicitando contagem de produtos na porta 1094.");

            DataOutputStream outToServer1 = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());

            outToServer1.writeInt(0);
            contagem = Integer.parseInt(inFromServer.readUTF());

            System.out.println(contagem);
            outToServer1.close();
            clientSocket.close();
        } catch (Exception e) {
            mensagem = "Erro ao inserir produto: " + e.getMessage();
        }
    }
}
