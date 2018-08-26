public class Monomial {
    private double coefficient;
    private int degree;

    /**
     * Constructor that creates Monomial with 1 parameter(x) by given coefficient and degree of "x"
     * @param coefficient - the coefficient of Monomial
     * @param degree - the degree of parameter
     */
    Monomial(double coefficient, int degree) {
        this.coefficient = coefficient;
        this.degree = degree;
    }

    /**
     * Basic getter for private field degree
     * @return - the degree of Monomial
     */
    public int getDegree() {
        return this.degree;
    }

    /**
     * Basic getter for private field coefficient
     * @return - the coefficient of Monomial
     */
    public double getCoefficient() {
        return this.coefficient;
    }

    /**
     * This method calculates the result of division this Monomial by the other Monomial
     * We call this method only for Monomials such that this.degree >= other.degree
     * Actually this is helper function for "divide" method of Polynomial class
     * @param other - the Monomial to divide by
     * @return - the quotient after division NOTE: there can't be remainder
     */
    public Monomial getMultiplier(Monomial other) {
        return new Monomial(this.coefficient / other.coefficient, this.degree - other.degree);
    }
}
