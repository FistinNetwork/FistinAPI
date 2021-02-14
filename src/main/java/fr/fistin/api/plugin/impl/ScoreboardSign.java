package fr.fistin.api.plugin.impl;

import fr.fistin.api.plugin.scoreboard.IScoreboardSign;
import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.Internal;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;

@Internal
class ScoreboardSign implements IScoreboardSign
{
    private boolean created = false;
    private final IVirtualTeam[] lines = new IVirtualTeam[15];
    private final Player player;
    private String objectiveName;

    ScoreboardSign(Player player, String objectiveName)
    {
        this.player = player;
        this.objectiveName = objectiveName;
    }

    @Override
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

    @Override
    public void destroy()
    {
        if (!this.created)
            return;

        this.getPlayerConnection().sendPacket(this.createObjectivePacket(PacketScoreboardMode.REMOVE.ordinal(), null));
        for (IVirtualTeam team : this.lines)
            if (team != null)
                this.getPlayerConnection().sendPacket((PacketPlayOutScoreboardTeam)team.removeTeam().get());

        this.created = false;
    }

    @Override
    public void setObjectiveName(String name)
    {
        this.objectiveName = name;
        if (this.created)
            this.getPlayerConnection().sendPacket(this.createObjectivePacket(PacketScoreboardMode.UPDATE.ordinal(), name));
    }

    @Override
    public void setLine(int line, String value)
    {
        final IVirtualTeam team = this.getOrCreateTeam(line);
        final String old = team.getCurrentPlayer();

        if (old != null && this.created)
            this.getPlayerConnection().sendPacket(this.removeLine(old));

        team.setValue(value);
        this.sendLine(line);
    }

    @Override
    public void removeLine(int line)
    {
        final IVirtualTeam team = this.getOrCreateTeam(line);
        final String old = team.getCurrentPlayer();

        if (old != null && this.created)
        {
            this.getPlayerConnection().sendPacket(this.removeLine(old));
            this.getPlayerConnection().sendPacket((PacketPlayOutScoreboardTeam)team.removeTeam().get());
        }

        this.lines[line] = null;
    }

    @Override
    public String getLine(int line)
    {
        if (line > 14)
            return null;
        if (line < 0)
            return null;
        return this.getOrCreateTeam(line).getValue();
    }

    @Override
    public IVirtualTeam getTeam(int line)
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
        final IVirtualTeam val = this.getOrCreateTeam(line);
        
        for (Object packet : val.sendLine())
            this.getPlayerConnection().sendPacket((PacketPlayOutScoreboardTeam)packet);
        
        this.getPlayerConnection().sendPacket(this.sendScore(val.getCurrentPlayer(), score));
        val.reset();
    }

    private IVirtualTeam getOrCreateTeam(int line)
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

    public static class VirtualTeam implements IVirtualTeam
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

        @Override
        public String getPrefix()
        {
            return this.prefix;
        }

        @Override
        public void setPrefix(String prefix)
        {
            if (this.prefix == null || !this.prefix.equals(prefix))
                this.prefixChanged = true;
            this.prefix = prefix;
        }

        @Override
        public String getSuffix()
        {
            return this.suffix;
        }

        @Override
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

        @Override
        public Supplier<?> createTeam()
        {
            return () -> this.createPacket(PacketScoreboardMode.CREATE.ordinal());
        }

        @Override
        public Supplier<?> updateTeam()
        {
            return () -> this.createPacket(PacketScoreboardMode.UPDATE.ordinal());
        }

        @Override
        public Supplier<?> removeTeam()
        {
            final PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            setField(packet, "a", this.name);
            setField(packet, "h", PacketScoreboardMode.REMOVE.ordinal());
            this.first = true;
            return () -> packet;
        }

        @Override
        public void setPlayer(String name)
        {
            if (this.currentPlayer == null || !this.currentPlayer.equals(name))
                this.playerChanged = true;
            this.oldPlayer = this.currentPlayer;
            this.currentPlayer = name;
        }

        @Override
        public List<?> sendLine()
        {
            final List<PacketPlayOutScoreboardTeam> packets = new ArrayList<>();

            if (this.first) packets.add((PacketPlayOutScoreboardTeam)this.createTeam().get());
            else if (this.prefixChanged || this.suffixChanged) packets.add((PacketPlayOutScoreboardTeam)this.updateTeam().get());
            
            if (this.first || this.playerChanged)
            {
                if (this.oldPlayer != null)
                    packets.add((PacketPlayOutScoreboardTeam)this.addOrRemovePlayer(4, this.oldPlayer).get());
                packets.add((PacketPlayOutScoreboardTeam)this.changePlayer().get());
            }

            if (this.first) this.first = false;

            return packets;
        }

        @Override
        public void reset()
        {
            this.prefixChanged = false;
            this.suffixChanged = false;
            this.playerChanged = false;
            this.oldPlayer = null;
        }

        @Override
        public Supplier<?> changePlayer()
        {
            return this.addOrRemovePlayer(3, this.currentPlayer);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Supplier<?> addOrRemovePlayer(int mode, String playerName)
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

            return () -> packet;
        }

        @Override
        public String getCurrentPlayer()
        {
            return this.currentPlayer;
        }

        @Override
        public String getValue()
        {
            return this.getPrefix() + this.getCurrentPlayer() + this.getSuffix();
        }

        @Override
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