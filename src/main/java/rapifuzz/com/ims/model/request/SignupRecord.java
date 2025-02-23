package rapifuzz.com.ims.model.request;

import rapifuzz.com.ims.constants.enums.UserType;

public record SignupRecord(String firstName, String lastName, String email, String address, String country, String state, String city, String pincode, String phoneCode, Long mobileNumber, String password, UserType userType) {
}
