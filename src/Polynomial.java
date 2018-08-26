import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;

public class Polynomial
{
    /**
     * Class is implemented so that at each time coefficients.length = degree + 1
     */
    private double[] coefficients;
    private int degree;

    /**
     * Constructor that creates a Polynomial  with given degree and with all coefficients equal to 0
     * NOTE: This constructor is only for local computations so it's private constructor
     * @param degree - the degree of Polynomial
     */
    private Polynomial(int degree) {
        if (degree < 0)
            degree = 0;
        this.coefficients = new double[degree + 1];
        this.degree = degree;
        Arrays.fill(this.coefficients, 0);
    }

    /**
     * Constructor that creates a Polynomial by given coefficients array
     * Here coefficients[i] is the coefficient of x^i
     * This constructor is the main way we shall create Polynomials
     * @param coefficients - array of coefficients of Polynomial
     */
    Polynomial(double[] coefficients) {
        this.coefficients = new double[coefficients.length];
        this.degree = coefficients.length - 1;
        System.arraycopy(coefficients, 0, this.coefficients, 0,this.degree + 1);
    }

    /**
     * Basic getter for private field degree
     * @return - the degree of Polynomial
     */
    public int getDegree() {
        return this.degree;
    }

    /**
     * Basic getter for private field coefficients
     * @return - array of coefficients of Polynomial
     */
    public double[] getCoefficients() {
        return this.coefficients;
    }

    /**
     * Method from base class(Object): Checks whether two Polynomials are equal or not
     * @param obj - the object to compare with
     * @return - boolean value : True -> if Polynomials are equal and False otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Polynomial) && Arrays.equals(this.coefficients, ((Polynomial) obj).coefficients);
    }

    /**
     * Method from base class(Object): Creates the String representation of Polynomial
     * @return - String representation of Polynomial
     */
    @Override
    public String toString() {
        StringBuilder expression = new StringBuilder();
        for(int i = this.degree; i >= 0; i--) {
            if (this.coefficients[i] == 0)
                continue;

            if (this.coefficients[i] > 0 && i != this.degree)
                expression.append("+");

            if (Math.abs(this.coefficients[i]) == 1.0) {
                if (this.coefficients[i] == -1.0)
                    expression.append("-");
            }
            else if (this.coefficients[i] == (long) this.coefficients[i])
                expression.append(String.format("%d", (long) this.coefficients[i]));
            else
                expression.append(String.format("%s", this.coefficients[i]));

            if (i != 0) {
                expression.append("x");
                if (i != 1) {
                    expression.append("^");
                    expression.append(((Integer) i).toString());
                }
            }
        }
        return expression.toString();
    }

    /**
     * This method calculates the value of Polynomial at some point "x"
     * @param x - the point to calculate for
     * @return - the value of Polynomial at point "x"
     */
    public double valueAt(double x) {
        double result = this.coefficients[this.degree];
        for(int i = this.degree - 1; i >= 0; i--)
            result = result * x + this.coefficients[i];
        return result;
    }

    /**
     * Method to add two Polynomials
     * @param other - the Polynomial to add to this
     * @return - sum of this and other Polynomials
     */
    public Polynomial add(Polynomial other) {
        int resultDegree = Math.max(this.degree, other.degree);
        if (this.degree == other.degree)
            while(resultDegree >= 0 && this.coefficients[resultDegree] == -other.coefficients[resultDegree])
                resultDegree--;

        Polynomial result = new Polynomial(resultDegree);

        for(int i = 0; i <= result.degree; i++)
            result.coefficients[i] =    (i <= this.degree ? this.coefficients[i] : 0) +
                                        (i <= other.degree ? other.coefficients[i] : 0);

        return result;
    }

    /**
     * Method to subtract a Polynomial from this
     * @param other - the Polynomial to subtract from this
     * @return - difference of this and other Polynomials
     */
    public Polynomial subtract(Polynomial other) {
        int resultDegree = Math.max(this.degree, other.degree);
        if (this.degree == other.degree)
            while(resultDegree >= 0 && this.coefficients[resultDegree] == other.coefficients[resultDegree])
                resultDegree--;

        Polynomial result = new Polynomial(resultDegree);

        for(int i = 0; i <= result.degree; i++)
            result.coefficients[i] =    (i <= this.degree ? this.coefficients[i] : 0) -
                                        (i <= other.degree ? other.coefficients[i] : 0);

        return result;
    }

    /**
     * Method to multiply two Polynomials
     * @param other - thw Polynomial to multiply by this
     * @return - the product of this and other Polynomials
     */
    public Polynomial multiply(Polynomial other) {
        Polynomial result = new Polynomial(this.degree + other.degree);

        for(int i = 0; i <= this.degree; i++)
            for(int j = 0; j <= other.degree; j++)
                result.coefficients[i + j] += this.coefficients[i] * other.coefficients[j];

        return result;
    }

    /**
     * Method to multiply Polynomial by Monomial
     * @param other - thw Monomial to multiply by this
     * @return - the product of this(Polynomial) and other(Monomial)
     */
    private Polynomial multiply(Monomial other) {
        Polynomial result = new Polynomial(this.degree + other.getDegree());

        for(int i = this.degree; i >= 0; i--)
            result.coefficients[i + other.getDegree()] = this.coefficients[i] * other.getCoefficient();

        return result;
    }

    /**
     * Method to divide Polynomials
     * @param other - the divisor Polynomial
     * @return - Pair of two Polynomials: First -> quotient and Second -> remainder after division
     */
    private Pair<Polynomial, Polynomial> divide(Polynomial other) {
        Polynomial remainder = new Polynomial(this.coefficients), quotient = new Polynomial(this.degree - other.degree);
        Monomial otherLeading = new Monomial(other.coefficients[other.degree], other.degree);

        while(remainder.degree >= other.degree) {
            Monomial multiplier = (new Monomial(remainder.coefficients[remainder.degree], remainder.degree)).getMultiplier(otherLeading);
            remainder = remainder.subtract(other.multiply(multiplier));
            quotient.coefficients[multiplier.getDegree()] = multiplier.getCoefficient();
        }

        return new Pair<>(quotient, remainder);
    }

    /**
     * Method that derives this Polynomial
     */
    private void derive() {
        if (this.degree == 0) {
            this.coefficients[0] = 0;
            return;
        }
        this.degree--;
        double[] temporary = this.coefficients;
        this.coefficients = new double[this.degree + 1];
        for(int i = 0; i <= this.degree; i++)
            this.coefficients[i] = temporary[i + 1] * (i + 1);
    }

    /**
     * This method counts the sign changes in polynomials list at point "point"
     * @param polynomials - the list of Polynomials
     * @param point - the point to calculate values at
     * @return - the count of sign changes if there isn't any 0 and -1 otherwise
     */
    private static int getSigma(ArrayList<Polynomial> polynomials, double point) {
        int count = 0;
        double previous = 0;
        for(int i = 0; i < polynomials.size(); i++) {
            double current = polynomials.get(i).valueAt(point);

            if (current == 0)
                return -1;

            if (i != 0 && (current > 0) ^ (previous > 0))
                count++;

            previous = current;
        }
        return count;
    }

    /**
     * This method prepares the Polynomial chain to use during algorithm
     * Chain is created by given algorithm
     * p_0(x) = p(x)
     * p_1(x) = p'(x)
     * ...
     * p_i(x) = -remainder(p_{i - 2}, p_{i - 1})
     * ... till some p_{m} comes out constant
     * @return - the resulting Polynomial chain
     */
    private ArrayList<Polynomial> getPolynomialChain() {
        ArrayList<Polynomial> chain = new ArrayList<>();

        chain.add(this);
        chain.add(new Polynomial(this.coefficients));
        chain.get(1).derive();

        int lowestDegree = chain.get(1).degree, size = 2;
        while(lowestDegree != 0) {
            Polynomial nextInChain = (new Polynomial(0)).subtract(chain.get(size - 2).divide(chain.get(size - 1)).getValue());
            lowestDegree = nextInChain.degree;
            size++;
            chain.add(nextInChain);
        }

        return chain;
    }

    /**
     * This method is the function to call from main.
     * Initially algorithm will find all distinct roots of Polynomial in inclusive range
     * [leftBound, rightBound] with maximal error of precision
     * First we call the chain preparing method
     * @param leftBound - the left bound of roots to be found
     * @param rightBound - the right bound of roots to be found
     * @param precision - the maximal error for any of answers, don't use smaller than 1e-8
     * @return - the ArrayList of distinct roots of Polynomial in given range
     */
    public ArrayList<Double> rootsInInterval(double leftBound, double rightBound, double precision) {
        ArrayList<Polynomial> chain = this.getPolynomialChain();

        while(Polynomial.getSigma(chain, rightBound) == -1)
            rightBound += (1e-9);

        while(Polynomial.getSigma(chain, leftBound) == -1)
            rightBound -= (1e-9);

        return rootsRecursive(leftBound, rightBound, precision, chain);
    }

    /**
     * The recursive function based on "divide and conquer" strategy
     * Function checks if the halves of range have any roots inside
     * and calls itself for the halves that have them
     * After all combines all roots found from different halves
     * For more information search Sturm's Theorem
     * Link: https://en.wikipedia.org/wiki/Sturm%27s_theorem
     * @param leftBound - the left bound of roots to be found
     * @param rightBound - the right bound of roots to be found
     * @param precision - the maximal error for any of answers
     * @param chain - Polynomial chain previosly prepared for this algorithm
     * @return - the ArrayList of distinct roots of Polynomial in given range
     */
    private ArrayList<Double> rootsRecursive(double leftBound, double rightBound, double precision, ArrayList<Polynomial> chain) {
        double middleBound = (leftBound + rightBound) / 2;
        ArrayList<Double> results = new ArrayList<>();

        if ((rightBound - leftBound) < precision) {
            results.add(middleBound);
        }
        else {
            while (Polynomial.getSigma(chain, middleBound) == -1)
                middleBound += (1e-9);

            int     right = Polynomial.getSigma(chain, rightBound),
                    left = Polynomial.getSigma(chain, leftBound),
                    middle = Polynomial.getSigma(chain, middleBound);

            if (middle < left)
                results.addAll(rootsRecursive(leftBound, middleBound, precision, chain));
            if (middle > right)
                results.addAll(rootsRecursive(middleBound, rightBound, precision, chain));
        }

        return results;
    }
}
