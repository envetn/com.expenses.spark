package com.expenses.sparkexample.services.Connections;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RegisteredConnection
{

    private static List<Services> registeredServices = new ArrayList<>();

    public static void addService(String ip, int port)
    {
        Services service = new Services(ip, port);
        if (!registeredServices.contains(service))
        {
            registeredServices.add(service);
            System.out.println("Service: " + service.getIp() + " " + service.getPort() + " registered");
        }
        else
        {
            System.out.println("services.com.expenses.sparkexample.services.Connections.Services already exists!");
        }
    }

    public static List<Services> getAllAvaliableServies()
    {
        List<Services> avaliableServices = new ArrayList<>();
        for (Services service : registeredServices)
        {
            if (service.isAvialable())
            {
                avaliableServices.add(service);
            }
        }
        return avaliableServices;
    }

    private static void validateExistingServices()
    {
        for (Services service : registeredServices)
        {
            if (isServerUp(service))
            {
                service.setAvaible();
            }
            else
            {
                // service.setBlocked();
                System.out.println("Removed service: " + service.getIp());
                registeredServices.remove(service);
            }
        }
    }

    public static Services getFirstAvailableService()
    {
        validateExistingServices();

        for (Services service : registeredServices)
        {
            if (service.isAvialable())
            {
                return service;
            }
        }

        return null; // Exception?
    }

    public static void printRegisteredServices()
    {
        for (Services service : registeredServices)
        {
            System.out.println(service.getIp());
        }
    }

    //Todo: move to socketConnect
    private static boolean isServerUp(Services services)
    {
        try (Socket socket = new Socket(services.getIp(), services.getPort()))
        {
            System.out.println("Validating " + services.getIp() + "...");
            socket.close();
            return true;
        }
        catch (IOException e)
        {
            System.out.println("Server on: " + services.getIp() + " is down");
        }
        return false;
    }
}
