package ca.mohawk.jdw.shifty.companyapi;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: company-api
  
  Developed By: Josh Maione (000320309)
*/

public class Address {

    public static class Builder {

        private int unit;
        private String street;
        private String city;
        private String country;

        public int unit(){
            return unit;
        }

        public Builder unit(final int unit){
            this.unit = unit;
            return this;
        }

        public String street(){
            return street;
        }

        public Builder street(final String street){
            this.street = street;
            return this;
        }

        public String city(){
            return city;
        }

        public Builder city(final String city){
            this.city = city;
            return this;
        }

        public String country(){
            return country;
        }

        public Builder country(final String country){
            this.country = country;
            return this;
        }

        public Address build(){
            return new Address(
                    unit,
                    street,
                    city,
                    country
            );
        }
    }

    private final int unit;
    private final String street;
    private final String city;
    private final String country;

    public Address(final int unit,
                   final String street,
                   final String city,
                   final String country){
        this.unit = unit;
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public int unit(){
        return unit;
    }

    public String street(){
        return street;
    }

    public String city(){
        return city;
    }

    public String country(){
        return country;
    }
}
