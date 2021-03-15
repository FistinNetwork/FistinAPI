package fr.fistin.api.eventbus;

import fr.fistin.api.eventbus.template.DefaultEventBus;
import org.junit.Test;

import java.util.function.Supplier;

public class EventBusTest
{
    @Test
    public void testEventBusAPI()
    {
        final IFistinEventBus<Supplier<? extends IFistinEvent>> bus = new DefaultEventBus();
        bus.registerEvent(TestEvent.class);
        bus.registerEvent(AnotherTestEvent.class);
        bus.addListener(new FistinEventListener() {
            @FistinEventHandler
            public void handleTestEvent(TestEvent event)
            {
                System.out.println("Handling event " + event.getName() + ", printing: " + event.getToPrint());
            }
        });
        bus.handleEvent(() -> new TestEvent("waaaaaw"));
        bus.handleEvent(() -> new AnotherTestEvent("waaaaaw"));
        bus.addListener(new FistinEventListener() {
            @FistinEventHandler
            public void handleTestEvent(AnotherTestEvent event)
            {
                System.out.println("Handling another event " + event.getName() + ", printing: " + event.getToPrint());
            }
        });
        bus.handleEvent(() -> new AnotherTestEvent("waaaaaw"));
        bus.clear();
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
