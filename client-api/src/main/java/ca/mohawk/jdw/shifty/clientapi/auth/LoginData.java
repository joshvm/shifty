package ca.mohawk.jdw.shifty.clientapi.auth;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

public class LoginData {

    private final String user;
    private final String pass;

    public LoginData(final String user,
                     final String pass){
        this.user = user;
        this.pass = pass;
    }

    public String user(){
        return user;
    }

    public String pass(){
        return pass;
    }
}
