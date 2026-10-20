package patterns.event_channel;


/**
 * Pub-sub паттерн является одной из вариаций паттерна Observer. Исходя из названия в паттерне выделяют два компонента:
 * Publisher (издатель) и Subscriber (подписчик). В отличие от Observer, связь между объектами осуществляется
 * посредством канала связи Event Channel (шины событий).
 * <p>
 * <a href="https://ru.wikipedia.org/wiki/%D0%9A%D0%B0%D0%BD%D0%B0%D0%BB_%D1%81%D0%BE%D0%B1%D1%8B%D1%82%D0%B8%D0%B9_(%D1%88%D0%B0%D0%B1%D0%BB%D0%BE%D0%BD_%D0%BF%D1%80%D0%BE%D0%B5%D0%BA%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F)">Wiki</a>
 */
public class EventChannelApp {

	public static void main(String[] args) {

		EventChannel channel = new EventChannelImpl();

		Publisher publisher_1 = new PublisherImpl("Topic_1", channel);
		Publisher publisher_2 = new PublisherImpl("Topic_2", channel);

		Subscriber subscriber_1 = new SubscriberImpl("Subscriber_1");
		Subscriber subscriber_2 = new SubscriberImpl("Subscriber_2");

		channel.subscribe("Topic_1", subscriber_1);
		channel.subscribe("Topic_1", subscriber_2);
		channel.subscribe("Topic_2", subscriber_2);

		publisher_1.publishEvent("Hello from publisher_1");
		publisher_2.publishEvent("Hello from publisher_2");

	}

}
