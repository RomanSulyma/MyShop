package net.service.impl;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.scope.ExtendedPermissions;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;
import net.model.SocialAccount;
import net.service.SocialService;

class FacebookSocialService implements SocialService {
    private final String idClient;
    private final String secret;
    private final String redirectUrl;
    //Takes parameters from Property file
    FacebookSocialService(ServiceManager serviceManager) {
        super();
        idClient = serviceManager.getApplicationProperty("social.facebook.idClient");
        secret = serviceManager.getApplicationProperty("social.facebook.secret");
        redirectUrl = serviceManager.getApplicationProperty("app.host") + "/from-social";
    }
    //Receive user token and transfer it to our application
    @Override
    public String getAuthorizeUrl() {
        ScopeBuilder scopeBuilder = new ScopeBuilder();
        scopeBuilder.addPermission(ExtendedPermissions.EMAIL);
        FacebookClient client = new DefaultFacebookClient(Version.VERSION_2_5);
        return client.getLoginDialogUrl(idClient, redirectUrl, scopeBuilder);
    }
    //After receiving authToken receives information about the user and creates a SocialAccount object
    @Override
    public SocialAccount getSocialAccount(String authToken) {
        FacebookClient client = new DefaultFacebookClient(Version.VERSION_2_5);
        FacebookClient.AccessToken accessToken = client.obtainUserAccessToken(idClient, secret, redirectUrl, authToken);
        client = new DefaultFacebookClient(accessToken.getAccessToken(), Version.VERSION_2_5);
        User user = client.fetchObject("me", User.class, Parameter.with("fields", "name,email,first_name,last_name"));
        //String avatarUrl = String.format("https://graph.facebook.com/v4.0/%s/picture?type=small", user.getId());
        return new SocialAccount(user.getFirstName(), user.getEmail()/*, avatarUrl*/);
    }
}
