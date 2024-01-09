package wojtek.arabia.gateway.utils;

import wojtek.arabia.gateway.inbound.ClientRegistrationRequest;

import java.util.HashMap;
import java.util.Set;

public class RequestValidator {

    public static final HashMap<String, String> countryPrefixMap = new HashMap<>() {{
        put("Poland", "+48");
        put("Germany", "+49");
    }};

    public static boolean clientRegistrationRequestIsValid(ClientRegistrationRequest request) {
        Set<String> keysSet = countryPrefixMap.keySet();
        return keysSet.contains(request.getCountry());
    }




}
