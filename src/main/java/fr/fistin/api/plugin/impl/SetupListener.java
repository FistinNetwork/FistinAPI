package fr.fistin.api.plugin.impl;

import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.ApiStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;

@ApiStatus.Internal
public class SetupListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final IFistinAPIProvider api = PluginProviders.getProvider(IFistinAPIProvider.class);
        try {
            final Connection connection = api.getDatabaseManager().getConnection("LevelingConnection").getConnection();
            if(!this.isPlayerExist(event.getPlayer(), connection.createStatement()))
            {
                final PreparedStatement statement = connection.prepareStatement("INSERT INTO player_levels (uuid, exp, coins) VALUES (?, 0, 0)");
                statement.setString(1, event.getPlayer().getUniqueId().toString());
                statement.executeUpdate();
            }
        } catch (Exception e) {
            api.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private boolean isPlayerExist(Player player, Statement statement) throws Exception{
        ResultSet resultSet = statement.executeQuery("SELECT * FROM player_levels");
        if(resultSet.getMetaData().getColumnName(1).equalsIgnoreCase("uuid")){
            while (resultSet.next()){
                if(resultSet.getString("uuid").equalsIgnoreCase(player.getUniqueId().toString())){
                    return true;
                }
            }
        }
        return false;
    }
}
