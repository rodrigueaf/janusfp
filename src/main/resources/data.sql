INSERT INTO oauth_client_details
	(client_id, resource_ids, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES ('internal', null, 'internal', 'web-app', 'client_credentials', null, 'ROLE_ADMIN', 36000, 3600000, null, true);

INSERT INTO oauth_client_details
	(client_id, resource_ids, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES ('web_app', null, 'web_app', 'openid', 'implicit,refresh_token,password,authorization_code', null, 'ROLE_ADMIN', 36000, 3600000, null, true);
