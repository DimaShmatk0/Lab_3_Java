import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int storageSize = 100;
        int producerCount = 50;
        int consumerCount = 150;
        int productsCount = 200000;

        MyStorage storage = new MyStorage(storageSize);

        int[] production = SplitProducts(productsCount, producerCount);
        int[] consumption = SplitProducts(productsCount, consumerCount);

        for (int i = 0; i < producerCount; i++) {
            new Producer(i + 1, production[i], storage);
        }

        for (int i = 0; i < consumerCount; i++) {
            new Consumer(i + 1, consumption[i], storage);
        }

    }

    private static int[] SplitProducts(int total, int parts) {
        if (parts <= 0 || total < parts)
            throw null;

        Random rand = new Random();
        int[] result = new int[parts];
        int remaining = total;

        for (int i = 0; i < parts - 1; i++) {
            // Гарантуємо, що для останніх частин залишиться хоча б по 1
            int max = remaining - (parts - i - 1);
            result[i] = rand.nextInt(1, max + 1);
            remaining -= result[i];
        }

        result[parts - 1] = remaining;
        return result;
    }
}

