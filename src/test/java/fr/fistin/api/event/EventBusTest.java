package fr.fistin.api.eventbus;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EventBusTest {
    private interface MockedInterface {
        void bell();
    }

    private interface MockedFistinEvent extends MockedInterface, FistinEvent {}
    private interface ExtendedMock extends MockedInterface, FistinEvent {}

    private FistinEventBus<Supplier<? extends FistinEvent>> bus;

    @Mock
    private MockedFistinEvent event;
    @Mock
    private ExtendedMock secondEvent;
    @Mock
    private ExtendedMock thirdEvent;

    @BeforeEach
    public void setup() {
        this.bus = new DefaultEventBus();
    }

    @AfterEach
    public void clean() {
        this.bus.clean();
    }

    @Test
    public void testHandleOneEvent() {
        this.bus.handleParent(true);
        this.bus.registerEvent(MockedFistinEvent.class);
        this.bus.addListener(new FistinEventListener() {
            @FistinEventHandler
            public void handleTestEvent(MockedFistinEvent event)
            {
                event.bell();
            }
        });

        this.bus.handleEvent(() -> this.event);

        verify(this.event).bell();
    }

    @Test
    public void testHandleTwoEvents() {
        this.bus.handleParent(true);
        this.bus.registerEvent(MockedFistinEvent.class);
        this.bus.registerEvent(ExtendedMock.class);

        this.bus.addListener(new FistinEventListener() {
            @FistinEventHandler
            public void handleTestEvent(MockedFistinEvent event)
            {
                event.bell();
            }
        });

        this.bus.addListener(new FistinEventListener() {
            @FistinEventHandler
            public void handleTestEvent(ExtendedMock event)
            {
                event.bell();
            }
        });

        this.bus.handleEvent(() -> this.secondEvent);
        this.bus.handleEvent(() -> this.event);

        verify(this.event).bell();
        verify(this.event).bell();
    }

    @Test
    public void shouldHookFirstAndThirdEvent() {
        this.bus.handleParent(true);
        this.bus.registerEvent(MockedFistinEvent.class);
        this.bus.registerEvent(ExtendedMock.class);

        this.bus.addListener(new FistinEventListener() {
            @FistinEventHandler
            public void handleTestEvent(MockedFistinEvent event)
            {
                event.bell();
            }
        });

        this.bus.handleEvent(() -> this.event);
        this.bus.handleEvent(() -> this.secondEvent);
        this.bus.addListener(new FistinEventListener() {
            @FistinEventHandler
            public void handleTestEvent(ExtendedMock event)
            {
                event.bell();
            }
        });
        this.bus.handleEvent(() -> this.thirdEvent);

        verify(this.event).bell();
        verify(this.secondEvent, never()).bell();
        verify(this.thirdEvent).bell();
    }
}
