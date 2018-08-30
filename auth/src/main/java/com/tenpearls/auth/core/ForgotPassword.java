package com.tenpearls.auth.core;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;

/**
 * ForgotPassword feature provides ability to reset password of the user.
 */
public class ForgotPassword extends Feature {

    private ForgotPasswordContinuation continuation;

    ForgotPassword(CognitoUserPool userPool) {
        super(userPool);
    }

    /**
     * Initiates the request to reset the password of the user.
     * @param username Username of the user to reset password.
     * @param listener Listener to listen the events of password reset events.
     */
    public void resetPassword(String username, final ForgotPasswordListener listener) {
        userPool.getUser(username).forgotPasswordInBackground(new ForgotPasswordHandler() {

            @Override
            public void onSuccess() {
                listener.onPasswordChangedSuccessfully();
            }

            @Override
            public void getResetCode(ForgotPasswordContinuation continuation) {
                ForgotPassword.this.continuation = continuation;
                CognitoUserCodeDeliveryDetails parameters = continuation.getParameters();
                String deliveryMedium = parameters.getDeliveryMedium();
                String destination = parameters.getDestination();
                listener.onCodeSent(deliveryMedium, destination);
            }

            @Override
            public void onFailure(Exception exception) {
                listener.onPasswordChangingFailed(exception);
            }
        });
    }

    /**
     * Verifies the code sent to the user through email/sms and if correct code is provided,
     * new password will be set for the user.
     * @param code Verification code received through email/sms.
     * @param newPassword New password to set for the user.
     * @throws IllegalStateException if this method is called before requesting to change password earlier.
     */
    public void verifyCode(String code, String newPassword) {
        if (continuation == null)
            throw new IllegalStateException("Forgot password request was not sent. Make sure request to reset password was previously " +
                    "sent through resetPassword(username, forgotPasswordListener) method before trying to verify code.");

        continuation.setPassword(newPassword);
        continuation.setVerificationCode(code);
        continuation.continueTask();
    }

    /**
     * Listener interface to listen to failed and successful password changed events. It also notifies of the verification code sent events.
     */
    public interface ForgotPasswordListener {

        /**
         * This method gets called as soon as password is successfully changed.
         */
        void onPasswordChangedSuccessfully();

        /**
         * This method notifies about sent verification code.
         * @param deliveryMedium Medium through which verification code is sent.
         * @param destination Address at which verification code is sent.
         */
        void onCodeSent(String deliveryMedium, String destination);

        /**
         * This method gets called if request to change password fails.
         * @param exception Reason to why request failed.
         */
        void onPasswordChangingFailed(Exception exception);

    }

}
