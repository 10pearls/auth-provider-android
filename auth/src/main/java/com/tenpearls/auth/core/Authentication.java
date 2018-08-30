package com.tenpearls.auth.core;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.tenpearls.auth.entities.Credentials;
import com.tenpearls.auth.entities.User;
import com.tenpearls.auth.utilities.PreferenceUtility;

/**
 * Authentication feature provides ability to login and logout the user.
 */
public class Authentication extends Feature {

    Authentication(CognitoUserPool userPool) {
        super(userPool);
    }


    /**
     * Call to methods attempts to authenticate the user through provided credentials.
     * It also fetches the details of the user after authenticating and provides the complete user instance in {@link AuthenticationListener#onLoginSuccessful(User)} event.
     * @param credentials Credentials of the user to log in.
     * @param listener to listen events of successful and failed login events.
     */
    public void login(final Credentials credentials, final AuthenticationListener listener){

        userPool.getUser(credentials.getUsername()).getSessionInBackground(new AuthenticationHandler() {
            @Override
            public void onSuccess(final CognitoUserSession userSession, CognitoDevice newDevice) {

                final CognitoUser cognitoUser = userPool.getUser(credentials.getUsername());
                cognitoUser.getDetailsInBackground(new GetDetailsHandler() {
                    @Override
                    public void onSuccess(CognitoUserDetails cognitoUserDetails) {

                        User user = new User(cognitoUser, cognitoUserDetails);
                        PreferenceUtility.setUser(user);
                        Session.create(userSession, user);

                        listener.onLoginSuccessful(user);

                    }

                    @Override
                    public void onFailure(Exception exception) {

                        listener.onLoginFailed(exception);

                    }
                });

            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation continuation, String userId) {
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(credentials.getUsername(), credentials.getPassword(), null);
                continuation.setAuthenticationDetails(authenticationDetails);
                continuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                // TODO: 12/08/2018 Need to implement this
            }

            @Override
            public void onFailure(Exception exception) {
                listener.onLoginFailed(exception);
            }
        });
    }


    /**
     * Performs logout for the currently logged in user.
     */
    public void logout(){
        userPool.getCurrentUser().signOut();
        PreferenceUtility.clearAllPreferences();
        Session.current().clear();
    }

    /**
     * Listener interface to listen to failed and successful login events.
     */
    public interface AuthenticationListener {
        /**
         * This method gets called whenever call to login method gets successful.
         * @param user User instance that has just been logged in.
         */
        void onLoginSuccessful(User user);

        /**
         * This method gets called whenever call to login method gets failed.
         * @param exception Reason to why the call failed.
         */
        void onLoginFailed(Exception exception);
    }

}
