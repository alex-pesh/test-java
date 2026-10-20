package misc;


/**
 * Без volatile у flag — часто зависает.
 * Читатель не видит изменение флага, т.к. JIT компилятор может выносить чтение flag из цикла.
 * Чтобы поймать именно момент, можно запустить с -XX:+PrintCompilation и увидеть, как цикл компилируется,
 * после чего flag перестаёт перечитываться.
 * <p>
 * Запустить с -Xint (только интерпретатор) — зависания не будет никогда.
 * Запустить с -XX:TieredStopAtLevel=1 (только C1, без C2) — зависание пропадёт.
 * Если зависает только на tier 4 значит, изменение произошло именно на C2.
 *
 */
public class VolatileTest {

	/*volatile*/ static boolean flag = false;
	static int data = 0;

	public static void main(String[] args) throws Exception {
		Thread reader = new Thread(() -> {
			long count = 0;
			while (!flag) {
				count++;
			}
			System.out.println("Reader saw flag=true, data=" + data + ", count=" + count);
		}, "reader");

		reader.start();

		Thread.sleep(100); // даём JIT скомпилировать цикл
		data = 42;
		flag = true;

		reader.join(3000);

		if (reader.isAlive()) {
			System.out.println("Поток reader завис: изменение flag не видно");
			System.exit(1);
		}
	}
}
