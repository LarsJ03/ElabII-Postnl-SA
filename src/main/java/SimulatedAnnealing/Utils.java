package SimulatedAnnealing;

import java.util.ArrayList;

public class Utils {

    public static <T> ArrayList<T> deepCopy(ArrayList<T> original) {
        ArrayList<T> copy = new ArrayList<>(original.size());
        for (T item : original) {
            copy.add(item);
        }
        return copy;
    }

    public static double[][] deepCopy(double[][] original) {
        if (original == null) {
            return null;
        }
        double[][] result = new double[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i].clone();
        }
        return result;
    }

    public static ServiceLocationConfig deepCopy(ServiceLocationConfig original) {
        return new ServiceLocationConfig(
                Utils.deepCopy(original.getServicelocations()),
                Utils.deepCopy(original.getRoads()),
                Utils.deepCopy(original.getDistances()),
                false
        );
    }
}
