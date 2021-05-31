package fr.fistin.api.packets;

public class HStartServerPacket implements FistinPacket
{
    private final String template;

    public HStartServerPacket(String template)
    {
        this.template = template;
    }

    public String getTemplate()
    {
        return this.template;
    }
}
