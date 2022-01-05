import java.lang.reflect.Array;
import java.util.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import static java.lang.Integer.*;

public class SparsePolynomial implements Polynomial {

    private Map<Integer, Integer> polynomial;
    private String strPolynomial;

    /**
     * Constructor for SparsePolynomial using a map
     * Pre-condition: an Map of <Integer, Integer> representing the coefficients and powers of a polynomial
     * Post-condition: an instance of SparsePolynomial is created using the map
     * @param polynomials
     */
    private SparsePolynomial(Map<Integer, Integer> polynomials)  {
        this.polynomial = simplify(polynomials);
        if(isZero(polynomials)){
            strPolynomial = "0";
        }
        this.polynomial = polynomials;
    }

    /**
     * Pre-condition: a map that supposedly used to become an instance
     * Post-condition: confirmed validation that not all values of  the map == 0
     * Checks if given parameter contains all zeros
     * @param polynomials map representing the coefficients and powers of a polynomial
     */
    private boolean isZero(Map<Integer, Integer> polynomials) {
        for(Map.Entry<Integer, Integer> e: polynomials.entrySet()){
            if( e.getValue() != 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Another constructor for SparsePolynomial using a string instead
     * Pre-condition: a string polynomial
     * Post-condition: an stance of SparsePolynomial is created using given string
     * The string is split up by spaces and then by 'x' and '^' to obtain the coefficients and index of the new map
     * Coefficients -> values and powers -> keys
     * @param equation a string that represents a polynomial
     */
    public SparsePolynomial(String equation){
        strPolynomial = equation;
        validPolynomial();
        String[] parts = equation.split(" ");
        polynomial = new TreeMap<Integer, Integer>(Collections.reverseOrder());

        // trim parts
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        int coefficient;

        for (int i = 0; i < parts.length; i += 2) {
            if (parts[i].contains("^")) {
                if (parts[i].indexOf("x") == 0) {
                    coefficient = 1;
                }
                else {
                    if(parts[i].substring(0, parts[i].indexOf("x")).matches(".*\\d.*")){
                        coefficient = parseInt(parts[i].substring(0, parts[i].indexOf("x")));
                    }
                    else{
                        if(parts[i].substring(0, parts[i].indexOf("x")).contains("-")){
                            coefficient = -1;
                        }
                        else{
                            coefficient = 1;
                        }
                    }
                }
                int exponent = parseInt(parts[i].substring(parts[i].indexOf("^") + 1));
                if(polynomial.containsKey(exponent)){
                    int value = polynomial.get(exponent);
                    polynomial.remove(exponent);
                    value += coefficient;
                    polynomial.put(exponent, value);
                }
                else{
                    polynomial.put(exponent, coefficient);
                }

            } else if (parts[i].contains("x")) {
                if (parts[i].indexOf("x") == 0) {
                    coefficient = 1;
                } else {
                    if(parts[i].substring(0, parts[i].indexOf("x")).equals("")){
                        coefficient = 1;
                    }
                    else if(parts[i].substring(0, parts[i].indexOf("x")).contains("-") && parts[i].substring(0, parts[i].indexOf("x")).length() == 1){
                        coefficient = -1;
                    }
                    else if (parts[i].substring(0, parts[i].indexOf("x")).contains("-")) {
                        coefficient = -1 * parseInt(parts[i].substring(1, parts[i].indexOf("x")));
                    }
                    else {
                        coefficient = parseInt(parts[i].substring(0, parts[i].indexOf("x")));
                    }
                }
                if(polynomial.containsKey(1)){
                    int value = polynomial.get(1);
                    polynomial.remove(1);
                    value += coefficient;
                    polynomial.put(1, value);
                }
                else{
                    polynomial.put(1, coefficient);
                }
            } else {
                if(polynomial.containsKey(0)){
                    int value = polynomial.get(0);
                    polynomial.remove(0);
                    value += parseInt(parts[i]);
                    polynomial.put(0, value);
                }
                else{
                    polynomial.put(0, parseInt(parts[i]));
                }
            }
        }
        this.polynomial = simplify(polynomial);
    }

    /**
     * Pre-condition: string representation of polynomial that isn't "" (same as wellformed)
     * Post-condition: validation if strPolynomial is a polynomial
     * Pre-condition for constructor to see if polynomial is wellformed
     */
    private void validPolynomial(){
        assert wellFormed() : "THe string is not a valid polynomial.";
    }

    /**
     * Pre-condition: an instance of SparsePolynomial is created
     * Post-condition: returning an integer representing the largest exponent power of the polynomial
     * Returns the largest degree of the polynomial
     * The first key of the map == largest exponent of the polynomial
     * @return the largest degree of the polynomial
     */
    @Override
    public int degree() {
        for (Map.Entry<Integer, Integer> e : polynomial.entrySet()) {
            if(e.getValue() != 0) {
                return e.getKey();
            }
        }
        return 0;
    }

    /**
     * Pre-condition: an integer representing a power in the polynomial
     * Post-condition: the value of the given integer/key in the map
     * Gets the coefficient of a term depending on the given exponent
     * Key <==> exponent hence same key/exponent -> return value
     * @param d the exponent whose coefficient is returned.
     * @return the coefficient of term with given d/power; returns -1 if power not found
     */
    @Override
    public int getCoefficient(int d) {
        for (Map.Entry<Integer, Integer> e : polynomial.entrySet()) {
            if (e.getKey() == d) {
                return e.getValue();
            }
        }
        return 0;
    }

    /**
     * Pre-condition: an instance of SparsePolynomial is created
     * Post-condition: boolean representation of if polynomial == 0
     * Checks if the polynomial map is zero
     * Polynomial == 0 when smallest index of array == 0
     * @return t/f if polynomial == 0
     */
    @Override
    public boolean isZero() {
        for (Map.Entry<Integer, Integer> e : polynomial.entrySet()) {
            if (e.getValue() != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Pre-condition: instance polynomial, q
     * Post-condition: instance of polynomial after adding q and polynomial
     * Adding q and polynomial into new map and creating SparsePolynomial instance from it
     * @param q the non-null polynomial to add to this.polynomial
     * @return new instance of SparsePolynomial of sum q and polynomial
     * @throws NullPointerException thrown when q == null
     */
    @Override
    public Polynomial add(Polynomial q) throws NullPointerException{
        if(q == null){
            throw new NullPointerException("There is nothing to add with.");
        }
        SparsePolynomial sp = (SparsePolynomial) q;
        Map<Integer, Integer> sum = new TreeMap<Integer, Integer>(Collections.reverseOrder());
        for (Map.Entry<Integer, Integer> i : polynomial.entrySet()) {
            for (Map.Entry<Integer, Integer> j : (sp.polynomial.entrySet())) {
                if (i.getKey() == j.getKey()) {
                    sum.put(i.getKey(), i.getValue() + j.getValue());
                }
            }
        }

        for (Map.Entry<Integer, Integer> i : polynomial.entrySet()) {
            if (!sum.containsKey(i.getKey())) {
                sum.put(i.getKey(), i.getValue());
            }
        }

        for (Map.Entry<Integer, Integer> i : (sp.polynomial.entrySet())) {
            if (!sum.containsKey(i.getKey())) {
                sum.put(i.getKey(), i.getValue());
            }
        }
        sum = simplify(sum);
        return new SparsePolynomial(sum);
    }

    /**
     * Pre-condition: instance of polynomial, q
     * Post-condition: instance of polynomial after multiplying q and polynomial
     * Multiplying q and polynomial resulting into new map and creating SparsePolynomial instance from it
     * @param q the polynomial to multiply with this.polynomial
     * @return new instance of SparsePolynomial of product q and polynomial
     * @throws NullPointerException thrown when q == null
     */
    @Override
    public Polynomial multiply(Polynomial q) throws NullPointerException{
        if(q == null){
            throw new NullPointerException("There is nothing to multiply with.");
        }

        SparsePolynomial sp = (SparsePolynomial) q;
        Map<Integer, Integer> product = new TreeMap<Integer, Integer>(Collections.reverseOrder());
        Map<Integer, Integer> temp;
        Polynomial result = new SparsePolynomial(product);

        for (Map.Entry<Integer, Integer> e : polynomial.entrySet()) {
            temp = new TreeMap<Integer, Integer>(Collections.reverseOrder());
            for (Map.Entry<Integer, Integer> f : sp.polynomial.entrySet()) {
                temp.put(e.getKey() + f.getKey(), e.getValue() * f.getValue());
            }
            result = result.add(new SparsePolynomial(temp));
        }
        return result;
    }

    /**
     * Pre-condition: instance of polynomial, q
     * Post-condition: instance of polynomial after subtracting q from polynomial
     * Subtracting q from polynomial resulting into new map and creating SparsePolynomial instance from it
     * @param q the polynomial to subtract from this.polynomial
     * @return new instance of SparsePolynomial of difference polynomial and q
     * @throws NullPointerException thrown when q == null
     */
    @Override
    public Polynomial subtract(Polynomial q) throws NullPointerException{
        if(q == null){
            throw new NullPointerException("There is nothing to subtract with.");
        }

        SparsePolynomial sp = (SparsePolynomial) q;
        Map<Integer, Integer> difference = new TreeMap<Integer, Integer>(Collections.reverseOrder());
        for (Map.Entry<Integer, Integer> i : polynomial.entrySet()) {
            for (Map.Entry<Integer, Integer> j : sp.polynomial.entrySet()) {
                if (i.getKey() == j.getKey()) {
                    difference.put(i.getKey(), i.getValue() - j.getValue());
                }
            }
        }
        for (Map.Entry<Integer, Integer> i : polynomial.entrySet()) {
            if (!difference.containsKey(i.getKey())) {
                difference.put(i.getKey(), i.getValue());
            }
        }
        for (Map.Entry<Integer, Integer> i : sp.polynomial.entrySet()) {
            if (!difference.containsKey(i.getKey())) {
                difference.put(i.getKey(), i.getValue() * -1); }
        }
        difference = simplify(difference);
        return new SparsePolynomial(difference);
    }

    /**
     * Pre-condition: polynomial instance
     * Post-condition: instance of polynomial after dividing polynomial with q
     * @param q the polynomial instance to divide with this.polynomial
     * @return new instance of SparsePolynomial of quotient polynomial and q
     * @throws NullPointerException thrown when q == null
     */

    public Polynomial divide(Polynomial q)throws NullPointerException{
        // key == exponent
        // value == coefficient

        if(q == null){
            throw new NullPointerException("There is nothing to subtract with.");
        }

        SparsePolynomial toBeDividedWith = (SparsePolynomial) q;
        SparsePolynomial result = null;

        ArrayList<Integer> divisorCoeff = new ArrayList<>(toBeDividedWith.polynomial.values());
        ArrayList<Integer> divisorExpo = new ArrayList<>(toBeDividedWith.polynomial.keySet());
        ArrayList<Integer> dividendCoeff = new ArrayList<>(polynomial.values());
        ArrayList<Integer> dividendExpo = new ArrayList<>(polynomial.keySet());
        ArrayList<Integer> copyDivisorCoeff = new ArrayList<>(toBeDividedWith.polynomial.values());
        ArrayList<Integer> copyDivisorExpo =  new ArrayList<>(toBeDividedWith.polynomial.keySet());
        ArrayList<Integer> newExpo = new ArrayList<>();
        ArrayList<Integer> newCoeff = new ArrayList<>();

        int coeffMultiplier, expoMultiplier;
        int i = 0;
        while(i <= dividendCoeff.size() - divisorCoeff.size()){
            copyDivisorExpo = new ArrayList<>(toBeDividedWith.polynomial.keySet());
            copyDivisorCoeff = new ArrayList<>(toBeDividedWith.polynomial.values());
            coeffMultiplier = (int) dividendCoeff.get(i) / copyDivisorCoeff.get(i);//
            expoMultiplier = (int) dividendExpo.get(i) - copyDivisorExpo.get(i);
            newExpo.add(expoMultiplier);
            newCoeff.add(coeffMultiplier);

            for(int j = 0; j < copyDivisorCoeff.size(); j++){
                copyDivisorCoeff.set(j, copyDivisorCoeff.get(j)*coeffMultiplier);
                copyDivisorExpo.set(j, copyDivisorExpo.get(j)+expoMultiplier);
            }

            for(int j = 0; j < copyDivisorCoeff.size(); j++){
                    dividendCoeff.set(j, dividendCoeff.get(j) - copyDivisorCoeff.get(j));
                }

            for(int j = 0; j < dividendCoeff.size(); j++){
                if(dividendCoeff.get(j)==0){
                    dividendCoeff.remove(j);
                    dividendExpo.remove(j);
                    j--;
                }
            }
        }

        String strResult = doubleArrayListToString(newCoeff, newExpo, "");

        if(dividendCoeff.size() == 0){
            result = new SparsePolynomial(strResult);
            return result;
        }
        else{
            String roundedResult = strResult;
            strResult += " + (";
            strResult = doubleArrayListToString(dividendCoeff, dividendExpo, strResult);
            strResult += "/(";
            strResult = doubleArrayListToString(divisorCoeff, divisorExpo, strResult);
            strResult += "))";
            System.out.println("The unrounded result is: "+strResult);
            result = new SparsePolynomial(roundedResult);
            return result;
        }
    }

    /**
     * Pre-condition: parameters
     * Post-condition: string representing polynomial instance
     * @param Coeff, an Arraylist of all coefficients of a polynomial equation in order
     * @param Expo, an ArrayList of all exponent values of a polynomial equation in order
     * @param strResult, the string variable that the newly formatted polynomial equation would be added to
     * @return string of the provided string value in addition to a new polynomial equation derived from the two ArrayLists
     */

    public String doubleArrayListToString(ArrayList<Integer> Coeff, ArrayList<Integer> Expo, String strResult){
        for(int k = 0; k < Coeff.size(); k++){
            strResult += Coeff.get(k);
            if(Expo.get(k) >= 1){
                strResult += "x";
                if(Expo.get(k) > 1){
                    strResult += "^" + Expo.get(k);
                }
                strResult += " + ";
            }
        }
        return strResult;
    }

    /**
     * Pre-condition: instance of polynomial
     * Post-condition: new instance of the negative from original polynomial
     * Gets the negative of the polynomial
     * The negative of a polynomial is polynomial itself * -1
     * @return new instance of SparsePolynomial from original polynomial * -1 in values
     */
    @Override
    public Polynomial minus() {
        Map<Integer, Integer> negative = new TreeMap<Integer, Integer>(Collections.reverseOrder());
        for (Map.Entry<Integer, Integer> e : polynomial.entrySet()) {
            negative.put(e.getKey(), e.getValue() * -1);
        }
        return new SparsePolynomial(negative);
    }

    /**
     * Pre-condition: string representation of polynomial that isn't ""
     * Post-condition: t/f of if string is a valid polynomial
     * Checks if polynomial is valid
     * Valid polynomial == no non-integers, contains x
     * Does not need to check for constructor(array) since the array will always have ints because of the rules of
     * Operators, int + int = int, int - int = int, int * int = int
     * @return t/f depending if polynomial has integer coefficients and power
     */
    @Override
    public boolean wellFormed() {
        if(strPolynomial.equals("")){ // array representation of polynomial is already only int and created
            return false; // aka pre-condition
        }
        if(strPolynomial.contains("/") || strPolynomial.contains(".")){ // no non-integers
            return false;
        }
        if(strPolynomial.indexOf('x') == -1){ // contains x
            return false;
        }
        if(strPolynomial.contains("(") || strPolynomial.contains(")")){ // no "()"
            return false;
        }
        if(strPolynomial.contains("]") || strPolynomial.contains("[")){ // no "[]"
            return false;
        }
        return true;
    }

    /**
     * Pre-condition: polynomial instance
     * Post-condition: string representing polynomial instance
     * Adds "x" to right of coefficient and "^" to right with power if necessary
     * @return string representation of polynomial instance
     */
    public String toString() {
        if (isZero()) {
            strPolynomial = "0";
            return "0";
        } else {
            String result = "";
            boolean first = true;

            for (Map.Entry<Integer, Integer> e : polynomial.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    result += " + ";
                }
                if (e.getKey() == 0) {
                    result += e.getValue();
                } else if (e.getKey() == 1) {
                    result += e.getValue() + "x";
                } else if (e.getKey() >= 2) {
                    result += e.getValue() + "x^" + e.getKey();
                }
            }
            strPolynomial = result;
            return result;
        }
    }

    /**
     * Pre-condition: o is not null
     * Post-condition: t/f depending if o == this
     * Checks if given parameter and this are equal
     * Lengths of maps, values of keys and values must match
     * @param o object to check if is equal to this
     * @return t/f depending if o == this and its values
     */

    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(o == null){
            return false;
        }
        if(o instanceof SparsePolynomial){
            SparsePolynomial sp = (SparsePolynomial) o;
            if(sp.isZero() == true && sp.isZero() == isZero()){
                return true;
            }
            if(sp.polynomial.size() != polynomial.size()){
                return false;
            }

            int i = 0;
            for(Map.Entry<Integer,Integer> a: polynomial.entrySet()){
                int k = 0;
                for(Map.Entry<Integer,Integer> b: sp.polynomial.entrySet()){
                    if(i == k){
                        if(a.getKey() != b.getKey() || a.getValue() != b.getValue()){
                            return false;
                        }
                    }
                    k++;
                }
                i++;
            }
            return true;
        }
        return false;
    }

    /**
     * Pre-condition: Treemap representing a polynomial
     * Post-condition: the same treemap without 0s in the values value
     * Simplifies a polynomial based off its coefficient value
     * @param m map representing a polynomial
     * @return treemap representing a polynomial without 0s in the values value
     */

    private Map<Integer,Integer> simplify(Map<Integer,Integer> m){
        ArrayList<Integer> toRemove = new ArrayList<>();
        for(Map.Entry<Integer,Integer> e: m.entrySet()){
            if(e.getValue() == 0){
                toRemove.add(e.getKey());
            }
        }
        for(int i = 0; i < toRemove.size(); i++){
            m.remove(toRemove.get(i));
        }
        return m;
    }
}
