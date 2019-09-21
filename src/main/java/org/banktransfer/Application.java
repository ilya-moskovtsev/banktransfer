package org.banktransfer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.banktransfer.model.Account;
import org.banktransfer.model.TransferRequestDTO;
import org.banktransfer.model.User;
import org.banktransfer.service.Bank;

public class Application {
    private Bank bank;
    private ObjectMapper mapper;
    private int serverPort;
    private String path;

    public Application(int serverPort, String path) {
        this.bank = new Bank();
        this.mapper = new ObjectMapper();
        this.serverPort = serverPort;
        this.path = path;
    }

    public static void main(String[] args) throws IOException {
        new Application(8000, "/api/transfer").start();
    }

    private void start() throws IOException {
        init();
        runServer();
    }

    private void init() {
        User sender = new User("senderName", "senderPassport");
        User recipient = new User("recipientName", "recipientPassport");
        bank.addUser(sender);
        bank.addAccountToUser(sender.getPassport(), new Account(new BigDecimal(1000), "senderRequisites"));
        bank.addUser(recipient);
        bank.addAccountToUser(recipient.getPassport(), new Account(new BigDecimal(1000), "recipientRequisites"));
    }

    private void runServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext(path, (handler -> {

            String json = jsonFromPost(handler);

            TransferRequestDTO transferRequestDTO = mapper.readValue(json, TransferRequestDTO.class);

            boolean hasTransferred = bank.transferMoney(transferRequestDTO);

            String respText = hasTransferred ? "Transfer Successful" : "Transfer Failed";
            handler.sendResponseHeaders(200, respText.getBytes().length);
            OutputStream output = handler.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
            handler.close();
        }));
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private String jsonFromPost(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len;
        while ((len = is.read(buffer)) > 0) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return new String(bos.toByteArray(), StandardCharsets.UTF_8);
    }
}
