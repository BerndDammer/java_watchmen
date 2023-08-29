package watchmen.networkinterfacewatch;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import wefwefwe.common;

public class NetworkInterfaceHolder
{
    private final Logger logger = Logger.getAnonymousLogger();


    private final String name;
    private byte[] mac;
    private int mtu;
    private boolean isLoopback;
    private boolean isPointToPoint;
    private boolean isUp;
    private boolean isVirtual;
    private boolean supportsMulticast;

    private Inet4Address inetAddress = null;
    private Inet4Address interfaceAddress = null;


    public NetworkInterfaceHolder(NetworkInterface ni)
    {
        name = ni.getName();
        if (common.LOG_NEW_INTERFACE)
            logger.info("New Interface : " + name + " ::: " + ni.getDisplayName());
        try
        {
            mac = ni.getHardwareAddress();
            mtu = ni.getMTU();

            isLoopback = ni.isLoopback();
            isPointToPoint = ni.isPointToPoint();
            isUp = ni.isUp();
            isVirtual = ni.isVirtual();
            supportsMulticast = ni.supportsMulticast();

        }
        catch (SocketException e)
        {
            logger.warning("exception while reading network interface values");
        }
        checkInetAddress(ni);
        checkInterfaceAddress(ni);
    }

    public void check(NetworkInterface ni)
    {
        checkInetAddress(ni);
        checkInterfaceAddress(ni);

        try
        {
            if (isLoopback != ni.isLoopback())
            {
                isLoopback = ni.isLoopback();
                logger.info("Change interface " + name + " loopback " + isLoopback);
            }
            if (isPointToPoint != ni.isPointToPoint())
            {
                isPointToPoint = ni.isPointToPoint();
                logger.info("Change interface " + name + " isPointToPoint " + isPointToPoint);
            }
            if (isUp != ni.isUp())
            {
                isUp = ni.isUp();
                logger.info("Change interface " + name + " isUp " + isUp);
            }
            if (isVirtual != ni.isVirtual())
            {
                isVirtual = ni.isVirtual();
                logger.info("Change interface " + name + " isVirtual " + isVirtual);
            }
            if (supportsMulticast != ni.supportsMulticast())
            {
                supportsMulticast = ni.supportsMulticast();
                logger.info("Change interface " + name + " supportsMulticast " + supportsMulticast);
            }
        }
        catch (SocketException e)
        {
            logger.warning("Exception while scanning network interface " + name);
        }
    }

    public boolean isGood()
    {
        boolean good = true;

        good &= mac != null && mac.length == 6;
        good &= mtu > 999 && mtu < 5982;

        good &= !isLoopback;
        good &= !isPointToPoint;
        good &= isUp;
        good &= !isVirtual;
        good &= supportsMulticast;

        good &= inetAddress != null;
        if (interfaceAddress != null)
        {
            good &= interfaceAddress.isSiteLocalAddress() || !common.REQUEST_LOCAL_IP;
        } else
            good = false;

        return good;
    }

    public static boolean isRootInterface(NetworkInterface ni)
    {
        boolean good = false;
        byte[] mac;
        try
        {
            mac = ni.getHardwareAddress();

            good &= mac != null && mac.length == 6;
            good &= ni.getParent() == null;
            good &= ni.getInetAddresses() != null;
        }
        catch (SocketException e)
        {
            return false;
        }
        return good;
    }

    // ********************************************************
    //
    // Helper
    private void checkInetAddress(NetworkInterface ni)
    {
        int countFound = 0;
        Inet4Address got = null;
        {
            Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
            if (inetAddresses != null)
            {
                // must found exact one IPv4 address
                while (inetAddresses.hasMoreElements())
                {
                    InetAddress ia = inetAddresses.nextElement();
                    if (ia != null && ia instanceof Inet4Address)
                    {
                        got = (Inet4Address) ia;
                        countFound++;
                    }
                }
                if (countFound != 1)
                {
                    got = null;
                }
            }
        }
        if (got == null ^ inetAddress == null)
        {
            inetAddress = got;
            if (inetAddress == null)
            {
                logger.info("Interface " + name + " lost inetAddress");
            } else
            {
                logger.info("Interface " + name + " new  inetAddress :  " + inetAddress);
            }
        }
        if (got != null && inetAddress != null && !got.equals(inetAddress))
        {
            inetAddress = got;
            logger.info("Interface " + name + " changed inetAddress :  " + inetAddress);
        }
    }

    private void checkInterfaceAddress(NetworkInterface ni)
    {
        int countFound = 0;
        Inet4Address got = null;

        {
            List<InterfaceAddress> interfaceAddresses = ni.getInterfaceAddresses();
            if (interfaceAddresses != null)
            {
                for (InterfaceAddress ina : interfaceAddresses)
                {
                    InetAddress ia = ina.getAddress();
                    if (ia != null && ia instanceof Inet4Address)
                    {
                        countFound++;
                        got = (Inet4Address) ia;
                    }
                }
                if (countFound != 1)
                {
                    got = null;
                }
            }
        }
        if (got == null ^ interfaceAddress == null)
        {
            Inet4Address old = interfaceAddress;
            interfaceAddress = got;
            if (interfaceAddress == null)
            {
                logger.info("Interface " + name + " lost interfaceAddress : " + old);
            } else
            {
                logger.info("Interface " + name + " new  interfaceAddress :  " + interfaceAddress);
            }
        }
        if (got != null && interfaceAddress != null && !got.equals(interfaceAddress))
        {
            interfaceAddress = got;
            logger.info("Interface " + name + " changed interfaceAddress :  " + interfaceAddress);
        }
    }

    // //////////////////////////////Getter and Setter
    public final String getName()
    {
        return name;
    }
}
