package concurrency;

public class AlternatingPrinter {

	private final Object lock = new Object();
	private boolean numberTurn = true;

	public static void main(String[] args) {
		AlternatingPrinter printer = new AlternatingPrinter();
		Thread numberThread = new Thread(() -> printer.printNumbers());
		Thread letterThread = new Thread(() -> printer.printLetters());
		numberThread.start();
		letterThread.start();
	}

	public void printNumbers() {
		for (int i = 1; i <= 26; i++) {
			synchronized (lock) {
				while (!numberTurn) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				System.out.print(i + " ");
				numberTurn = false;
				lock.notifyAll();
			}
		}
	}

	public void printLetters() {
		for (char c = 'A'; c <= 'Z'; c++) {
			synchronized (lock) {
				while (numberTurn) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				System.out.print(c + " ");
				numberTurn = true;
				lock.notifyAll();
			}
		}
	}

}
