package net.service;

import net.model.SocialAccount;
//Will return an interface for a redirect, as well as a token for receiving user information.
public interface SocialService {
    String getAuthorizeUrl();

    SocialAccount getSocialAccount(String authToken);
}
