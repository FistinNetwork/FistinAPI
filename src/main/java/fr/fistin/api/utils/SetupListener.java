package fr.fistin.api.utils;

import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.ApiStatus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@ApiStatus.Internal
public class SetupListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final IFistinAPIProvider api = PluginProviders.getProvider(IFistinAPIProvider.class);
        //First add to database
        try {
            Connection connection = api.getDatabaseManager().getConnection("LevelingConnection").getConnection();
            Statement statement = connection.createStatement();
            if(!this.isPlayerExist(event.getPlayer(), statement))
                statement.execute(String.format("INSERT INTO player_levels (uuid, exp, coins) VALUES ('%s', 0, 0)", event.getPlayer().getUniqueId().toString()));
        } catch (Exception e) {
            e.printStackTrace();
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
