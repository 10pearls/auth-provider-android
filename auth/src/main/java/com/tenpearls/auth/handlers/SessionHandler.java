package com.tenpearls.auth.handlers;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.tenpearls.auth.core.AuthProvider;

public abstract class SessionHandler implements AuthenticationHandler {

    private boolean isRefreshing = false;

    public SessionHandler() {
    }

    public SessionHandler(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
    }

    @Override
    abstract public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice);

    @Override
    final public void getAuthenticationDetails(AuthenticationContinuation continuation, String userId) {
        if (!isRefreshing)
            return;

        AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, "", null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    @Override
    final public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

    }

    @Override
    final public void authenticationChallenge(ChallengeContinuation continuation) {

    }

    @Override
    abstract public void onFailure(Exception exception);
}
