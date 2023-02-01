import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    static List<Thread> threads = new ArrayList<>();
    static Thread thread;
    static Thread thread1;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Runnable logic = () -> {
                String text = generateRoute("RLRFR", 100);
                int maxSize = 0;
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == 'R') {
                        maxSize++;
                    }
                }
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(maxSize)) {
                        int count = 1 + sizeToFreq.get(maxSize);
                        sizeToFreq.replace(maxSize, count);
                    } else {
                        sizeToFreq.put(maxSize, 1);
                    }
                    sizeToFreq.notify();
                }
            };
            thread = new Thread(logic);
            threads.add(thread);
        }

        Runnable logic1 = () -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Самое частое количество повторений "
                            + sizeToFreq.keySet().stream().max(Comparator.comparing(sizeToFreq::get)).orElse(null)
                            + " (встретилось " + Collections.max(sizeToFreq.values()) + " раз)");
                }
            }
        };
        thread1 = new Thread(logic1);
        thread1.start();

        for (Thread thread : threads) {
            thread.start();
            thread.join();
        }
        thread1.interrupt();
    }


    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
