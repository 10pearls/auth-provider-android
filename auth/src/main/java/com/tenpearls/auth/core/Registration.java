package com.tenpearls.auth.core;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.tenpearls.auth.entities.Credentials;
import com.tenpearls.auth.entities.User;

import java.util.Map;


/**
 * Registration feature provides ability to register data.
 */
public class Registration extends Feature {

    private CognitoUser cognitoUser;
    private RegistrationListener registrationListener;

    private String username;

    Registration(CognitoUserPool userPool) {
        super(userPool);
    }

    /**
     * Attempts to register a user with provided credentials and attributes
     * @param credentials Username and Password combination
     * @param attributes Attributes of the user
     * @param registrationListener Listener to notify events regarding registration status
     */
    public void register(Credentials credentials, User.Attributes attributes, RegistrationListener registrationListener) {
        this.registrationListener = registrationListener;
        CognitoUserAttributes userAttributes = attributes.toCognitoAttributes();

        userPool.signUpInBackground(username = credentials.getUsername(), credentials.getPassword(), userAttributes, null, new SignUpHandler() {
            @Override
            public void onSuccess(CognitoUser user, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                cognitoUser = user;
                if (!userConfirmed)
                    onConfirmationRequired(cognitoUserCodeDeliveryDetails);
                else
                    onSignUpSuccessful();
            }

            @Override
            public void onFailure(Exception exception) {
                Registration.this.registrationListener.onSignUpFailed(exception);
            }
        });
    }


    /**
     * Event which gets fired on successful sign up.
     */
    private void onSignUpSuccessful() {
        if (registrationListener == null)
            return;

        registrationListener.onSignUpSuccessful();
    }


    /**
     * Event which gets fired when account verification is required.
     * @param cognitoUserCodeDeliveryDetails Delivery details received from {@link CognitoUserPool#signUpInBackground(String, String, CognitoUserAttributes, Map, SignUpHandler)}
     *                                       method's successful response.
     */
    private void onConfirmationRequired(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
        registrationListener.onUserConfirmationRequired(cognitoUserCodeDeliveryDetails.getDestination(), cognitoUserCodeDeliveryDetails.getDeliveryMedium(), cognitoUserCodeDeliveryDetails.getAttributeName());
    }


    /**
     * Validates the registration using confirmation code provided through email/sms.
     * <b>Note:</b> Use this method only when registration was done recently using this exact same instance.
     * @param confirmationCode verification code provided through email/sms.
     * @throws IllegalStateException If instance doesn't contain username.
     */
    public void confirmRegistration(String confirmationCode) {
        if (TextUtils.isEmpty(username))
            throw new IllegalStateException("Username does not currently exist for this instance, call confirmRegistration(String username, String confirmationCode) to pass username");

        confirmRegistration(username, confirmationCode, registrationListener);
    }

    /**
     * Validates the registration using confirmation code provided through email/sms.
     * @param username Username of the user for which confirmation code is being provided.
     * @param confirmationCode verification code provided through email/sms.
     * @param listener listener to listen to confirmation events.
     */
    public void confirmRegistration(String username, String confirmationCode, RegistrationListener listener) {
        this.username = username;
        this.registrationListener = listener;

        userPool.getUser(username).confirmSignUpInBackground(confirmationCode, true, new GenericHandler() {
            @Override
            public void onSuccess() {
                //registrationListener.onUserConfirmed();
                onSignUpSuccessful();
            }

            @Override
            public void onFailure(Exception exception) {
                registrationListener.onConfirmationFailure(exception);
            }
        });
    }

    /**
     * Re-sends the confirmation code to user that just signed up using current instance.
     * @throws IllegalStateException If instance doesn't contain username.
     */
    public void resendConfirmationCode() {
        if (TextUtils.isEmpty(username))
            throw new IllegalStateException("Username does not currently exist for this instance, call resendConfirmationCode(String username) to pass username");

        resendConfirmationCode(username, registrationListener);
    }

    /**
     * Re-sends the confirmation code to user.
     * @param username Username of the user for which confirmation code needs to be re-sent.
     * @param listener listener to listen to re-sent events.
     */
    public void resendConfirmationCode(String username, @Nullable RegistrationListener listener) {
        this.username = username;

        if (listener != null) this.registrationListener = listener;

        userPool.getUser(username).resendConfirmationCodeInBackground(new VerificationHandler() {
            @Override
            public void onSuccess(CognitoUserCodeDeliveryDetails verificationCodeDeliveryMedium) {
                if (registrationListener == null)
                    return;

                registrationListener.onConfirmationCodeReSent();
            }

            @Override
            public void onFailure(Exception exception) {
                if (registrationListener == null)
                    return;

                registrationListener.onConfirmationCodeSendingFailed(exception);
            }
        });
    }

    /**
     * Listener interface to listen to registration events. It also notifies of user confirmation events.
     */
    public abstract static class RegistrationListener {
        /**
         * This method notifies about the event when sign up gets successful after user confirmation.
         */
        public abstract void onSignUpSuccessful();

        /**
         * This method notifies when sign up fails.
         * @param exception Reason to why request failed
         */
        public abstract void onSignUpFailed(Exception exception);

        /**
         * This method notifies about sent verification code
         * @param destination Address at which verification code is sent.
         * @param deliveryMedium Medium through which verification code is sent.
         * @param deliveryMode Mode of delivery.
         */
        public void onUserConfirmationRequired(String destination, String deliveryMedium, String deliveryMode) { }

        //default void onUserConfirmed() { }

        /**
         * This method notifies when confirmation of user gets failed.
         * @param exception Reason to why confirmation failed.
         */
        public void onConfirmationFailure(Exception exception) { }

        /**
         * This method notifies when confirmation code successfully gets re-sent.
         */
        public void onConfirmationCodeReSent(){ }

        /**
         * This method notifies when confirmation code fails to get re-sent.
         * @param exception Reason to why resending code got failed.
         */
        public void onConfirmationCodeSendingFailed(Exception exception) {}
    }
}
