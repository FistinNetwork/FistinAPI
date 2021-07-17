package fr.fistin.api.plugin.providers;

import java.util.logging.Logger;

public interface IStandalonePlugin extends IPluginProvider
{
    <T> T unsafeGet(String parameter, TypeGet typeGet);

    enum TypeGet {
        FIELD,
        METHOD
    }

    Logger getLogger();
}
