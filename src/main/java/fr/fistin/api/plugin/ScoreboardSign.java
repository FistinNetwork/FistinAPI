package fr.fistin.api.plugin;

import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ScoreboardSign
{
    private boolean created = false;
    private final VirtualTeam[] lines = new VirtualTeam[15];
    private final Player player;
    private String objectiveName;

    public ScoreboardSign(Player player, String objectiveName)
    {
        this.player = player;
        this.objectiveName = objectiveName;
    }

    public void create()
    {
        if (this.created)
            return;

        final PlayerConnection playerConnection = this.getPlayerConnection();
        playerConnection.sendPacket(this.createObjectivePacket(PacketScoreboardMode.CREATE.ordinal(), this.objectiveName));
        playerConnection.sendPacket(this.setObjectiveSlot());
        int i = 0;
        while (i < this.lines.length)
            this.sendLine(i++);

        this.created = true;
    }

    public void destroy()
    {
        if (!this.created)
            return;

        this.getPlayerConnection().sendPacket(this.createObjectivePacket(PacketScoreboardMode.REMOVE.ordinal(), null));
        for (VirtualTeam team : this.lines)
            if (team != null)
                this.getPlayerConnection().sendPacket(team.removeTeam());

        this.created = false;
    }

    public void setObjectiveName(String name)
    {
        this.objectiveName = name;
        if (this.created)
            this.getPlayerConnection().sendPacket(this.createObjectivePacket(PacketScoreboardMode.UPDATE.ordinal(), name));
    }

    public void setLine(int line, String value) 
    {
        final VirtualTeam team = this.getOrCreateTeam(line);
        final String old = team.getCurrentPlayer();

        if (old != null && this.created)
            this.getPlayerConnection().sendPacket(this.removeLine(old));

        team.setValue(value);
        this.sendLine(line);
    }

    public void removeLine(int line)
    {
        final VirtualTeam team = this.getOrCreateTeam(line);
        final String old = team.getCurrentPlayer();

        if (old != null && this.created)
        {
            this.getPlayerConnection().sendPacket(this.removeLine(old));
            this.getPlayerConnection().sendPacket(team.removeTeam());
        }

        this.lines[line] = null;
    }

    public String getLine(int line)
    {
        if (line > 14)
            return null;
        if (line < 0)
            return null;
        return this.getOrCreateTeam(line).getValue();
    }

    public VirtualTeam getTeam(int line)
    {
        if (line > 14)
            return null;
        if (line < 0)
            return null;
        return this.getOrCreateTeam(line);
    }

    private PlayerConnection getPlayerConnection()
    {
        return ((CraftPlayer)this.player).getHandle().playerConnection;
    }

    private void sendLine(int line)
    {
        if (line > 14)
            return;
        if (line < 0)
            return;
        if (!this.created)
            return;

        final int score = (15 - line);
        final VirtualTeam val = getOrCreateTeam(line);
        
        for (Packet<?> packet : val.sendLine())
            this.getPlayerConnection().sendPacket(packet);
        
        this.getPlayerConnection().sendPacket(sendScore(val.getCurrentPlayer(), score));
        val.reset();
    }

    private VirtualTeam getOrCreateTeam(int line)
    {
        if (this.lines[line] == null)
            this.lines[line] = new VirtualTeam("__fakeScore" + line);

        return this.lines[line];
    }

    private PacketPlayOutScoreboardObjective createObjectivePacket(int mode, String displayName)
    {
        final PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        setField(packet, "a", this.player.getName());

        // Mode
        // 0 : create
        // 1 : remove
        // 2 : update
        setField(packet, "d", mode);

        if (mode == 0 || mode == 2)
        {
            setField(packet, "b", displayName);
            setField(packet, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        }

        return packet;
    }

    private PacketPlayOutScoreboardDisplayObjective setObjectiveSlot()
    {
        final PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();
        setField(packet, "a", 1);
        setField(packet, "b", this.player.getName());

        return packet;
    }

    private PacketPlayOutScoreboardScore sendScore(String line, int score)
    {
        final PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line);
        setField(packet, "b", this.player.getName());
        setField(packet, "c", score);
        setField(packet, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);

        return packet;
    }

    private PacketPlayOutScoreboardScore removeLine(String line)
    {
        return new PacketPlayOutScoreboardScore(line);
    }

    public static class VirtualTeam
    {
        private final String name;
        private String prefix;
        private String suffix;
        private String currentPlayer;
        private String oldPlayer;

        private boolean prefixChanged, suffixChanged, playerChanged = false;
        private boolean first = true;

        private VirtualTeam(String name, String prefix, String suffix)
        {
            this.name = name;
            this.prefix = prefix;
            this.suffix = suffix;
        }

        private VirtualTeam(String name)
        {
            this(name, "", "");
        }

        public String getPrefix()
        {
            return this.prefix;
        }

        public void setPrefix(String prefix)
        {
            if (this.prefix == null || !this.prefix.equals(prefix))
                this.prefixChanged = true;
            this.prefix = prefix;
        }

        public String getSuffix()
        {
            return this.suffix;
        }

        public void setSuffix(String suffix)
        {
            if (this.suffix == null || !this.suffix.equals(prefix))
                this.suffixChanged = true;
            this.suffix = suffix;
        }

        private PacketPlayOutScoreboardTeam createPacket(int mode)
        {
            final PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            setField(packet, "a", this.name);
            setField(packet, "h", mode);
            setField(packet, "b", "");
            setField(packet, "c", this.prefix);
            setField(packet, "d", this.suffix);
            setField(packet, "i", 0);
            setField(packet, "e", "always");
            setField(packet, "f", 0);

            return packet;
        }

        public PacketPlayOutScoreboardTeam createTeam()
        {
            return this.createPacket(0);
        }

        public PacketPlayOutScoreboardTeam updateTeam()
        {
            return this.createPacket(2);
        }

        public PacketPlayOutScoreboardTeam removeTeam()
        {
            final PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            setField(packet, "a", name);
            setField(packet, "h", 1);
            this.first = true;
            return packet;
        }

        public void setPlayer(String name)
        {
            if (this.currentPlayer == null || !this.currentPlayer.equals(name))
                this.playerChanged = true;
            this.oldPlayer = this.currentPlayer;
            this.currentPlayer = name;
        }

        public Iterable<PacketPlayOutScoreboardTeam> sendLine()
        {
            final List<PacketPlayOutScoreboardTeam> packets = new ArrayList<>();

            if (this.first) packets.add(createTeam());
            else if (this.prefixChanged || this.suffixChanged) packets.add(this.updateTeam());
            
            if (this.first || this.playerChanged)
            {
                if (this.oldPlayer != null)
                    packets.add(this.addOrRemovePlayer(4, this.oldPlayer));
                packets.add(this.changePlayer());
            }

            if (this.first) this.first = false;

            return packets;
        }

        public void reset()
        {
            this.prefixChanged = false;
            this.suffixChanged = false;
            this.playerChanged = false;
            this.oldPlayer = null;
        }

        public PacketPlayOutScoreboardTeam changePlayer()
        {
            return this.addOrRemovePlayer(3, currentPlayer);
        }

        @SuppressWarnings("unchecked")
        public PacketPlayOutScoreboardTeam addOrRemovePlayer(int mode, String playerName)
        {
            final PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            
            setField(packet, "a", name);
            setField(packet, "h", mode);

            try
            {
                final Field field = packet.getClass().getDeclaredField("g");
                field.setAccessible(true);
                ((List<String>) field.get(packet)).add(playerName);
            }
            catch (NoSuchFieldException | IllegalAccessException e)
            {
                PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);
            }

            return packet;
        }

        public String getCurrentPlayer()
        {
            return this.currentPlayer;
        }

        public String getValue()
        {
            return this.getPrefix() + this.getCurrentPlayer() + this.getSuffix();
        }

        public void setValue(String value)
        {
            if (value.length() <= 16)
            {
                this.setPrefix("");
                this.setSuffix("");
                this.setPlayer(value);
            }
            else if (value.length() <= 32)
            {
                this.setPrefix(value.substring(0, 16));
                this.setPlayer(value.substring(16));
                this.setSuffix("");
            }
            else if (value.length() <= 48)
            {
                this.setPrefix(value.substring(0, 16));
                this.setPlayer(value.substring(16, 32));
                this.setSuffix(value.substring(32));
            }
            else throw new IllegalArgumentException("Too long value ! Max 48 characters, value was " + value.length() + " !");
        }
    }

    private static void setField(Object edit, String fieldName, Object value)
    {
        try
        {
            final Field field = edit.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(edit, value);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private enum PacketScoreboardMode
    {
        CREATE,
        REMOVE,
        UPDATE
    }
}