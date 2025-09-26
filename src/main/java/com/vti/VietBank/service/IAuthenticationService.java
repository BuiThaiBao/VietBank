package com.vti.VietBank.service;

import com.nimbusds.jose.JOSEException;
import com.vti.VietBank.dto.request.AuthenticationRequest;
import com.vti.VietBank.dto.request.IntrospectRequest;
import com.vti.VietBank.dto.request.LogoutRequest;
import com.vti.VietBank.dto.request.RefreshRequest;
import com.vti.VietBank.dto.response.AuthenticationResponse;
import com.vti.VietBank.dto.response.IntrospectResponse;
import com.vti.VietBank.entity.User;

import java.text.ParseException;

public interface IAuthenticationService {
    public IntrospectResponse introspectToken(IntrospectRequest request) throws JOSEException, ParseException;

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    public void logout(LogoutRequest request) throws ParseException, JOSEException;;

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
