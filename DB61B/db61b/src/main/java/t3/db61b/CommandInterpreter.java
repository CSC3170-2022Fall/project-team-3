
package t3.db61b;

import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Scanner;

import java.util.List;

import static t3.db61b.Utils.*;

/**
 * An object that reads and interprets a sequence of commands from an
 * input source.
 * 
 * @author
 */
class CommandInterpreter {

    /*
     * STRATEGY.
     *
     * This interpreter parses commands using a technique called
     * "recursive descent." The idea is simple: we convert the BNF grammar,
     * as given in the specification document, into a program.
     *
     * First, we break up the input into "tokens": strings that correspond
     * to the "base case" symbols used in the BNF grammar. These are
     * keywords, such as "select" or "create"; punctuation and relation
     * symbols such as ";", ",", ">="; and other names (of columns or tables).
     * All whitespace and comments get discarded in this process, so that the
     * rest of the program can deal just with things mentioned in the BNF.
     * The class Tokenizer performs this breaking-up task, known as
     * "tokenizing" or "lexical analysis."
     *
     * The rest of the parser consists of a set of functions that call each
     * other (possibly recursively, although that isn't needed for this
     * particular grammar) to operate on the sequence of tokens, one function
     * for each BNF rule. Consider a rule such as
     *
     * <create statement> ::= create table <table name> <table definition> ;
     *
     * We can treat this as a definition for a function named (say)
     * createStatement. The purpose of this function is to consume the
     * tokens for one create statement from the remaining token sequence,
     * to perform the required actions, and to return the resulting value,
     * if any (a create statement has no value, just side-effects, but a
     * select clause is supposed to produce a table, according to the spec.)
     *
     * The body of createStatement is dictated by the right-hand side of the
     * rule. For each token (like create), we check that the next item in
     * the token stream is "create" (and report an error otherwise), and then
     * advance to the next token. For a metavariable, like <table definition>,
     * we consume the tokens for <table definition>, and do whatever is
     * appropriate with the resulting value. We do so by calling the
     * tableDefinition function, which is constructed (as is createStatement)
     * to do exactly this.
     *
     * Thus, the body of createStatement would look like this (_input is
     * the sequence of tokens):
     *
     * _input.next("create");
     * _input.next("table");
     * String name = name();
     * Table table = tableDefinition();
     * _input.next(";");
     *
     * plus other code that operates on name and table to perform the function
     * of the create statement. The .next method of Tokenizer is set up to
     * throw an exception (DBException) if the next token does not match its
     * argument. Thus, any syntax error will cause an exception, which your
     * program can catch to do error reporting.
     *
     * This leaves the issue of what to do with rules that have alternatives
     * (the "|" symbol in the BNF grammar). Fortunately, our grammar has
     * been written with this problem in mind. When there are multiple
     * alternatives, you can always tell which to pick based on the next
     * unconsumed token. For example, <table definition> has two alternative
     * right-hand sides, one of which starts with "(", and one with "as".
     * So all you have to do is test:
     *
     * if (_input.nextIs("(")) {
     * _input.next("(");
     * // code to process "<column name>,  )"
     * } else {
     * // code to process "as <select clause>"
     * }
     *
     * As a convenience, you can also write this as
     *
     * if (_input.nextIf("(")) {
     * // code to process "<column name>,  )"
     * } else {
     * // code to process "as <select clause>"
     * }
     *
     * combining the calls to .nextIs and .next.
     *
     * You can handle the list of <column name>s in the preceding in a number
     * of ways, but personally, I suggest a simple loop:
     *
     * ... = columnName();
     * while (_input.nextIs(",")) {
     * _input.next(",");
     * ... = columnName();
     * }
     *
     * or if you prefer even greater concision:
     *
     * ... = columnName();
     * while (_input.nextIf(",")) {
     * ... = columnName();
     * }
     *
     * (You'll have to figure out what do with the names you accumulate, of
     * course).
     */

    /**
     * A new CommandInterpreter executing commands read from INP, writing
     * prompts on PROMPTER, if it is non-null.
     */
    CommandInterpreter(Scanner inp, PrintStream prompter) {
        _input = new Tokenizer(inp, prompter);
        _database = new Database();
    }

    /**
     * Parse and execute one statement from the token stream. Return true
     * iff the command is something other than quit or exit.
     */
    boolean statement() {
        switch (_input.peek()) {
            case "create":
                createStatement();
                break;
            case "load":
                loadStatement();
                break;
            case "exit":
            case "quit":
                exitStatement();
                return false;
            case "*EOF*":
                return false;
            case "insert":
                insertStatement();
                break;
            case "print":
                printStatement();
                break;
            case "select":
                selectStatement();
                break;
            case "store":
                storeStatement();
                break;
            default:
                throw error("unrecognizable command");
        }
        return true;
    }

    /** Parse and execute a create statement from the token stream. */
    void createStatement() {
        _input.next("create");
        _input.next("table");
        String name = name();
        Table table = tableDefinition();
        _database.put(name, table);
        _input.next(";");
    }

    /**
     * Parse and execute an exit or quit statement. Actually does nothing
     * except check syntax, since statement() handles the actual exiting.
     */
    void exitStatement() {
        if (!_input.nextIf("quit")) {
            _input.next("exit");
        }
        _input.next(";");
    }

    /** Parse and execute an insert statement from the token stream. */
    void insertStatement() {
        _input.next("insert");
        _input.next("into");
        Table table = tableName();
        _input.next("values");

        ArrayList<String> values = new ArrayList<>();
        values.add(literal());
        while (_input.nextIf(",")) {
            values.add(literal());
        }
        if (values.size() != table.columns())
            throw error("Number of data not match with number of columns");
        table.add(new Row(values.toArray(new String[values.size()])));
        _input.next(";");
    }

    /** Parse and execute a load statement from the token stream. */
    void loadStatement() {
        _input.next("load");
        String LoadName = name();
        Table LoadTable = Table.readTable(LoadName);
        _database.put(LoadName, LoadTable);
        System.out.println("Loaded " + LoadName + ".db");
        _input.next(";");
    }

    /** Parse and execute a store statement from the token stream. */
    void storeStatement() {
        _input.next("store");
        String name = _input.peek();
        Table table = tableName();
        table.writeTable(name);
        System.out.printf("Stored %s.db%n", name);
        _input.next(";");
    }

    /** Parse and execute a print statement from the token stream. */
    void printStatement() {
        _input.next("print");
        Table GetTable = tableName();
        GetTable.print();
        _input.next(";");
    }

    /** Parse and execute a select statement from the token stream. */
    void selectStatement() {
        _input.next("select");
        Table SelectTable = selectClause();
        _input.next(";");
        System.out.println("Search results:");
        SelectTable.print();
    }

    /**
     * Parse and execute a table definition, returning the specified
     * table.
     */
    Table tableDefinition() {
        Table table;
        if (_input.nextIf("(")) {
            ArrayList<String> ColumnName = new ArrayList<String>();
            ColumnName.add(columnName());
            while (_input.nextIf(",")) {
                ColumnName.add(columnName());
            }
            _input.next(")");
            table = new Table(ColumnName);
        } else {
            _input.next("as");
            _input.next("select");
            table = selectClause();
        }
        return table;
    }

    Table recursiveSelect(List<Table> tabList, List<String> colTitles, List<String> colTitlesLeft, int num,
            Table curTable) {
        Table table = new Table(new String[] { "" });
        Table selecTable = tabList.get(num);
        for (int i = 0; i < colTitlesLeft.size();) {
            // if the left column can be find in the following table add it to colTitles
            if (selecTable.findColumn(colTitlesLeft.get(i)) != -1) {
                colTitles.add(colTitlesLeft.remove(i));
            } else {
                i++;
            }
        }
        table = selecTable.select(curTable, colTitles);
        return table;
    }

    /**
     * Parse and execute a select clause from the token stream, returning the
     * resulting table.
     */
    Table selectClause() {
        List<String> colTitles = new ArrayList<String>();
        colTitles.add(columnName());
        while (_input.nextIf(",")) {
            colTitles.add(columnName());
        }
        _input.next("from");
        List<Table> tabList = new ArrayList<>();

        // initialize a new table to record the selection
        Table table = new Table(new String[] { "" });
        tabList.add(tableName());
        while (_input.nextIf(",")) {
            tabList.add(tableName());
        }

        // Normal selection first
        if (tabList.size() < 2) {
            // one table
            Table selecTable = tabList.get(0);
            table = selecTable.select(colTitles);
            // more than two table.
        } else {
            int tableNum = tabList.size();
            Table selecTable = tabList.get(0);
            Table selecTable2 = tabList.get(1);
            if (tableNum > 2) {
                List<String> colTitlesLeft = new ArrayList<String>();
                for (int i = 0; i < colTitles.size();) {
                    // if this column is not in first table and second table
                    if (selecTable.findColumn(colTitles.get(i)) == -1
                            && selecTable2.findColumn(colTitles.get(i)) == -1) {
                        colTitlesLeft.add(colTitles.remove(i));
                    } else {
                        i++;
                    }
                }
                table = selecTable.select(selecTable2, colTitles);
                for (int i = 2; i < tableNum; i++) {
                    table = recursiveSelect(tabList, colTitles, colTitlesLeft, i, table);
                }
            } else {
                table = selecTable.select(selecTable2, colTitles);
            }
        }

        // if next tokenizer is where, check condition.
        if (_input.nextIf("where")) {
            List<Condition> conList = new ArrayList<>();
            // tabList.add(table);
            Table[] tabArray = tabList.toArray(new Table[tabList.size()]);
            conList = conditionClause(tabArray);
            if (tabList.size() < 2) {
                table = tabList.get(0).select(colTitles, conList);
            } else if (tabList.size() == 2) {
                table = tabList.get(0).select(tabList.get(1), colTitles, conList);
            } else {
                conList = conditionClause(table);
                table = table.select(colTitles, conList);
            }
        }

        return table;
    }

    /** Parse and return a valid name (identifier) from the token stream. */
    String name() {
        return _input.next(Tokenizer.IDENTIFIER);
    }

    /**
     * Parse and return a valid column name from the token stream. Column
     * names are simply names; we use a different method name to clarify
     * the intent of the code.
     */
    String columnName() {
        return name();
    }

    /**
     * Parse a valid table name from the token stream, and return the Table
     * that it designates, which must be loaded.
     */
    Table tableName() {
        String name = name();
        Table table = _database.get(name);
        if (table == null) {
            throw error("unknown table: %s", name);
        }
        return table;
    }

    /**
     * Parse a literal and return the string it represents (i.e., without
     * single quotes).
     */
    String literal() {
        String lit = _input.next(Tokenizer.LITERAL);
        return lit.substring(1, lit.length() - 1).trim();
    }

    /**
     * Parse and return a list of Conditions that apply to TABLES from the
     * token stream. This denotes the conjunction (`and') zero
     * or more Conditions.
     */
    ArrayList<Condition> conditionClause(Table... tables) {
        ArrayList<Condition> conList = new ArrayList<Condition>();
        Condition condi = condition(tables);
        conList.add(condi);
        while (_input.nextIf("and")) {
            Condition condi2 = condition(tables);
            conList.add(condi2);
        }
        return conList;
    }

    /**
     * Parse and return a Condition that applies to TABLES from the
     * token stream.
     */
    Condition condition(Table... tables) {
        String colTitle = columnName();
        String relate = _input.next(Tokenizer.RELATION);
        Column coll = new Column(colTitle, tables);
        // for (int i = 0; i < tables.length; i++) {
        // // if the column is in that table
        // if (tables[i].findColumn(colTitle) != -1) {
        // coll = new Column(colTitle, tables[i]);
        // }
        // }

        if (_input.nextIs(Tokenizer.LITERAL)) {
            String report = literal();
            Condition condi = new Condition(coll, relate, report);
            return condi;
        } else {
            String colTitle2 = columnName();
            Column report = new Column(colTitle2, tables);
            Condition condi = new Condition(coll, relate, report);
            return condi;
        }
    }

    /** Advance the input past the next semicolon. */
    void skipCommand() {
        while (true) {
            try {
                while (!_input.nextIf(";") && !_input.nextIf("*EOF*")) {
                    _input.next();
                }
                return;
            } catch (DBException excp) {
                /* No action */
            }
        }
    }

    /** The command input source. */
    private Tokenizer _input;
    /** Database containing all tables. */
    private Database _database;
}
