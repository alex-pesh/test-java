package patterns.event_channel;

public interface EventChannel {

	void subscribe(String topic, Subscriber subscriber);

	void publish(String topic, String data);

}
