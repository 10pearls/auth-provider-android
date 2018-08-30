package com.tenpearls.auth.enums;

/**
 * Enumeration of region names for cognito authentications
 */
public enum Regions {

    GovCloud(com.amazonaws.regions.Regions.GovCloud),


    US_EAST_1(com.amazonaws.regions.Regions.US_EAST_1),


    US_EAST_2(com.amazonaws.regions.Regions.US_EAST_2),


    US_WEST_1(com.amazonaws.regions.Regions.US_WEST_1),


    US_WEST_2(com.amazonaws.regions.Regions.US_WEST_2),


    EU_WEST_1(com.amazonaws.regions.Regions.EU_WEST_1),


    EU_WEST_2(com.amazonaws.regions.Regions.EU_WEST_2),


    EU_WEST_3(com.amazonaws.regions.Regions.EU_WEST_3),


    EU_CENTRAL_1(com.amazonaws.regions.Regions.EU_CENTRAL_1),


    AP_SOUTH_1(com.amazonaws.regions.Regions.AP_SOUTH_1),


    AP_SOUTHEAST_1(com.amazonaws.regions.Regions.AP_SOUTHEAST_1),


    AP_SOUTHEAST_2(com.amazonaws.regions.Regions.AP_SOUTHEAST_2),


    AP_NORTHEAST_1(com.amazonaws.regions.Regions.AP_NORTHEAST_1),


    AP_NORTHEAST_2(com.amazonaws.regions.Regions.AP_NORTHEAST_2),


    SA_EAST_1(com.amazonaws.regions.Regions.SA_EAST_1),


    CA_CENTRAL_1(com.amazonaws.regions.Regions.CA_CENTRAL_1),


    CN_NORTH_1(com.amazonaws.regions.Regions.CN_NORTH_1),


    CN_NORTHWEST_1(com.amazonaws.regions.Regions.CN_NORTHWEST_1);

    private final com.amazonaws.regions.Regions cognitoRegion;

    Regions(com.amazonaws.regions.Regions cognitoRegion) {
        this.cognitoRegion = cognitoRegion;
    }

    public com.amazonaws.regions.Regions getCognitoRegion() {
        return cognitoRegion;
    }
}
