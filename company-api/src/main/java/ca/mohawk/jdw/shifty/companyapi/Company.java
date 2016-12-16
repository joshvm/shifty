package ca.mohawk.jdw.shifty.companyapi;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: company-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.model.employee.EmployeeDao;
import ca.mohawk.jdw.shifty.companyapi.model.employee.EmployeeValidator;
import ca.mohawk.jdw.shifty.companyapi.model.shift.ShiftDao;
import ca.mohawk.jdw.shifty.companyapi.model.shift.ShiftValidator;

public class Company {

    public static class Builder {

        private String name;
        private String email;
        private String phone;
        private Address address;
        private EmployeeDao employees;
        private EmployeeValidator employeeValidator;
        private ShiftDao shifts;
        private ShiftValidator shiftValidator;

        public String name(){
            return name;
        }

        public Builder name(final String name){
            this.name = name;
            return this;
        }

        public String email(){
            return email;
        }

        public Builder email(final String email){
            this.email = email;
            return this;
        }

        public String phone(){
            return phone;
        }

        public Builder phone(final String phone){
            this.phone = phone;
            return this;
        }

        public Address address(){
            return address;
        }

        public Builder address(final Address address){
            this.address = address;
            return this;
        }

        public EmployeeDao employees(){
            return employees;
        }

        public Builder employees(final EmployeeDao employees){
            this.employees = employees;
            return this;
        }

        public EmployeeValidator employeeValidator(){
            return employeeValidator;
        }

        public Builder employeeValidator(final EmployeeValidator employeeValidator){
            this.employeeValidator = employeeValidator;
            return this;
        }

        public ShiftDao shifts(){
            return shifts;
        }

        public Builder shifts(final ShiftDao shifts){
            this.shifts = shifts;
            return this;
        }

        public ShiftValidator shiftValidator(){
            return shiftValidator;
        }

        public Builder shiftValidator(final ShiftValidator shiftValidator){
            this.shiftValidator = shiftValidator;
            return this;
        }

        public Company build(){
            return new Company(
                    name,
                    email,
                    phone,
                    address,
                    employees,
                    employeeValidator,
                    shifts,
                    shiftValidator
            );
        }
    }

    private final String name;
    private final String email;
    private final String phone;

    private final Address address;

    private final EmployeeDao employees;
    private final EmployeeValidator employeeValidator;

    private final ShiftDao shifts;
    private final ShiftValidator shiftValidator;

    public Company(final String name,
                   final String email,
                   final String phone,
                   final Address address,
                   final EmployeeDao employees,
                   final EmployeeValidator employeeValidator,
                   final ShiftDao shifts,
                   final ShiftValidator shiftValidator){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.employees = employees;
        this.employeeValidator = employeeValidator;
        this.shifts = shifts;
        this.shiftValidator = shiftValidator;
    }

    public String name(){
        return name;
    }

    public String email(){
        return email;
    }

    public String phone(){
        return phone;
    }

    public Address address(){
        return address;
    }

    public EmployeeDao employees(){
        return employees;
    }

    public EmployeeValidator employeeValidator(){
        return employeeValidator;
    }

    public ShiftDao shifts(){
        return shifts;
    }

    public ShiftValidator shiftValidator(){
        return shiftValidator;
    }
}
