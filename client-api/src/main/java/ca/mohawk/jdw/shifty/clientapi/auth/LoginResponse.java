package ca.mohawk.jdw.shifty.clientapi.auth;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.clientapi.ShiftyClient;
import ca.mohawk.jdw.shifty.clientapi.event.response.ResponseCode;
import ca.mohawk.jdw.shifty.clientapi.utils.Utils;
import java.util.Map;

public class LoginResponse {

    public enum Code implements ResponseCode {

        ALREADY_LOGGED_IN(1),
        EMPLOYEE_NOT_FOUND(2),
        PASS_MISMATCH(3),
        EMPLOYEE_ALREADY_ACTIVE(4);

        private static final Map<Integer, ResponseCode> MAP = Utils.map(values(), ResponseCode::id);

        private final int id;

        Code(final int id){
            this.id = id;
        }

        @Override
        public int id(){
            return id;
        }

        public static ResponseCode forId(final int id){
            return ResponseCode.forId(MAP, id);
        }
    }

    private final ShiftyClient.Config config;
    private final LoginData data;
    private final ResponseCode code;
    private final ShiftyClient client;

    public LoginResponse(final ShiftyClient.Config config,
                         final LoginData data,
                         final ResponseCode code,
                         final ShiftyClient client){
        this.config = config;
        this.data = data;
        this.code = code;
        this.client = client;
    }

    public ShiftyClient.Config config(){
        return config;
    }

    public LoginData data(){
        return data;
    }

    public ResponseCode code(){
        return code;
    }

    public ShiftyClient client(){
        return client;
    }
}
