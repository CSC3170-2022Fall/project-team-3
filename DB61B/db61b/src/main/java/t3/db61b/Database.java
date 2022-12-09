// This is a SUGGESTED skeleton for a class that contains the Tables your
// program manipulates.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution changes about 6
// lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package t3.db61b;

import java.util.HashMap; // Solution attempt -Chen Dekun

/**
 * A collection of Tables, indexed by name.
 * 
 * @author
 */
class Database {
    /** An empty database. */
    public Database() {
        _database = new HashMap<String, Table>(); // Solution attempt -Chen Dekun
    }

    /**
     * Return the Table whose name is NAME stored in this database, or null
     * if there is no such table.
     */
    public Table get(String name) {
        if (_database.containsKey(name)) { // Solution attempt -Chen Dekun
            return _database.get(name);
        }
        return null;
    }

    /**
     * Set or replace the table named NAME in THIS to TABLE. TABLE and
     * NAME must not be null, and NAME must be a valid name for a table.
     */
    public void put(String name, Table table) {
        if (name == null || table == null) {
            throw new IllegalArgumentException("null argument");
        }
        // HashMap "put" function can deal with both Set and Replace
        _database.put(name, table); // Solution attempt -Chen Dekun
    }

    private HashMap<String, Table> _database; // Solution attempt -Chen Dekun
}
