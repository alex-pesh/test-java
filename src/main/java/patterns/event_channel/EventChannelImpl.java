package patterns.event_channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventChannelImpl implements EventChannel {

	private final Map<String, List<Subscriber>> topicSubscribers = new HashMap<>();


	@Override
	public void subscribe(String topic, Subscriber subscriber) {
		topicSubscribers.computeIfAbsent(topic, key -> new ArrayList<>()).add(subscriber);
	}


	@Override
	public void publish(String topic, String data) {
		List<Subscriber> subscribers = topicSubscribers.get(topic);
		if (subscribers != null) {
			for (Subscriber subscriber : subscribers) {
				subscriber.notifyEvent(topic, data);
			}
		}
	}

}
