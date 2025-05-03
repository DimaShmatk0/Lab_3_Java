
public class Producer extends Thread {

    private final int id;
    private final int itemCount;
    private final MyStorage storage;

    public Producer(int id, int itemCount, MyStorage storage) {
        this.id = id;
        this.itemCount = itemCount;
        this.storage = storage;
        start();
    }

    public void run() {
        for (int i = 0; i < itemCount; i++) {
            try {
                storage.produce(new Item(id + i), id);
                System.out.println("Producer " + id + " added item: " + (id + i));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}