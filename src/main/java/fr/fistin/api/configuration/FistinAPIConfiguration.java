package fr.fistin.api.configuration;

public interface FistinAPIConfiguration extends FistinConfiguration
{
    String getLevelingUser();
    String getLevelingPass();
    String getLevelingHost();
    String getLevelingDbName();
    int getLevelingPort();
    String getHydraHost();
    int getHydraPort();
    String getHydraPass();
    boolean getHydraEnable();
}
