# CSC3170 Course Project Report

## Historical Progress
This is a preliminary report, which includes Project Overall Description, Team Members, Project Specification and Project Checklist.  For details, you can refer to [README](README.md).


## Program Design
Since the template we used was written by Java, we have 7 important classes to together form our major function. The basic relationship amoung classes is shown below:

![image](graphs/relationship.png)

For each class:
+ **Tokenizer**: Parse user commands, and interacts with database or creates conditions.
+ **CommandInterpreter**: Receive semantic information extracted from Tokenizer, and assign tasks to corresponding functions for execution.
+ **Condition**: Deal with "where condition" in select command.
+ **Column**: An index of a column in a list of rows.
+ **Row**: A single row of data.
+ **Table**: Write/read from files, and maintain db operations.
+ **Database**: Formed by tables.

Based on the hierarchical class structure, we built a [Checklist](README.md) for progress management. Group members are able to focus on the implementation of different classes in parallel. For instance, the person who implement Row class do not need to pay much attention both on the detail implementation of Column and the calling method in Table.


## File Structure
Here we will briefly introduce the file structure of our Github repo. The Quick Access Link in [README](README.md) will be helpful to quickly access important files.

1. In root directory, there are 6 files and 4 subdirectories. The files are 2 configurations files, 1 license, 3 descriptive documents (including the report). The subdirectories are 2 configuration directories, 1 main codes directory (DB61B), 1 graph directory (containing graphs used in markdown files).
2. In DB61B, there are 3 important directories with key codes:
   * "\DB61B\db61b\src\main\java\t3\db61b": Java codes of the project
   * "\DB61B\db61b\src\test\java\t3\db61b": Test files for Maven unit test
   * "\DB61B\testing": Final test files


## Functionality Implementation
### Tokenizer
[Tokenizer](DB61B/db61b/src/main/java/t3/db61b/Tokenizer.java) class is provided by the template from DB61B. It uses regex to Tokenize the input of user.

### CommandInterpreter
In the [CommandInterpreter](DB61B/db61b/src/main/java/t3/db61b/CommandInterpreter.java) part, there are several command implementation including Create,Load,Print,Store,Insert,Select and Quit/Exit. We mainly focus on the Select command since others use similar structure thus are easy to implement. The Selection Clause is shown below:

![image](graphs/selection_clause.png)

It shows the basic implementation of the selection part. Its logic is: whether there is a condition or not, create a table first. If there is a condition, perform a single table condition query directly on the basis of the output table. But our task also includes the selection of two tables and the selection of conditions. Our idea is to merge the selected columns and create a new table, and then perform single table condition query on the basis of this new table.

### Condition
[Condition](DB61B/db61b/src/main/java/t3/db61b/Condition.java) class is a class representing Conditions inputted to system and take on the task of comparing data according to recorded conditions.

This class is the dependency of condition selections

### Row & Column
In [Row](DB61B/db61b/src/main/java/t3/db61b/Row.java) and [Column](DB61B/db61b/src/main/java/t3/db61b/Column.java) parts, we implement them as a sub-structure of table. Especially for row class, it's the structure that directly stores the data. And to prevent the to be inserted data from duplication, we override the equals and hashcodes method for comparison.

For Column class, it's a collection that act as a bunch of "pointers" to index the data in columns. It also stores column titles, which gives efficiency to index.

### Table
In [Table](DB61B/db61b/src/main/java/t3/db61b/Table.java) class, we have divided this class into two implementations. The first one is the base component and functions of Table class, the second part is the select related functions.

In Table print method, we aim at making the tables interactive and make a sort of visualization style to print the table tidily.

![image](graphs/tableprint_style_sample.png)

This is a sample of table print in System. It's a "parody" of MySQL display style, but giving good readability.

In the first part we use Row class as the direct sub-struct. Base on this design we implemented C/R operations.

In the second part, we inplement following select function:
+ **Simple select**: only locates target columns in one table
+ **Multiple table simple select**: only locates target columns in multiple tables
+ **Condition select**: locates target columns fitting the conditions in one table
+ **Multiple table condition select**: locates target columns fitting the conditions in multiple tables

### Database
[Database](DB61B/db61b/src/main/java/t3/db61b/Database.java) class is a collection of tables. It indexes all table loaded or created in current session with their names. This class is mainly used to locate existed tables when running other methods.


## Difficulty & Solutions
### 1. Intermediate Test (Unit Tests):
Based the original file structure, we have to finish everything and do the final test to debug. It was quite hard to find and correct mistakes from many functions.

So, we introduced Maven management method to realise intermediate test. Once a class with several functions is implemented, the programmer could write a small piece of code to verify the correctness of that class.

### 2. Multi Tables Selection:
Even though our single table and double table selection is OK, when we try to select multiple tables, we find that when the number of selected tables is greater than or equal to 3, the selection conditions sometimes become invalid, or only the header is returned without data。

It seems that， rather than a bug the current version of the project is more so missing some implementation,which is lossing the support of condition for multi-table (3+) select. Originally after the Command Interpreter goes through the if-elses to find which exact kind of select it is dealing with it calls the corresponding function to handle that case. But now instead, it only uses the no condition variants to process a table with the Columns needed and then it Tries to apply conditions. This way its very normal that we cannot find conditions on non-selected columns. Since multi-table select is not included in the request of this project, we just give some suggestions about it:
- Try to make the "Recursive Select" able to handle condition list in the input
- Try different implementation method for multi-table such as creating a big natural joined table and then single table select with cond smthng like that ...
- Accept that the 3+ Table select won't handle conditions, for 1-2 table select goes back to the original way it was done, and for 3 tables with no condition just throw a different error to make it clear it is unimplemented rather just bug.


## Contribution
<!-- change the info below to be the real case -->

| Student ID | Student Name |GitHub Username | Contribution |
| ---------- | ------------ |------------------------- |----------------------------------|
| 120090336   | 陈德坤🚩    |@[salixc](https://github.com/salixc) | Implement the whole class of Condition and Database. Dealing with report, slides, presentation video things. |
| 120090747   | 陈清源    |@[Christoph-UGameGerm](https://github.com/Christoph-UGameGerm)| Implement Table class except selection. Bug fix in the first overall testing. Configure Python tester. |
| 120090675   | 黎鸣     |@[Mo9L1](https://github.com/Mo9L1) | implement select with condition|
| 119010531 |Nasr Alae-eddine|@[H4D32](https://github.com/H4D32) | Switch to Maven Project and Implement the first unit testing framework. Implement two table Variety of select with and without conditions |
| 120010027  | 张家宇    |@[JJY-jy233](https://github.com/JJY-jy233) | Implement single table selection; Working on optimization of selectClause function in CommandInterpreter.java; Realize multitable(more than two tables);Fix some bugs in table.java.| 
| 118010408   | 张昊旻  |@[118010408](https://github.com/118010408) | Implement insert, print and load; Fill in create and store; Preliminary completion of condition, condition clause and unconditional single table selection |
