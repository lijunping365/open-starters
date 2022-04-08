package com.saucesubfresh.starter.oauth.token;

import com.saucesubfresh.starter.oauth.authentication.Authentication;

/**
 * @author lijunping on 2022/3/31
 */
public interface TokenEnhancer {

    /**
     * Provides an opportunity for customization of an access token (e.g. through its additional information map) during
     * the process of creating a new token for use by a client.
     *
     * @param accessToken the current access token with its expiration and refresh token
     * @param authentication the current authentication including client and user details
     * @return a new token enhanced with additional information
     */
    AccessToken enhance(AccessToken accessToken, Authentication authentication);

}
