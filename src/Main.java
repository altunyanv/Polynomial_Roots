import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Polynomial polynomial = new Polynomial(new double[]{-2, 1, 0, -4, 0, 0, 1});
        System.out.println(polynomial);
        ArrayList<Double> roots = polynomial.rootsInInterval(-2,2, 1e-5);
        System.out.println(roots);
    }
}
