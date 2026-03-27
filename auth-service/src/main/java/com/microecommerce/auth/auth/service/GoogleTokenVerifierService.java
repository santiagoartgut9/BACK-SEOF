package com.microecommerce.auth.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.microecommerce.auth.auth.model.VerifiedGoogleUser;
import com.microecommerce.auth.shared.exception.BusinessException;
import com.microecommerce.auth.shared.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleTokenVerifierService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifierService(@Value("${auth.google.client-id}") String clientId,
            @Value("${auth.google.issuer}") String issuer) {
        List<String> issuers = buildIssuerAllowList(issuer);
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .setIssuers(issuers)
                .build();
    }

    private static List<String> buildIssuerAllowList(String configuredIssuer) {
        if (configuredIssuer == null || configuredIssuer.isBlank()) {
            return List.of("https://accounts.google.com", "accounts.google.com");
        }

        String trimmed = configuredIssuer.trim();
        List<String> issuers = new ArrayList<>();
        issuers.add(trimmed);

        if (trimmed.startsWith("https://")) {
            issuers.add(trimmed.substring("https://".length()));
        } else if (!trimmed.startsWith("http://")) {
            issuers.add("https://" + trimmed);
        }

        // Ensure the two canonical issuers are always accepted.
        if (!issuers.contains("https://accounts.google.com")) {
            issuers.add("https://accounts.google.com");
        }
        if (!issuers.contains("accounts.google.com")) {
            issuers.add("accounts.google.com");
        }

        return List.copyOf(issuers);
    }

    public VerifiedGoogleUser verify(String idToken) {
        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                throw new UnauthorizedException("Token de Google inválido");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            Object emailVerifiedClaim = payload.get("email_verified");
            boolean emailVerified = Boolean.TRUE.equals(emailVerifiedClaim)
                    || "true".equals(String.valueOf(emailVerifiedClaim));

            if (!emailVerified) {
                throw new UnauthorizedException("La cuenta de Google no tiene email verificado");
            }

            String subject = payload.getSubject();
            String email = payload.getEmail();
            String fullName = (String) payload.get("name");

            if (subject == null || subject.isBlank() || email == null || email.isBlank()) {
                throw new BusinessException("El token de Google no contiene claims obligatorias");
            }

            if (fullName == null || fullName.isBlank()) {
                fullName = email;
            }

            return new VerifiedGoogleUser(subject, email, fullName);
        } catch (GeneralSecurityException | IOException ex) {
            throw new UnauthorizedException("No se pudo validar el token de Google");
        }
    }
}
