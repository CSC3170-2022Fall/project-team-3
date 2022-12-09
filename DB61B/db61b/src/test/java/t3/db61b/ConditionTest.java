package t3.db61b;

import org.junit.Test;
import static org.junit.Assert.*;

/** Tests Basic functionality */
// Basic tests by cdk
public class ConditionTest {

    public Table student = new Table(new String[] {"first_name", "second_name"});

    @Test
    public void testConditionColumnWithColumn() {
    /* Run the unit tests in this file. */
        student.add(new Row(new String[] {"Alex", "Bob"}));
        student.add(new Row(new String[] {"Candy", "Candy"}));
        student.add(new Row(new String[] {"Duke", "Andy"}));
        Column col1 = new Column("first_name", student);
        Column col2 = new Column("second_name", student);
        Row row1 = new Row(new String[] {"Alex", "Bob"});
        Row row2 = new Row(new String[] {"Candy", "Candy"});
        Row row3 = new Row(new String[] {"Duke", "Andy"});
        Condition condSmaller = new Condition(col1, "<", col2);
        assertTrue(condSmaller.test(row1));
        Condition condSmallerEq = new Condition(col1, "<=", col2);
        assertTrue(condSmallerEq.test(row1, row2));
        Condition condEqual = new Condition(col1, "=", col2);
        assertTrue(condEqual.test(row2));
        Condition condNotEqual = new Condition(col1, "!=", col2);
        assertTrue(condNotEqual.test(row1));
        Condition condBigger = new Condition(col1, ">", col2);
        assertTrue(condBigger.test(row3));
        Condition condBiggerEq = new Condition(col1, ">=", col2);
        assertTrue(condBiggerEq.test(row2, row3));
    }

    @Test
    public void testConditionColumnWithValue() {
    /* Run the unit tests in this file. */
        student.add(new Row(new String[] {"Bob", "Candy"}));
        student.add(new Row(new String[] {"Candy", "Candy"}));
        student.add(new Row(new String[] {"Duke", "Andy"}));
        Column col1 = new Column("first_name", student);
        Row row1 = new Row(new String[] {"Bob", "Candy"});
        Row row2 = new Row(new String[] {"Candy", "Candy"});
        Row row3 = new Row(new String[] {"Duke", "Andy"});
        Condition condSmaller = new Condition(col1, "<", "Candy");
        assertTrue(condSmaller.test(row1));
        Condition condSmallerEq = new Condition(col1, "<=", "Candy");
        assertTrue(condSmallerEq.test(row1, row2));
        Condition condEqual = new Condition(col1, "=", "Candy");
        assertTrue(condEqual.test(row2));
        Condition condNotEqual = new Condition(col1, "!=", "Bobby");
        assertTrue(condNotEqual.test(row1));
        Condition condBigger = new Condition(col1, ">", "Candy");
        assertTrue(condBigger.test(row3));
        Condition condBiggerEq = new Condition(col1, ">=", "Candy");
        assertTrue(condBiggerEq.test(row2, row3));
    }
}