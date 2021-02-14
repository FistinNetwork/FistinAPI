package fr.fistin.api.utils;

import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Internal
public class SetupListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final IFistinAPIProvider api = PluginProviders.getProvider(IFistinAPIProvider.class);
        //First add to database
        try {
            Connection connection = api.getDatabaseManager().getLevelingConnection().getConnection();
            Statement statement = connection.createStatement();
            if(!this.isPlayerExist(event.getPlayer(), statement))
                statement.execute(String.format("INSERT INTO player_levels (uuid, exp, coins) VALUES ('%s', 0, 0)", event.getPlayer().getUniqueId().toString()));
        } catch (Exception throwables) {
            throwables.printStackTrace();
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
