package wizardspace.user;

import IxLambdaBackend.auth.AuthenticationContext;

public class Auth {
    private static String KEY = "a829h;}leifjccghntjasctchtgdkucjrvirnduhielrudfgt";
    private static String ISSUER = "WizardSpace";
    private static String claim_ID = "id";

    public static AuthenticationContext getAuthenticationContext() {
        return new AuthenticationContext(KEY, ISSUER, claim_ID);
    }
}
