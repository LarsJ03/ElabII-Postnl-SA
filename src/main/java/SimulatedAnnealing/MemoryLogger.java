package SimulatedAnnealing;

public class MemoryLogger {
    public static void logMemoryUsage(String message) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Request garbage collection
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(message + " - Used memory: " + memory / (1024 * 1024) + " MB");
    }
}


