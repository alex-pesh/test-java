package patterns.event_channel;

public class PublisherImpl implements Publisher{

	private String topic;
	private EventChannel channel;

	public PublisherImpl(String topic, EventChannel channel) {
		this.topic = topic;
		this.channel = channel;
	}


	@Override
	public void publishEvent(String data) {
		channel.publish(topic, data);
	}

}
