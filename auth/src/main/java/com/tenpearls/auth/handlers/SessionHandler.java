package com.tenpearls.auth.handlers;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;

public abstract class SessionHandler implements AuthenticationHandler {
    @Override
    abstract public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice);

    @Override
    final public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {

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
