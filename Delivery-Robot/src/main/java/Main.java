import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (sizeToFreq) {
                for (int i = 0; i < 100; i++) {
                    String text = generateRoute("RLRFR", 100);

                    int maxSize = 0;
                    for (int j = 0; j < text.length(); j++) {
                        if (text.charAt(j) == 'R') {
                            maxSize++;
                        }
                    }
                    if (sizeToFreq.containsKey(maxSize)) {
                        int count = 1 + sizeToFreq.get(maxSize);
                        sizeToFreq.replace(maxSize, count);
                    } else {
                        sizeToFreq.put(maxSize, 1);
                    }

                }
                sizeToFreq.notify();
            }
        }).start();

        synchronized (sizeToFreq) {
            if (sizeToFreq.isEmpty()) {
                try {
                    sizeToFreq.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Самое частое количество повторений "
                    + sizeToFreq.keySet().stream().max(Comparator.comparing(sizeToFreq::get)).orElse(null)
                    + " (встретилось " + Collections.max(sizeToFreq.values()) + " раз)");
            System.out.println("Другие размеры:");
            for (Map.Entry<Integer, Integer> pair : sizeToFreq.entrySet()) {
                System.out.println("- " + pair.getKey() + " (" + pair.getValue() + " раз)");
            }
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
