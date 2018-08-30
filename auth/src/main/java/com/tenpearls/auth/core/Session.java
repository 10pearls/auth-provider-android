package com.tenpearls.auth.core;

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

    static void create(CognitoUserSession session, User user) {
        instance = new Session(session, user);
    }

    /**
     * Returns session for currently logged in user.
     * @return Session object or null if no user currently logged in.
     */
    public static Session current() {
        if (instance == null) {
            restoreSessionIfExists();
        }

        return instance;
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
     * Returns currently signed in user.
     * @return currently signed in user.
     */
    public User getUser() {
        return PreferenceUtility.getUser();
    }
}
