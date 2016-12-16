package ca.mohawk.jdw.shifty.clientapi.utils;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.model.ActivityLog;
import ca.mohawk.jdw.shifty.clientapi.net.Packet;
import ca.mohawk.jdw.shifty.companyapi.Address;
import ca.mohawk.jdw.shifty.companyapi.Company;
import ca.mohawk.jdw.shifty.companyapi.model.employee.Employee;
import ca.mohawk.jdw.shifty.companyapi.model.shift.Shift;
import java.sql.Timestamp;

public final class NetUtils {

    private NetUtils(){}

    public static Company deserializeCompany(final Packet pkt){
        return new Company.Builder()
                .name(pkt.readString())
                .email(pkt.readString())
                .phone(pkt.readString())
                .address(
                        new Address.Builder()
                            .unit(pkt.readInt())
                            .street(pkt.readString())
                            .city(pkt.readString())
                            .country(pkt.readString())
                            .build()
                )
                .build();
    }

    public static Employee deserializeEmployee(final Packet pkt){
        return new Employee(
                pkt.readLong(),
                pkt.readString(),
                Employee.Rank.forId(pkt.readUnsignedByte()),
                pkt.readString(),
                pkt.readString(),
                pkt.readString()
        );
    }

    public static Shift deserializeShift(final long employeeId,
                                         final Packet pkt){
        return new Shift(
                pkt.readLong(),
                employeeId,
                new Timestamp(pkt.readLong()),
                new Timestamp(pkt.readLong()),
                pkt.readString()
        );
    }

    public static ActivityLog deserializeActivityLog(final Employee employee,
                                                     final Packet pkt){
        return new ActivityLog(
                employee,
                new Timestamp(pkt.readLong()),
                pkt.readString()
        );
    }
}
