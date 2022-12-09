package t3.db61b;

import org.junit.Test;
import static org.junit.Assert.*;

/** Tests Basic functionality */
// Basic tests by cdk
public class DatabaseTest {

    public Table table_student = new Table(new String[] { "Student_name", "ID", "Year" });
    public Table table_student_new = new Table(new String[] { "Student_name", "ID", "Year", "GPA" });
    public Database db = new Database();

    @Test
    public void testDatabaseGetMatched() {
    /* Run the unit tests in this file. */
        db.put("sTable", table_student);
        assertEquals(table_student, db.get("sTable"));
    }

    @Test
    public void testDatabaseGetUnmatched() {
    /* Run the unit tests in this file. */
        assertEquals(null, db.get("sTable"));
    }

    @Test
    public void testDatabaseReplace() {
    /* Run the unit tests in this file. */
        db.put("sTable", table_student);
        db.put("sTable", table_student_new);
        Table tb = db.get("sTable");
        assertEquals(4, tb.columns());
    }

    
}