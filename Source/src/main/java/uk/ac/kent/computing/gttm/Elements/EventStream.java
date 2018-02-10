package uk.ac.kent.computing.gttm.Elements;

import java.util.List;

/**
 * An EventStream is used to encapsulate a stream of Event objects which is used
 * to encapsulate a piece of music.*
 *
 * @author Alexander Dodd
 */
public class EventStream {

    private List<Event> eventStream;
    private Scale localScale;

    /**
     * Create an EventStream object.
     *
     * @param events a list of Event objects which represents the stream of
     * events
     * @param localScale the scale associated with the EventStream
     */
    public EventStream(List<Event> events, Scale localScale) {
        eventStream = events;
        this.localScale = localScale;
    }

    /**
     *
     * @return the Scale associated with this EventStream object
     */
    public Scale getLocalScale() {
        return localScale;
    }

    /**
     * Retrieves an Event object at the specified position.
     *
     * @param position the position from which to retrieve the Event object
     * @return an Event object at the given position, or null if the position is
     * greater than the EventStream length
     */
    public Event getEvent(int position) {
        if (eventStream.size() <= position) {
            return null;
        } else {
            return eventStream.get(position);
        }
    }

    /**
     * 
     * @return an ordered List containing the stream of Event objects
     */
    public List<Event> getEventList() {
        return eventStream;
    }

}
