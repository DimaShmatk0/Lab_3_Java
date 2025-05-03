
public class Main {
    public static void main(String[] args) {
        MyStorage storage = new MyStorage(3);

        new Producer(1, 4, storage);
        new Producer(2, 6, storage);
        new Producer(3, 3, storage);

        new Consumer(1, 6, storage);
        new Consumer(2, 12, storage);

        new Producer(4, 5, storage);
    }
}