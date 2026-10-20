package patterns.event_channel;

public interface Subscriber {

	void notifyEvent(String topic, String data);

}
