package wojtek.arabia.gateway.utils;

import org.springframework.stereotype.Component;
import wojtek.arabia.gateway.inbound.ClientPackageCreationRequest;
import wojtek.arabia.gateway.inbound.ClientRegistrationRequest;
import wojtek.arabia.gateway.inbound.ClientVerificationRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class RequestValidator {

    public static final HashMap<String, String> countryPrefixMap = new HashMap<>() {{
        put("Poland", "+48");
        put("Germany", "+49");
        put("Austria", "+43");
    }};

    public boolean clientRegistrationRequestIsValid(ClientRegistrationRequest request) {
        Set<String> keysSet = countryPrefixMap.keySet();
        return keysSet.contains(request.getCountry());
    }

    public boolean clientVerificationRequestIsValid(ClientVerificationRequest request){
        Set<String> keysSet = countryPrefixMap.keySet();
        return keysSet.contains(request.getCountry()) && !request.getSmsCode().isBlank();
    }

    public boolean clientPackageCreationRequestIsValid(ClientPackageCreationRequest request){
        List<PackageType> packageTypeList = List.of(PackageType.values());
        List<DeliveryCountry> deliveryCountryList = List.of(DeliveryCountry.values());
        return !request.getUserId().toString().isBlank() && packageTypeList.contains(request.getPackageType()) && deliveryCountryList.contains(request.getDeliveryCountry());
    }


}
