package com.endava.service_system.service;

import com.endava.service_system.dao.TokenDao;
import com.endava.service_system.model.entities.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenDao tokenDao;

    public Token save(Token token){
        return tokenDao.save(token);
    }

    public Optional<Token> getToken(String token){
        return tokenDao.getTokenByTokenEquals(token);
    }
}
