package t3.db61b;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Unit test for simple App.
 */
public class TableTest {
    /**
     * Rigorous Test :-)
     */
    public Table table = new Table(new String[] { "Student", "Mentor", "School" });

    @Test
    public void Test_writeTable() {
        table.add(new Row(new String[] { "Alex", "Bob", "SDS" }));
        table.add(new Row(new String[] { "Charlie", "Diana", "SSE" }));
        table.writeTable(new String("student"));
    }

    @Test
    public void Test_print() {
        table.add(new Row(new String[] { "Alex", "Bob", "SDS" }));
        table.add(new Row(new String[] { "Charlie", "Diana", "SSE" }));
        table.print();
    }

    @Test
    public void Test_readTable() {
        table.add(new Row(new String[] { "Alex", "Bob", "SDS" }));
        table.add(new Row(new String[] { "Charlie", "Diana", "SSE" }));
        table.writeTable(new String("Student"));
        Table new_table = Table.readTable(new String("Student"));
        new_table.print();
    }

    @Test
    public void Test_columns() {
        table.add(new Row(new String[] { "Alex", "Bob", "SDS" }));
        table.add(new Row(new String[] { "Charlie", "Diana", "SSE" }));
        assertEquals(3, table.columns());
        Table new_table = new Table(new String[] { "A_Test", "B_Test" });
        assertEquals(2, new_table.columns());
    }

    @Test
    public void Test_getTitle() {
        assertEquals("Student", table.getTitle(0));
        assertEquals("School", table.getTitle(2));
    }

    @Test
    public void Test_findColumn() {
        Table t1 = Table.readTable("PATH\\students"); //write your absolute path here
        assertEquals(-1, t1.findColumn("Mentor"));
        assertEquals(-1, t1.findColumn("School"));
        assertEquals(5, t1.findColumn("Major"));
    }

    @Test
    public void Test_size() {
        table.add(new Row(new String[] { "Alex", "Bob", "SDS" }));
        assertEquals(1, table.size());
        table.add(new Row(new String[] { "Charlie", "Diana", "SSE" }));
        assertEquals(2, table.size());
    }

    @Test
    public void Test_add() {
        table.add(new Row(new String[] { "Test", "Add", "Success" }));
        table.add(new Row(new String[] { "Print", "To", "Check" }));
        table.print();
    }

    @Test
    public void Test_select() {
        Table t = Table.readTable("PATH\\students"); //write your absolute path here
        List<String> select = new LinkedList<>();
        select.add("Major");
        select.add("SID");
        Table t1 = t.select(select);
        t1.print();
    }

    @Test
    public void Test_equijoin() {
        table.add(new Row(new String[] { "Alex", "Bob", "SDS" }));
        table.add(new Row(new String[] { "Charlie", "Diana", "SSE" }));

        Table stu_id_table = new Table(new String[] { "Student", "ID" });
        stu_id_table.add(new Row(new String[] { "Alex", "000001" }));
        stu_id_table.add(new Row(new String[] { "Charlie", "000002" }));

        table.print();
        stu_id_table.print();

        List<Column> column_stu_table = new ArrayList<Column>();
        List<Column> column_stu_idtable = new ArrayList<Column>();
        column_stu_table.add(new Column("Student", table));
        column_stu_idtable.add(new Column("Student", stu_id_table));
        for (Row row_table : table) {
            for (Row row_idtable : stu_id_table) {
                /*
                 * // Change the decorator private of equijoin to public before testing it.
                 * if (Table.equijoin(column_stu_table, column_stu_idtable, row_table,
                 * row_idtable))
                 * System.out.println("equijoin True");
                 */
            }
        }
    }


}
