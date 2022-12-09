package t3.db61b;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Basic tests for different select cases
 */
public class SelectTest {
    /**
     * Rigorous Test :-)
     */
    public Table table = new Table(new String[] { "ID", "Student", "Mentor", "School" });
    public Table table2 = new Table(new String[] { "ID", "Course", "Grade" });
    @Test
    public void selectSingleNoCond() {
        table.add(new Row(new String[] { "1", "Alex", "Bob", "SDS" }));
        table.add(new Row(new String[] { "2","Charlie", "Diana", "SSE" }));
        assertEquals(3, table.findColumn("School"));
        List<String> columnNames = new ArrayList<>();
        columnNames.add("Student");
        columnNames.add("School");
        Table selected = table.select(columnNames);
        assertEquals(1, selected.findColumn("School"));
        for (Row r: selected) {
            assertEquals(2, r.size());
        }
        selected.print();
    }  
    @Test
    public void selectDoubleNoCond() {
        table.add(new Row(new String[] { "1", "Alex", "Bob", "SDS" }));
        table.add(new Row(new String[] { "2","Charlie", "Diana", "SSE" }));
        table2.add(new Row(new String[] { "1", "Math", "A"}));
        table2.add(new Row(new String[] { "1", "Phy", "B"}));
        table2.add(new Row(new String[] { "1", "Chem", "C"}));
        table2.add(new Row(new String[] { "2", "Math", "B+"}));
        table2.add(new Row(new String[] { "2", "Phy", "B-"}));
        table2.add(new Row(new String[] { "2", "Csc", "A-"}));
        List<String> columnNames = new ArrayList<>();
        columnNames.add("Student");
        columnNames.add("Course");
        columnNames.add("Grade");
        Table selected = table.select(table2, columnNames);
        assertEquals(2, selected.findColumn("Grade"));
        for (Row r: selected) {
            assertEquals(3, r.size());
        }
        selected.print();
    }  
}
