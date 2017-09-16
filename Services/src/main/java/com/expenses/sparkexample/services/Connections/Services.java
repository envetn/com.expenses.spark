package com.expenses.sparkexample.services.Connections;

public class Services
{
    private final int port;
    private final String ip;
    private boolean available;
    
    public Services(String ip, int port)
    {
        this.port = port;
        this.ip = ip;
        this.available = true;
    }
    
    public String getIp()
    {
        return ip;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public boolean isAvialable()
    {
        return available;
    }
    
    public void setBlocked()
    {
        available = false;
    }
    
    public void setAvaible()
    {
        available = true;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + port;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Services other = (Services) obj;
        if (ip == null)
        {
            if (other.ip != null) return false;
        }
        else if (!ip.equals(other.ip)) return false;
        if (port != other.port) return false;
        return true;
    }
}
