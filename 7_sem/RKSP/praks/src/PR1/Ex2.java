package PR1;

import java.util.Scanner;
import java.util.concurrent.*;

public class Ex2 {
    public static void main(String[] args) {
        // ������� ��� �������
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        System.out.println("Enter a number (or 'e' to exit):");
        // ������� ����������� ���� ��� ��������� ��������
        while (true) {
            try {
                // ����������� ����� � ������������
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine();
                // ���������, ���� ������������ ����� �����
                if ("e".equalsIgnoreCase(userInput)) {
                    break;
                }
                // ����������� ��������� �������� � �����
                int number = Integer.parseInt(userInput);
                // ������� ������ ��� ���������� �������� ����� � ���������� � ��� �������
                Future<Integer> future = executorService.submit(() ->
                        calculateSquare(number));
                // ������� ���������� ������ � �������� ���������
                try {
                    int result = future.get();
                    System.out.println("Result: " + result);
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Error executing the request: " + e.getMessage());
                }

            } catch (NumberFormatException e) {
                System.err.println("Invalid number format. Please enter an integer.");
            }
        }
        // ��������� ��� �������
        executorService.shutdown();
    }
    private static int calculateSquare(int number) {
        // ��������� �������� �� 1 �� 5 ������
        int delayInSeconds = ThreadLocalRandom.current().nextInt(1, 6);
        try {
            Thread.sleep(delayInSeconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // ���������� ������� �����
        return number * number;
    }
}

