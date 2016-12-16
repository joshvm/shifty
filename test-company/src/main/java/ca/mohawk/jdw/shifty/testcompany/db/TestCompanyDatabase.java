package ca.mohawk.jdw.shifty.testcompany.db;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: test-company
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.testcompany.db.employee.SqliteEmployeeDao;
import ca.mohawk.jdw.shifty.testcompany.db.shift.SqliteShiftDao;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.skife.jdbi.v2.DBI;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

public class TestCompanyDatabase {

    public static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final DBI dbi;

    private final SqliteEmployeeDao employees;
    private final SqliteShiftDao shifts;

    public TestCompanyDatabase(){
        final SQLiteConnectionPoolDataSource ds = new SQLiteConnectionPoolDataSource();
        ds.setUrl("jdbc:sqlite:testcompany.db");
        dbi = new DBI(ds);

        employees = dbi.open(SqliteEmployeeDao.class);
        shifts = dbi.open(SqliteShiftDao.class);
    }

    public SqliteEmployeeDao employees(){
        return employees;
    }

    public SqliteShiftDao shifts(){
        return shifts;
    }
}
