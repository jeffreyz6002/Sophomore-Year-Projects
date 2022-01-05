import javax.swing.*;
import static org.junit.jupiter.api.Assertions.*;

// Must enable assertions in VM Options
// Add "-ea" to VM Options while editing configurations
// Do Divide

class SparsePolynomialTest {
    public static void main (String [] args){
        System.out.println("SparsePolynomialTest");

        SparsePolynomial sp1 = new SparsePolynomial("3x^3 + 2x^2");
        SparsePolynomial sp2 = new SparsePolynomial("3x^3 + 2x^2");
        SparsePolynomial sp3 = new SparsePolynomial("2x^3 + 4x + -2");
        SparsePolynomial negativeSP1 = new SparsePolynomial("-3x^3 + -2x^2");
        SparsePolynomial negative = new SparsePolynomial("-2x^3 + -3");
        SparsePolynomial fakeZero = new SparsePolynomial("0x + 0");
        SparsePolynomial fakeZero2 = new SparsePolynomial("0x^3");
        SparsePolynomial linear = new SparsePolynomial("4x");
        SparsePolynomial negPower = new SparsePolynomial("-3x^-2 + -x + 6");
        SparsePolynomial extendedSP1 = new SparsePolynomial("3x^3 + 2x^2 + 0");
        SparsePolynomial negDividend = new SparsePolynomial("4x^4 - 13x^3 + 12x^2 - 10x - 12");
        SparsePolynomial negDivisor = new SparsePolynomial("-3x^2 + 4x - 6");
        SparsePolynomial dividend1 = new SparsePolynomial("x^3 + 6x^2 + 11x + 6");
        SparsePolynomial dividend2 = new SparsePolynomial("x^3 + 6x^2 + 11x + 8");
        SparsePolynomial divisor = new SparsePolynomial("x^2 + 5x + 6");

        testEquals(sp1, sp2, sp3, fakeZero, fakeZero2, extendedSP1);
        testToString(sp1,sp3,fakeZero);
        testDegree(sp1, linear, negPower);
        testGetCoefficient(sp1, negPower);
        testIsZero(sp1, negativeSP1,fakeZero);
        testTimesMius(sp1, negativeSP1);
        testAdd(sp1, negativeSP1, sp3, negative, fakeZero);
        testMultiply(sp1, sp3, negativeSP1, fakeZero);
        testSubtract(sp1, sp3, negativeSP1, fakeZero);
        testDivide(dividend1, dividend2, divisor, negDividend, negDivisor);
        testNullPointerExceptionWithOperator(sp2);
        testWellFormed();

    }

    public static void testEquals(SparsePolynomial sp1, SparsePolynomial sp2, SparsePolynomial sp3, SparsePolynomial fakeZero, SparsePolynomial fakeZero2, SparsePolynomial extendedSP1) {
        assertTrue(sp1.equals(sp2));
        assertTrue(sp1.equals(sp1));
        assertFalse(sp1.equals(sp3));
        assertFalse(sp1.equals(null));
        assertTrue(fakeZero.equals(fakeZero2));
        assertTrue(extendedSP1.equals(sp1));
    }

    public static void testToString(SparsePolynomial sp1, SparsePolynomial sp3, SparsePolynomial fakeZero){
        assertEquals(sp1.toString(),"3x^3 + 2x^2");
        assertEquals(sp3.toString(), "2x^3 + 4x + -2");
        assertEquals(fakeZero.toString(),"0");
    }

    public static void testDegree(SparsePolynomial sp1, SparsePolynomial linear, SparsePolynomial negPower){
        assertEquals(sp1.degree(),3);
        assertEquals(linear.degree(), 1);
        assertEquals(negPower.degree(), 1);
    }

    public static void testGetCoefficient(SparsePolynomial sp1, SparsePolynomial negPower){
        assertEquals(sp1.getCoefficient(3), 3);
        assertEquals(negPower.getCoefficient(-2),-3);
    }

    public static void testIsZero(SparsePolynomial sp1, SparsePolynomial negativeSP1, SparsePolynomial fakeZero){
        assertFalse(sp1.isZero());
        assertFalse(negativeSP1.isZero());
        assertTrue(fakeZero.isZero());
    }

    public static void testTimesMius(SparsePolynomial sp1, SparsePolynomial negativeSP1){
        assertEquals(sp1.minus(), negativeSP1);
        assertEquals(negativeSP1.minus(), sp1);
    }

    public static void testAdd(SparsePolynomial sp1, SparsePolynomial negativeSP1, SparsePolynomial sp3, SparsePolynomial negative, SparsePolynomial fakeZero){
        assertEquals(sp1.add(negativeSP1).toString(), "0");
        assertEquals(sp1.add(sp3), new SparsePolynomial("5x^3 + 2x^2 + 4x + -2"));
        assertEquals(sp1.add(negative), new SparsePolynomial("1x^3 + 2x^2 + -3"));
        assertEquals(negative.add(negativeSP1), new SparsePolynomial("-5x^3 + -2x^2 + -3"));
        assertEquals(fakeZero.add(sp1), sp1);
    }

    public static void testMultiply(SparsePolynomial sp1, SparsePolynomial sp3, SparsePolynomial negativeSP1, SparsePolynomial fakeZero){
        assertEquals(sp1.multiply(sp3), new SparsePolynomial("6x^6 + 4x^5 + 12x^4 + 2x^3 + -4x^2"));
        assertEquals(negativeSP1.multiply(sp1), new SparsePolynomial("-9x^6 + -12x^5 + -4x^4"));
        assertEquals(sp1.multiply(fakeZero), fakeZero);
    }

    public static void testSubtract(SparsePolynomial sp1, SparsePolynomial sp3, SparsePolynomial negativeSP1, SparsePolynomial fakeZero){
        assertEquals(sp1.subtract(sp1).toString(), "0");
        assertEquals(sp1.subtract(sp3), new SparsePolynomial("1x^3 + 2x^2 + -4x + 2"));
        assertEquals(negativeSP1.subtract(sp1), new SparsePolynomial("-6x^3 + -4x^2"));
        assertEquals(sp1.subtract(fakeZero), sp1);
        assertEquals(fakeZero.subtract(sp1), negativeSP1);
    }

    public static void testDivide(SparsePolynomial dividend1, SparsePolynomial dividend2, SparsePolynomial divisor, SparsePolynomial negDividend, SparsePolynomial negDivisor){
        assertEquals(dividend1.divide(divisor).toString(), "1x + 1");
        assertEquals(dividend2.divide(divisor).toString(), "1x + 1");
        assertEquals(negDividend.divide(negDivisor).toString(), "-x^2 + 3x + 2");
    }

    public static void testNullPointerExceptionWithOperator(SparsePolynomial sp2){
        Exception e;
        AssertionError ae;
        String expectedMsg, actualMsg;

        e = assertThrows(NullPointerException.class,() -> sp2.add(null));
        expectedMsg = "There is nothing to add with.";
        actualMsg = e.getMessage();
        assertTrue(actualMsg.equalsIgnoreCase(expectedMsg));

        e = assertThrows(NullPointerException.class,() -> sp2.subtract(null));
        expectedMsg = "There is nothing to subtract with.";
        actualMsg = e.getMessage();
        assertTrue(actualMsg.equalsIgnoreCase(expectedMsg));

        e = assertThrows(NullPointerException.class,() -> sp2.multiply(null));
        expectedMsg = "There is nothing to multiply with.";
        actualMsg = e.getMessage();
        assertTrue(actualMsg.equalsIgnoreCase(expectedMsg));
    }

    public static void testWellFormed(){
        AssertionError ae;
        String expectedMsg = "The string is not a valid polynomial.";

        ae = assertThrows(AssertionError.class, ()-> new SparsePolynomial("3/4x + 2"));
        assertTrue(ae.getMessage().equalsIgnoreCase(expectedMsg));

        ae = assertThrows(AssertionError.class, ()-> new SparsePolynomial("4.2x^2 + 3x"));
        assertTrue(ae.getMessage().equalsIgnoreCase(expectedMsg));

        ae = assertThrows(AssertionError.class, ()-> new SparsePolynomial("(3*3)x^3 + 3x"));
        assertTrue(ae.getMessage().equalsIgnoreCase(expectedMsg));

        ae = assertThrows(AssertionError.class, ()-> new SparsePolynomial("4x^2 + [3x + 3x]"));
        assertTrue(ae.getMessage().equalsIgnoreCase(expectedMsg));

        ae = assertThrows(AssertionError.class, ()-> new SparsePolynomial("0"));
        assertTrue(ae.getMessage().equalsIgnoreCase(expectedMsg));
    }

}