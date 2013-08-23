package gov.nih.nci.firebird.common;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class ValidationResultTest {

    @Test
    public void testIsValid_True() {
        assertTrue(new ValidationResult().isValid());
    }

    @Test
    public void testIsValid_False() {
        ValidationFailure failure = new ValidationFailure("Hello, this is an error");
        ValidationResult result = new ValidationResult(failure);
        assertFalse(result.isValid());
    }

    @Test
    public void testAddFailures_NoFailures() {
        ValidationResult originalResult = new ValidationResult();
        ValidationResult newResult = new ValidationResult();
        newResult.addFailures(originalResult);
        assertTrue(newResult.isValid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFailures_Null() {
        ValidationResult newResult = new ValidationResult();
        newResult.addFailures(null);
    }

    @Test
    public void testAddFailures_Failures() {
        ValidationFailure failure = new ValidationFailure("Hello, this is an error");
        ValidationResult originalResult = new ValidationResult(failure);
        ValidationResult newResult = new ValidationResult();
        newResult.addFailures(originalResult);
        assertFalse(newResult.isValid());
        assertEquals(1, newResult.getFailures().size());
    }

    @Test
    public void testAddFailuresFailure() {
        ValidationFailure failure = new ValidationFailure("Hello, this is an error");
        ValidationResult newResult = new ValidationResult();
        newResult.addFailure(failure);
        assertFalse(newResult.isValid());
        assertEquals(1, newResult.getFailures().size());
        assertEquals(failure, newResult.getFailures().iterator().next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFailuresFailure_Null() {
        ValidationResult newResult = new ValidationResult();
        newResult.addFailure(null);
    }

    @Test
    public void testToString() {
        ValidationFailure failure1 = new ValidationFailure("Hello, this is an error");
        ValidationFailure failure2 = new ValidationFailure("Goodbye, this was another error");
        ValidationResult result = new ValidationResult(failure1, failure2);
        assertTrue(result.toString().contains(failure1.getMessage()));
        assertTrue(result.toString().contains(failure2.getMessage()));
    }

    @Test
    public void testToString_NoFailures() {
        ValidationResult result = new ValidationResult();
        assertTrue(StringUtils.isBlank(result.toString()));
    }
}
