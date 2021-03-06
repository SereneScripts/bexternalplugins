package net.runelite.client.plugins.socket.packet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.plugins.socket.org.json.JSONObject;

@AllArgsConstructor
public class SocketPlayerJoin {

    @Getter(AccessLevel.PUBLIC)
    private String playerName;

}
