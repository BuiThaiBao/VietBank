package com.vti.VietBank.service;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.vti.VietBank.dto.request.auth.AuthenticationRequest;
import com.vti.VietBank.dto.request.auth.IntrospectRequest;
import com.vti.VietBank.dto.request.auth.LogoutRequest;
import com.vti.VietBank.dto.request.auth.RefreshRequest;
import com.vti.VietBank.dto.response.auth.AuthenticationResponse;
import com.vti.VietBank.dto.response.auth.IntrospectResponse;

public interface IAuthenticationService {
    public IntrospectResponse introspectToken(IntrospectRequest request) throws JOSEException, ParseException;

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    public void logout(LogoutRequest request) throws ParseException, JOSEException;
    ;

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
