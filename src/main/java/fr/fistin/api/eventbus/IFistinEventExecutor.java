package fr.fistin.api.eventbus;

public interface IFistinEventExecutor
{
    void handleEvent(IFistinEvent event);
    void clear();
    IFistinEventBusRegisterer getRegisterer();

    @FunctionalInterface
    interface IFistinEventBusRegisterer
    {
        void addEventBus(IFistinEventBus bus);
    }
}
