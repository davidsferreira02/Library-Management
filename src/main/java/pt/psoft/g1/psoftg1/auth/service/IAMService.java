package pt.psoft.g1.psoftg1.auth.service;

import com.nimbusds.oauth2.sdk.ParseException;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

public interface IAMService {
    User authenticate(String authorizationCode) throws ParseException;
}
