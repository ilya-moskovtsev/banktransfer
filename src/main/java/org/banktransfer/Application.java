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

    private static final Bank BANK = new Bank();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int SERVER_PORT = 8000;
    public static final String PATH = "/api/transfer";

    public static void main(String[] args) throws IOException {
        init();

        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        server.createContext(PATH, (handler -> {

            String json = jsonFromPost(handler);

            TransferRequestDTO transferRequestDTO = MAPPER.readValue(json, TransferRequestDTO.class);

            boolean hasTransferred = BANK.transferMoney(transferRequestDTO);

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

    private static String jsonFromPost(HttpExchange exchange) throws IOException {
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

    private static void init() {
        User sender = new User("senderName", "senderPassport");
        User recipient = new User("recipientName", "recipientPassport");
        BANK.addUser(sender);
        BANK.addAccountToUser(sender.getPassport(), new Account(new BigDecimal(1000), "senderRequisites"));
        BANK.addUser(recipient);
        BANK.addAccountToUser(recipient.getPassport(), new Account(new BigDecimal(1000), "recipientRequisites"));
    }
}
