package fr.fistin.api.impl;

import fr.fistin.api.packets.FistinPacket;
import fr.fistin.api.packets.PacketException;
import fr.fistin.api.packets.PacketManager;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PacketManagerImplTest
{
    private boolean test = false;

    @Test
    public void testRegisterAndSendPacket()
    {
        final PacketManager packetManager = new PacketManagerImpl();
        packetManager.registerPacket(ATestPacket.class, aTestPacket -> this.test = true);
        // it is ignored if the packet manager is ok.
        packetManager.registerPacket(ATestPacket.class, aTestPacket -> this.test = false);
        packetManager.sendPacket(new ATestPacket("wawawaw"));
        assertTrue(this.test);
        packetManager.clear();
    }

    @Test(expected = PacketException.class)
    public void testSendAfterStop()
    {
        final PacketManager packetManager = new PacketManagerImpl();
        packetManager.registerPacket(ATestPacket.class, aTestPacket -> this.test = true);
        packetManager.clear();
        packetManager.sendPacket(new ATestPacket("wawawaw"));
        assertFalse(this.test);
    }

    @Test(expected = PacketException.class)
    public void testSendUnregisteredPacket()
    {
        final PacketManager packetManager = new PacketManagerImpl();
        packetManager.sendPacket(new ATestPacket("wawawaw"));
        packetManager.clear();
        assertFalse(this.test);
    }

    private static class ATestPacket implements FistinPacket
    {
        private final String foobar;

        public ATestPacket(String foobar)
        {
            this.foobar = foobar;
        }

        @Override
        public String toString()
        {
            return "ATestPacket{" + "foobar='" + this.foobar + '\'' + '}';
        }
    }
}
