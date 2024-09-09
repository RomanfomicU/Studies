package PR1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Ex1 {
    public static List<Integer> generateArray10000() { // генерация 10000 рандомных чисел типа Integer в листе
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            int randomNumber = random.nextInt();
            list.add(randomNumber);
        }
        return list;
    }
    public static int findMinNumber(List<Integer> list) throws InterruptedException {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is null");
        }
        int minNumber = list.get(0); // Предположим, что первый элемент - минимальный
        for (int number : list) {
            Thread.sleep(1);
            if (number < minNumber) {
                minNumber = number; // Найдено новое минимальное число
            }
        }
        return minNumber;
    }
    public static int findMinNumberMnogopotok(List<Integer> list) throws InterruptedException, ExecutionException {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is null");
        }
        // Определяем количество доступных процессоров
        int numberOfThreads = 4;
        // Создаем пул потоков для выполнения задач
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        // Создаем список задач для каждого подсписка
        List<Callable<Integer>> tasks = new ArrayList<>();
        int batchSize = list.size() / numberOfThreads;

        // Разбиваем список на подсписки и создаем задачи для каждого подсписка
        for (int i = 0; i < numberOfThreads; i++) {
            final int startIndex = i * batchSize;
            final int endIndex = (i == numberOfThreads - 1) ? list.size() : (i +
                    1) * batchSize;
            tasks.add(() -> findMinInRange(list.subList(startIndex, endIndex)));
        }
        // Запускаем все задачи и получаем Future объекты для получения результатов
        List<Future<Integer>> futures = executorService.invokeAll(tasks);
        // Инициализируем переменную для хранения максимального значения
        int min = Integer.MAX_VALUE;
        // Обходим результаты каждой задачи и находим минимальное значение
        for (Future<Integer> future : futures) {
            int partialMin = future.get();
            Thread.sleep(1);
            if (partialMin < min) {
                min = partialMin;
            }
        }
        // Завершаем пул потоков
        executorService.shutdown();
        // Возвращаем минимальное значение из всех подсписков
        return min;
    }
    // Функция для поиска минимальное числа в подсписке
    private static int findMinInRange(List<Integer> sublist) throws InterruptedException {
        int min = Integer.MAX_VALUE;
        for (int number : sublist) {
            Thread.sleep(1);
            if (number < min) {
                min = number;
            }
        }
        return min;
    }
    public static int findMinNumberFork(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is null");
        }
        // Создаем пул потоков ForkJoin
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        // Создаем корневую задачу MinFinderTask для всего списка
        MinFinderTask task = new MinFinderTask(list, 0, list.size());
        // Выполняем корневую задачу и получаем результат
        return forkJoinPool.invoke(task);
    }

    // Внутренний класс MinFinderTask, расширяющий RecursiveTask для многопоточного выполнения
    static class MinFinderTask extends RecursiveTask<Integer> {
        private List<Integer> list;
        private int start;
        private int end;
        // Конструктор MinFinderTask для создания задачи для подсписка
        MinFinderTask(List<Integer> list, int start, int end) {
            this.list = list;
            this.start = start;
            this.end = end;
        }
        // Метод compute(), выполняющий вычисления для задачи
        @Override
        protected Integer compute() {
            // Если в подсписке только один элемент, вернем его
            if (end - start <= 1000) {
                try {
                    return findMinInRange(list.subList(start, end));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // Найдем середину подсписка
            int middle = start + (end - start) / 2;
            // Создаем две подзадачи для левой и правой половин подсписка
            MinFinderTask leftTask = new MinFinderTask(list, start, middle);
            MinFinderTask rightTask = new MinFinderTask(list, middle, end);
            // Запускаем подзадачу для правой половины параллельно
            leftTask.fork();
            // Вычисляем минимальные значения в левой и правой половинах подсписка
            int rightResult = rightTask.compute();
            int leftResult = leftTask.join();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Возвращаем минимальное значение из левой и правой половин
            return Math.min(leftResult, rightResult);
        }
        public static void main(String[] args) throws InterruptedException,
                ExecutionException {
            List<Integer> testList = generateArray10000();
            long startTime = System.nanoTime();
            int result = findMinNumber(testList);
            long endTime = System.nanoTime();
            long durationInMilliseconds = (endTime - startTime) / 1_000_000;

            System.out.println("Sequential function execution time: " +
                    durationInMilliseconds + " ms. Result - " + result);
            startTime = System.nanoTime();
            result = findMinNumberMnogopotok(testList);
            endTime = System.nanoTime();
            durationInMilliseconds = (endTime - startTime) / 1_000_000;
            System.out.println("Multi-threaded function execution time: " +
                    durationInMilliseconds + " ms. Result - " + result);
            startTime = System.nanoTime();
            result = findMinNumberFork(testList);
            endTime = System.nanoTime();
            durationInMilliseconds = (endTime - startTime) / 1_000_000;
            System.out.println("Fork function execution time: " +
                    durationInMilliseconds + " ms. Result - " + result);
        }
    }
}
