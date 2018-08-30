package com.tenpearls.auth.core;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;

/**
 * Base class of each feature.
 */
class Feature {

    final CognitoUserPool userPool;

    /**
     * Initializes the feature and stores the {@link CognitoUserPool} instance to access requests.
     * @param userPool instance to access user pool requests.
     */
    Feature(CognitoUserPool userPool) {
        this.userPool = userPool;
    }

}
