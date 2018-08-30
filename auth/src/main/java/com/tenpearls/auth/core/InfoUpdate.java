package com.tenpearls.auth.core;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.tenpearls.auth.entities.User;
import com.tenpearls.auth.utilities.PreferenceUtility;

/**
 * InfoUpdate feature provides ability to update data.
 */
public class InfoUpdate extends Feature {
    InfoUpdate(CognitoUserPool userPool) {
        super(userPool);
    }

    /**
     * Call to this method will update the information of currently signed in user.
     * @param listener Listener to listen the events of password reset events.
     */
    public void refreshCurrentUser(final UpdateUserListener listener){
        userPool.getCurrentUser().getDetailsInBackground(new GetDetailsHandler() {
            @Override
            public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                User currentUser = PreferenceUtility.getUser();
                currentUser.getAttributes().fromCognitoAttributes(cognitoUserDetails.getAttributes());
                PreferenceUtility.setUser(currentUser);

                listener.onSuccess(currentUser);
            }

            @Override
            public void onFailure(Exception exception) {
                listener.onFailure(exception);
            }
        });
    }

    /**
     * Listener interface to listen to failed and successful user info update events.
     */
    public interface UpdateUserListener {

        /**
         * This method gets called when info of the user has successfully been updated.
         * @param user Update user object.
         */
        void onSuccess(User user);

        /**
         * This method notifies if fetching updated information has failed.
         * @param exception Reason to why the call failed.
         */
        void onFailure(Exception exception);
    }
}
