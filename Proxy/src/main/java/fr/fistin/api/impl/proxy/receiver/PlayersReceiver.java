package fr.fistin.api.impl.proxy.receiver;

import fr.fistin.api.packet.FistinPacket;
import fr.fistin.api.packet.FistinPacketReceiver;
import fr.fistin.api.packet.model.PlayerSendPacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by AstFaster
 * on 04/11/2022 at 16:08
 */
public class PlayersReceiver implements FistinPacketReceiver {

    @Override
    public void receive(FistinPacket packet) {
        if (packet instanceof PlayerSendPacket) {
            final PlayerSendPacket playerPacket = (PlayerSendPacket) packet;

            final ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(playerPacket.getServerName());
            final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerPacket.getPlayerId());

            if (player != null && serverInfo != null) {
                player.connect(serverInfo);
            }
        }
    }

}
