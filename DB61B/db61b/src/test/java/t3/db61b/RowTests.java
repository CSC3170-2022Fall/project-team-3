package t3.db61b;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

/** Tests Basic functionality */
public class RowTests {

    @Test
    public void testRowSize() {
        /* Run the unit tests in this file. */
        Row r = new Row(new String[] { "Alae", "First", "Test", "Size 4" });
        assertEquals(4, r.size());
    }

    @Test
    public void testRowSizeEmpty() {
        /* Run the unit tests in this file. */
        Row r = new Row(new String[] {});
        assertEquals(0, r.size());
    }

    @Test
    public void testRowGet() {
        /* Run the unit tests in this file. */
        Row r = new Row(new String[] { "Easy", "Get", "Test" });
        assertEquals("Get", r.get(1));
    }

    @Test
    public void testRowEquals() {
        /* Run the unit tests in this file. */
        Row r = new Row(new String[] { "Easy", "Equals", "Test" });
        Row r1 = new Row(new String[] { "Easy", "Equals", "Test" });
        Row r2 = new Row(new String[] { "Basic" });
        Row r3 = new Row(new String[] { "Easy", "Equal", "Test" });
        assertTrue(r.equals(r1));
        assertFalse(r.equals(r2));
        assertFalse(r.equals(r3));
    }

    @Test
    public void testRowConstructor() {
        Table table = new Table(new String[] { "Student", "Mentor", "School" });
        table.add(new Row(new String[] { "Alex", "Bob", "SDS" }));
        table.add(new Row(new String[] { "Charlie", "Diana", "SSE" }));
        table.add(new Row(new String[] { "Eileen", "Fiona", "SME" }));
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("Student", table));
        columns.add(new Column("Mentor", table));
        columns.add(new Column("School", table));
        Row[] rows = new Row[3];
        Integer index = 0;
        for (Row row : table) {
            rows[index++] = row;
            if (index == 3)
                break;
        }
        Row target_row = new Row(columns, rows);
        for (int i = 0; i < target_row.size(); i++) {
            System.out.println(target_row.get(i));
        }
    }

}