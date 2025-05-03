import java.util.concurrent.Semaphore;

public class MyStorage {
    private final Semaphore emptyStorage = new Semaphore(0);//чи склад пустий
    private final Semaphore fullStorage;//размер склада
    private final Semaphore mutex = new Semaphore(1);//скільки може зайти виробників та покупців на склад
    private final Item[] items;
    private int putIndex = 0;
    private int takeIndex = 0;

    public MyStorage(int size) {
        items = new Item[size];
        fullStorage = new Semaphore(size);
    }

    public void produce(Item item, int producerId) throws InterruptedException {
        System.out.printf("Виробник %d: підходить до складу: %n", producerId);

        // чекаємо на вільне місце
        System.out.printf("Виробник %d: чекає на вільне місце: %n", producerId);
        fullStorage.acquire();
        System.out.printf("Виробник %d: отримав дозвіл на вхід (є вільне місце): %n", producerId);

        // захоплюємо м’ютекс
        System.out.printf("Виробник %d: чекає на м’ютекс для роботи з масивом: %n", producerId);
        mutex.acquire();
        System.out.printf("Виробник %d: зайшов у критичну секцію (має м’ютекс): %n", producerId);

        // додаємо
        System.out.printf("Виробник %d: починає класти продукт %s id: %n", producerId, item);
        addItem(item);
        System.out.printf("Виробник %d: успішно поклав продукт %s id: %n", producerId, item);

        // вихід із критичної секції
        mutex.release();
        System.out.printf("Виробник %d: вийшов із складу (відпустив м’ютекс): %n", producerId);

        // сигналізуємо споживачам, що з’явився новий предмет
        emptyStorage.release();
        System.out.printf("Виробник %d: повідомив, що є новий предмет у складі: %n", producerId);
        System.out.printf("Виробник %d: завершує операцію: %n", producerId);
    }

    public void consume(int consumerId) throws InterruptedException {
        System.out.printf("Споживач %d: підходить до складу: %n", consumerId);

        // чекаємо, поки з’явиться хоча б один предмет
        System.out.printf("Споживач %d: чекає на наявність предмета: %n", consumerId);
        emptyStorage.acquire();
        System.out.printf("Споживач %d: отримав дозвіл на вхід (є предмет): %n", consumerId);

        // захоплюємо м’ютекс
        System.out.printf("Споживач %d: чекає на м’ютекс для роботи з масивом: %n", consumerId);
        mutex.acquire();
        System.out.printf("Споживач %d: зайшов у критичну секцію (має м’ютекс): %n", consumerId);

        // забираємо
        System.out.printf("Споживач %d: починає брати предмет: %n", consumerId);
        Item item = removeItem();
        System.out.printf("Споживач %d: успішно взяв предмет з id: %n", consumerId, item.getId());

        // вихід із критичної секції
        mutex.release();
        System.out.printf("Споживач %d: вийшов із складу (відпустив м’ютекс): %n", consumerId);

        // сигналізуємо виробникам, що звільнилося місце
        fullStorage.release();
        System.out.printf("Споживач %d: повідомив, що звільнилось місце у складі: %n", consumerId);
        System.out.printf("Споживач %d: завершує операцію: %n", consumerId);
    }


    private void addItem(Item item) {
        items[putIndex] = item;
        putIndex = (putIndex + 1) % items.length; //закольцовка масиву
    }

    private Item removeItem() {
        Item item = items[takeIndex];
        items[takeIndex] = null;
        takeIndex = (takeIndex + 1) % items.length;//закольцовка масиву
        return item;
    }


}