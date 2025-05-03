public class Consumer extends Thread {

    private final int id;
    private final int itemCount;
    private final MyStorage storage;

    public Consumer(int id, int itemCount, MyStorage storage) {
        this.id = id;
        this.itemCount = itemCount;
        this.storage = storage;
        start();
    }

    public void run() {
        for (int i = 0; i < itemCount; i++) {
            try {
                storage.consume(id);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}