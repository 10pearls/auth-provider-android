package com.tenpearls.auth.core;

import android.support.annotation.Nullable;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.tenpearls.auth.entities.User;
import com.tenpearls.auth.handlers.SessionHandler;
import com.tenpearls.auth.utilities.PreferenceUtility;

public class Session {

    private static Session instance;

    private CognitoUserSession session;

    private Session(CognitoUserSession session, User user) {
        this.session = session;
    }

    static Session create(CognitoUserSession session, User user) {
        instance = new Session(session, user);
        return instance;
    }

    /**
     * Returns session for currently logged in user.
     * @return Session object or null if no user currently logged in.
     */
    @Nullable
    public static Session current() {
        if (instance == null) {
            restoreSessionIfExists();
        }

        return instance;
    }

    /**
     * Attempts to retrieve session either from cache or if session has been expired,
     * reports failure.
     * @param listener Listener to listen to session retrieval events
     */
    public static void retrieveSession(Listener listener) {
        Session currentSession = Session.current();

        if (currentSession != null && currentSession.isValid())
            listener.onSessionRetrievalSuccess(currentSession);
        else
            Session.refresh(listener);
    }

    private static void restoreSessionIfExists() {
        AuthProvider.getInstance().getUserPool().getCurrentUser().getSession(new SessionHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Session.create(userSession, PreferenceUtility.getUser());
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }

    void clear() {
        Session.instance = null;
        session = null;
    }

    /**
     * Returns if current session is valid.
     * @return boolean to identify the validity of current session.
     */
    public boolean isValid(){
        return session != null && session.isValid();
    }

    /**
     * Returns currently signed in user.
     * @return currently signed in user.
     */
    public User getUser() {
        return PreferenceUtility.getUser();
    }

    /**
     * Returns refresh token provided by AWS Cognito UserPool SDK.
     * @return String containing refresh token.
     */
    public String getRefreshToken(){
        return this.session.getRefreshToken().getToken();
    }

    /**
     * Returns access token provided by AWS Cognito UserPool SDK.
     * @return String containing access token.
     */
    public String getAccessToken(){
        return this.session.getAccessToken().getJWTToken();
    }

    /**
     * Returns id token provided by AWS Cognito UserPool SDK.
     * @return String containing id token.
     */
    public String getIdToken(){
        return this.session.getIdToken().getJWTToken();
    }

    private static void refresh(final Listener listener){

        AuthProvider.getInstance().getUserPool().getCurrentUser().getSessionInBackground(new SessionHandler(true) {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Session currentSession = Session.create(userSession, PreferenceUtility.getUser());
                listener.onSessionRetrievalSuccess(currentSession);
            }

            @Override
            public void onFailure(Exception exception) {
                listener.onSessionRetrievalFailed(exception);
            }
        });
    }


    /**
     * Listener interface to listen to failed and successful session retrieval events.
     */
    public interface Listener {
        /**
         * Event notifying successful retrieval of session.
         * @param currentSession Retrieved session.
         */
        void onSessionRetrievalSuccess(Session currentSession);

        /**
         * This event gets called whenever call to login method gets failed.
         * @param exception Reason to why the call failed.
         */
        void onSessionRetrievalFailed(Exception exception);
    }
}
