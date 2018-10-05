package com.tenpearls.auth.core;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.tenpearls.auth.enums.Regions;

/**
 * Class used to provide features regarding authentication.
 */
public class AuthProvider {

    @SuppressLint("StaticFieldLeak")
    private static AuthProvider instance;

    private final Context context;

    private final CognitoUserPool userPool;

    //Features
    private Registration registration;
    private Authentication auth;
    private PasswordReset passwordReset;
    private InfoUpdate infoUpdate;

    private AuthProvider(Context context, String userPoolId, String clientId, String clientSecret, Regions region) {
        this.context = context.getApplicationContext();
        this.userPool = new CognitoUserPool(this.context, userPoolId, clientId, clientSecret, region.getCognitoRegion());
    }

    /**
     * Initializes the AuthProvider class and Cognito User Pool. Ideally, it should be called from the {@link Application#onCreate()} method of application class.
     * @param context Android application context
     * @param userPoolId User-pool-Id of the user-pool provided at the Cognito Identity Provider developer console.
     * @param clientId Client-Id generated for this app and user-pool at the Cognito Identity Provider developer console.
     * @param clientSecret Client Secret provided by Cognito Console.
     * @param region AWS region.
     */
    public static void initialize(Context context, String userPoolId, String clientId, String clientSecret, Regions region){
        if (instance != null)
            return;

        instance = new AuthProvider(context, userPoolId, clientId, clientSecret, region);
    }

    /**
     * Provides singleton instance of AuthProvider to get authentication features.
     * @return Singleton instance of AuthProvider.
     *
     * @throws IllegalStateException If this method is called without calling {@link #initialize(Context, String, String, String, Regions)} earlier.
     */
    public static AuthProvider getInstance() {
        if (instance == null)
            throw new IllegalStateException("Not initialized. Call initialize() method to initialize before fetching the instance");

        return instance;
    }


    /**
     * Returns an instance of {@link Registration} feature.
     * Instance of the feature is created only once and the same instance
     * is returned afterwards to share the data between requests.
     * @return instance of {@link Registration} feature.
     */
    public Registration getRegistrationFeature(){
        if (registration == null)
            registration = new Registration(userPool);

        return registration;
    }


    /**
     * Returns an instance of {@link Authentication} feature.
     * Instance of the feature is created only once and the same instance
     * is returned afterwards to share the data between requests.
     * @return instance of {@link Authentication} feature.
     */
    public Authentication getAuthenticationFeature(){
        if (auth == null)
            auth = new Authentication(userPool);

        return auth;
    }


    /**
     * Returns an instance of {@link PasswordReset} feature.
     * Instance of the feature is created only once and the same instance
     * is returned afterwards to share the data between requests.
     * @return instance of {@link PasswordReset} feature.
     */
    public PasswordReset getPasswordResetFeature(){
        if (passwordReset == null)
            passwordReset = new PasswordReset(userPool);

        return passwordReset;
    }


    /**
     * Returns an instance of {@link InfoUpdate} feature.
     * Instance of the feature is created only once and the same instance
     * is returned afterwards to share the data between requests.
     * @return instance of {@link InfoUpdate} feature.
     */
    public InfoUpdate getInfoUpdateFeature(){
        if (infoUpdate == null)
            infoUpdate = new InfoUpdate(userPool);

        return infoUpdate;
    }


    /**
     * Provides Cognito's User Pool instance.
     * @return {@link CognitoUserPool} instance.
     */
    CognitoUserPool getUserPool(){
        return userPool;
    }


    /**
     * Returns application context provided through initialize method.
     * @return Android application context
     */
    public Context getContext() {
        return context;
    }
}
