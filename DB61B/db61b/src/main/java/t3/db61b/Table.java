// This is a SUGGESTED skeleton for a class that represents a single
// Table.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution changes or adds
// about 100 lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package t3.db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


import static t3.db61b.Utils.*;

/**
 * A single table in a database.
 * 
 * @author P. N. Hilfinger
 */
class Table implements Iterable<Row> {
    /**
     * A new Table whose columns are given by COLUMNTITLES, which may
     * not contain dupliace names.
     */
    Table(String[] columnTitles) {
        for (int i = columnTitles.length - 1; i >= 1; i -= 1) {
            for (int j = i - 1; j >= 0; j -= 1) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw error("duplicate column name: %s", columnTitles[i]);
                }
            }
        }
        this.titles = new String[columnTitles.length];
        for (int i = 0; i < columnTitles.length; i++) {
            this.titles[i] = columnTitles[i];
            // System.out.println(this.titles[i]);
        }
        // System.out.println(this.titles);
    }

    /** A new Table whose columns are give by COLUMNTITLES. */
    Table(List<String> columnTitles) {
        this(columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    public int columns() {
        return this.titles.length;
    }

    /** Return the title of the Kth column. Requires 0 <= K < columns(). */
    public String getTitle(int k) {
        return this.titles[k];
    }

    /**
     * Return the number of the column whose title is TITLE, or -1 if
     * there isn't one.
     */
    public int findColumn(String title) {
        for (int i = 0; i < this.columns(); i++) {
            if (this.titles[i].equals(title))
                return i;
        }
        return -1;
    }

    /** Return the number of Rows in this table. */
    public int size() {
        return _rows.size();
    }

    /** Returns an iterator that returns my rows in an unspecfied order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /**
     * Add ROW to THIS if no equal row already exists. Return true if anything
     * was added, false otherwise.
     */
    public boolean add(Row row) {
        if (_rows.contains(row))
            return false;
        _rows.add(row);
        return true;
    }

    /**
     * Read the contents of the file NAME.db, and return as a Table.
     * Format errors in the .db file cause a DBException.
     */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db")); //Add "DB61B/testing/" -Suggestion Alae
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table = new Table(columnNames);
            System.out.println("table._title" + table.titles.toString());
            String line;
            String[] data;
            while ((line = input.readLine()) != null) {
                data = line.split(",");
                table.add(new Row(data));
            }
        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    } // Solution attempt -Jiayu

    /**
     * Write the contents of TABLE into the file NAME.db. Any I/O errors
     * cause a DBException.
     */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String sep;
            sep = "";
            output = new PrintStream(name + ".db");
            sep = String.join(",", this.titles);
            output.println(sep);
            sep = "";
            for (Row r : _rows) {
                for (int i = 0; i < this.columns() - 1; i += 1) {
                    sep += r.get(i);
                    sep += ",";
                }
                sep += r.get(this.columns() - 1);
                output.println(sep);
                sep = "";
            }
        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /** Print my contents on the standard output. */
    void print() {
        for (String title : this.titles)
            System.out.print(title + "\t");
        System.out.print("\n");
        for (Row row : this._rows) {
            for (int i = 0; i < this.columns(); i++) {
                System.out.print(row.get(i) + "\t");
            }
            System.out.print("\n");
        }
    }

    /**
     * Return a new Table whose columns are COLUMNNAMES, selected from
     * rows of this table that satisfy CONDITIONS.
     */
    Table select(List<String> columnNames, List<Condition> conditions) {
        Table result = new Table(columnNames);
        // FILL IN

        return result;
    }

    /**
     * Return a new Table whose columns are COLUMNNAMES, selected from
     * rows if this table with no conditions
     * 
     * @param columnNames
     * @return Table
     */
    Table select(List<String> columnNames) {
        Table result = new Table(columnNames);
        List<Integer> columnNum = new ArrayList<>();    
        for (String columnName : columnNames) {
            columnNum.add(this.findColumn(columnName));
        }
        for (Row row : _rows) {
            String[] newRow = new String[columnNames.size()];
            for (int i = 0; i < columnNames.size(); i++) {
                newRow[i] = row.get(columnNum.get(i));
            }
            result.add(new Row(newRow));    
        }
        return result;
    }

    /**
     * Return a new Table whose columns are COLUMNNAMES, selected
     * from pairs of rows from this table and from TABLE2 that match
     * on all columns with identical names and satisfy CONDITIONS.
     */
    Table select(Table table2, List<String> columnNames,
            List<Condition> conditions) {
        Table result = new Table(columnNames);
        // FILL IN
        return result;  
    }

    private static ArrayList<Column> namesToColumns(List<String> columnNames,
    Table... tables) {
        ArrayList<Column> Col = new ArrayList<>();
        for (int i = 0; i < columnNames.size(); i++) {
        Col.add(new Column(columnNames.get(i), tables));
        }
        return Col;
    }

        /**
     * Return a new Table whose columns are COLUMNNAMES, selected
     * from pairs of rows from this table and from TABLE2 that match
     * on all columns with identical names with no conditions.
     */
    Table select(Table table2, List<String> columnNames) {
        Table result = new Table(columnNames);
        ArrayList<String> commonCols = new ArrayList<>();
        for (int i = 0; i < this.columns(); i++) {
            for (int j = 0; j < table2.columns(); j++) {
                String thisCol = this.getTitle(i);
                String col2 = table2.getTitle(j);
                if (thisCol.equals(col2)) {
                    commonCols.add(thisCol);
                }
            }
        }
        ArrayList<Column> common1 = namesToColumns(commonCols, this);
        ArrayList<Column> common2 = namesToColumns(commonCols, table2); 
        Table[] tables = {this, table2};
        ArrayList<Column> columns = namesToColumns(columnNames, tables);
        for (Row row1: this) {
            for (Row row2: table2) {
                if (equijoin(common1, common2, row1, row2)) {
                    Row[] rows = {row1, row2};
                    result.add(new Row(columns, rows));
                }
            }
        }
        return result;
    }

    /**
     * Return true if the columns COMMON1 from ROW1 and COMMON2 from
     * ROW2 all have identical values. Assumes that COMMON1 and
     * COMMON2 have the same number of elements and the same names,
     * that the columns in COMMON1 apply to this table, those in
     * COMMON2 to another, and that ROW1 and ROW2 come, respectively,
     * from those tables.
     */
    private static boolean equijoin(List<Column> common1, List<Column> common2,
            Row row1, Row row2) {
        List<String> data1 = new ArrayList<String>(), data2 = new ArrayList<String>();
        for (int i = 0; i < common1.size(); i++) {
            data1.add(common1.get(i).getFrom(row1));
            data2.add(common2.get(i).getFrom(row2));
        }
        if (!data1.containsAll(data2) || !data2.containsAll(data1))
            return false;
        return true;
    }

    /** My rows. */
    private HashSet<Row> _rows = new HashSet<>();
    private String[] titles;
}
