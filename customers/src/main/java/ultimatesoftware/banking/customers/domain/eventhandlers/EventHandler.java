package ultimatesoftware.banking.customers.domain.eventhandlers;

import org.axonframework.eventhandling.AnnotationEventListenerAdapter;
import org.axonframework.eventsourcing.eventstore.EventStore;

public class EventHandler {
    public EventHandler(EventStore eventStore) {
        AnnotationEventListenerAdapter annotationEventListenerAdapter
                = new AnnotationEventListenerAdapter(new CustomerEventHandler());
        eventStore.subscribe(eventMessages -> eventMessages.forEach(e -> {
            try {
                annotationEventListenerAdapter.handle(e);
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
        }));
    }
}
