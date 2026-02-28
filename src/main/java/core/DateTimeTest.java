package core;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

import static java.time.temporal.ChronoField.*;

public class DateTimeTest {

	public static void main(String[] args) {
		testAdjustment();
	}



	public static void testClock() {

		Clock sysClock = Clock.system(ZoneOffset.ofHours(10));
		Clock offset  = Clock.offset(sysClock, Duration.ofDays(1));

		System.out.println(LocalDateTime.now(sysClock));
	}


	public static void testChronoField() {
		LocalDateTime date = LocalDateTime.now();

		System.out.println(date);

		System.out.println("DAY_OF_MONTH					[1]: " +   date.with(DAY_OF_MONTH, 1) );
		System.out.println("HOUR_OF_DAY  					[8]:  " +  date.with(HOUR_OF_DAY, 8) );
		System.out.println("ALIGNED_WEEK_OF_MONTH			[1]:  " +  date.with(ChronoField.ALIGNED_WEEK_OF_MONTH, 1) );
		System.out.println("ALIGNED_DAY_OF_WEEK_IN_MONTH	[1]:  " +  date.with(ALIGNED_DAY_OF_WEEK_IN_MONTH, 1) );
	}



	public static void testAdjustment() {
		LocalDateTime date = LocalDateTime.now();
		System.out.println(date);

		System.out.println( date.with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.MONDAY)) );
		System.out.println( date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)) );
	}

}
