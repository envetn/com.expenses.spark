package com.expenses.sparkexample.services.Socket;

import com.expenses.sparkexample.services.Connections.RegisteredConnection;
import com.expenses.sparkexample.services.Connections.Services;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.expenses.sparkexample.services.Utilities.createJsonHeader;

public class SocketConnect
{
    private static final Logger logger = Logger.getLogger(SocketConnect.class);

    private String myRequest;
    private final CertificateService myCertificateService;
    private final static boolean IS_SECURE = false;

    public SocketConnect(String request)
    {
        myRequest = request;
        myCertificateService = new CertificateService();
    }

    public String sendToServer()
    {
        String response;
        try
        {
            logger.info("Executing supplyAsync..");
            CompletableFuture<String> receiver = CompletableFuture.supplyAsync(this::executeRequest);
            response = receiver.get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            logger.info("Failed to contact server: " + e.toString());
            response = createJsonHeader("Failed", e.getMessage());
        }

        return response;
    }

    private String executeRequest()
    {
        myRequest += System.getProperty("line.separator");
        String response = null;
        RegisteredConnection.printRegisteredServices();
        Services service = RegisteredConnection.getFirstAvailableService();

        if (IS_SECURE)
        {
            response = trySecure();
        }

        if (response == null)
        {
            if (service != null)
            {
                try (Socket socket = new Socket(service.getIp(), service.getPort()))
                {
                    logger.info("Trying :" + service.getIp() + "....");

                    PrintWriter printWriterOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                    BufferedReader bufferReaderIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    printWriterOut.write(myRequest);
                    printWriterOut.flush();

                    response = bufferReaderIn.readLine();
                    bufferReaderIn.close();
                    printWriterOut.close();

                }
                catch (IOException e)
                {
                    logger.info("Failed to contact server: " + e.toString());
                    response = createJsonHeader("Failed", e.getMessage());
                }
            }
            else
            {
                logger.info("No avaliable services");
                response = createJsonHeader("Failed", "No avaliable services");
            }
        }

        return response;
    }

    private String trySecure()
    {
        String response = "";

        try
        {
            Services service = RegisteredConnection.getFirstAvailableService();

            if (service != null)
            {

                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(myCertificateService.getKeyManagers(), myCertificateService.getTrustManagers(), null);
                SSLSocketFactory socketFactory = sslContext.getSocketFactory();
                SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(service.getIp(), service.getPort());


                PrintWriter printWriterOut = new PrintWriter(sslSocket.getOutputStream(), true);
                BufferedReader bufferReaderIn = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

                printWriterOut.write(myRequest);
                printWriterOut.flush();

                response = bufferReaderIn.readLine();
                bufferReaderIn.close();
                printWriterOut.close();
            }

        }
        catch (NoSuchAlgorithmException | KeyManagementException | IOException e)
        {
            logger.info("Failed with Secure :", e);
        }

        return response;
    }



}
