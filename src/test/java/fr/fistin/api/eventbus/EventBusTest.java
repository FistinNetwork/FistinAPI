package fr.fistin.api.eventbus;

import fr.fistin.api.eventbus.internal.InternalEventBus;
import fr.fistin.api.eventbus.internal.InternalFistinEventExecutor;
import org.junit.Test;

public class EventBusTest
{
    @Test
    public void testEventBusAPI()
    {
        final IFistinEventExecutor executor = new InternalFistinEventExecutor();
        final IFistinEventBus bus = new InternalEventBus();
        executor.getRegisterer().addEventBus(bus);
        bus.registerEvent(TestEvent.class);
        bus.registerEvent(AnotherTestEvent.class);
        bus.addListener(new FistinEventListener() {
            @FistinEventHandler
            public void handleTestEvent(TestEvent event)
            {
                System.out.println("Handling event " + event.getName() + ", printing: " + event.getToPrint());
            }
        });
        executor.handleEvent(new TestEvent("waaaaaw"));
        executor.handleEvent(new AnotherTestEvent("waaaaaw"));
        bus.addListener(new FistinEventListener() {
            @FistinEventHandler
            public void handleTestEvent(AnotherTestEvent event)
            {
                System.out.println("Handling another event " + event.getName() + ", printing: " + event.getToPrint());
            }
        });
        executor.handleEvent(new AnotherTestEvent("waaaaaw"));
    }

    private static class TestEvent implements IFistinEvent
    {
        private final String toPrint;

        public TestEvent(String toPrint)
        {
            this.toPrint = toPrint;
        }

        public String getToPrint()
        {
            return this.toPrint;
        }

        @Override
        public String getName()
        {
            return "TestEvent";
        }
    }

    private static class AnotherTestEvent implements IFistinEvent
    {
        private final String toPrint;

        public AnotherTestEvent(String toPrint)
        {
            this.toPrint = toPrint;
        }

        public String getToPrint()
        {
            return this.toPrint;
        }

        @Override
        public String getName()
        {
            return "AnotherTestEvent";
        }
    }
}
