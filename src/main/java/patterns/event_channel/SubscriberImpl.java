package patterns.event_channel;

public class SubscriberImpl implements Subscriber {

	private String name;

	public SubscriberImpl(String name) {
		this.name = name;
	}


	@Override
	public void notifyEvent(String topic, String data) {
		System.out.printf("Subscriber '%s' received event from topic '%s': %s%n", name, topic, data);
	}

}
