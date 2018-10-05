# Auth-Provider Android
Library to facilitate authentication implementation for Android platform using Amazon Cognito Userpools SDK.

## Download
Using Gradle:
```gradle
repositories {
  mavenCentral()
  google()
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.10pearls:auth-provider-android:-SNAPSHOT'
}
```

## Getting Started
Initialize AuthProvider in `onCreate()` method of **Application** class:
```java
@Override
public void onCreate() {
    super.onCreate();
    AuthProvider.initialize(this, USER_POOL_ID, CLIENT_ID, CLIENT_SECRET, COGNITO_REGION);
}
```
#### Authentication:
To perform login, request feature to intiate authentication:
```java
Authentication authenticationFeature = AuthProvider.getInstance().getAuthenticationFeature();
```

Prepare credentials:
```java
Credentials credentials = new Credentials(username, password);
```

Perform authentication:
```java
authenticationFeature.login(credentials, new Authentication.Listener() {
    @Override
    public void onLoginSuccessful(User user) {
        //Logged in successfully
    }

    @Override
    public void onLoginFailed(Exception exception) {
        //Login failure
    }
});
```


#### Registration:
Registration feature can also be requested in the similar way:
```java
Registration registrationFeature = AuthProvider.getInstance().getRegistrationFeature();
```

Prepare user attributes:
```java
User.Attributes attributes = new User.Attributes();
attributes.put("name", name);
attributes.put("email", email);
attributes.put("phone_number", phoneNumber);
```
Initiate registration:

```java
//credentials object can be prepared in a similar way as shown in authentication
registrationFeature.register(credentials, attributes, new Registration.Listener() {
    @Override
    public void onSignUpSuccessful() {
      //Sign up successful
    }

    @Override
    public void onSignUpFailed(Exception exception) {
      //Failed signing up user.
    }
});
```

For complete documentation, see [javadocs](#).

## Features
AuthProvider provides following features:
* Authentication
* Registration and account confirmation
* Password reset

## ProGuard
If you are using proguard, classes defined in this library needs to be ignored:
```
-keep class com.tenpearls.auth.** { *; }
```

## Compatibility
* **Minimum Android SDK:** AuthProvider requires a minimum API level of 19.
* **Compile Android SDK:** AuthProvider requires you to compile against API 27 or later.

## Dependencies
Authprovider uses [Amazon Cognito Userpools SDK](https://docs.aws.amazon.com/aws-mobile/latest/developerguide/getting-started.html)
