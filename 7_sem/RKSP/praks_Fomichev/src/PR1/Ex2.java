package PR1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Ex2 {
    private static int calculateSquare(int number) {
        int delayInSeconds = ThreadLocalRandom.current().nextInt(1, 6);
        try {
            Thread.sleep(delayInSeconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return number * number;
    }
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        // Для хранения всех задач
        List<Future<Integer>> futureResults = new ArrayList<>();

        while (true) {
            try {
                System.out.println("Enter a number (or 'e' to exit):");
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine();

                if ("e".equalsIgnoreCase(userInput)) {
                    break;
                }

                int number = Integer.parseInt(userInput);

                // Отправляем задачу на выполнение и сохраняем Future в список
                Future<Integer> future = executorService.submit(() -> calculateSquare(number));
                futureResults.add(future);

                // Параллельно проверяем готовность других задач
                for (Iterator<Future<Integer>> iterator = futureResults.iterator(); iterator.hasNext();) {
                    Future<Integer> resultFuture = iterator.next();
                    if (resultFuture.isDone()) {
                        try {
                            int result = resultFuture.get();
                            System.out.println("Result: " + result);
                            iterator.remove(); // Убираем завершенные задачи
                        } catch (InterruptedException | ExecutionException e) {
                            System.err.println("Error executing the request: " + e.getMessage());
                            iterator.remove();
                        }
                    }
                }

            } catch (NumberFormatException e) {
                System.err.println("Invalid number format. Please enter an integer.");
            }
        }
        executorService.shutdown();
    }
}
