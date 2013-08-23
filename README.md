students_db
===========

Sample project for importing and exporting student and parent records from/to CSV files.

This file contains parent name and address information along with their child(s) student id and campus name.  This means the parent information is duplicated if they have more than one child.  This program parses this CSV file and places it in an H2 database containing a PARENTS and STUDENTS table.  Finally, it has the ability to export the data as a CSV where the child(s) information is appended to the end of the parent record so the parent data is not repeated.
