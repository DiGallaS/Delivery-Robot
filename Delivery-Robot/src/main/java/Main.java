import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    static List<Thread> threads = new ArrayList<>();

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
                }
            };

            Thread thread = new Thread(logic);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Самое частое количество повторений "
                + sizeToFreq.keySet().stream().max(Comparator.comparing(sizeToFreq::get)).orElse(null)
                + " (встретилось " + Collections.max(sizeToFreq.values()) + " раз)");

        System.out.println("Другие размеры:");

        for (Map.Entry<Integer, Integer> pair : sizeToFreq.entrySet()) {
            System.out.println("- " + pair.getKey() + " (" + pair.getValue() + " раз)");
        }
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
