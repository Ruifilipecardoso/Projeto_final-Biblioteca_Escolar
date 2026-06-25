package com.iefp.SistemaInternoBibliotecaEscolar.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;


@Service
public class JwtService {
    //Chave de segurança gerada automaticamente
    private final SecretKey CHAVE_SECRETA = Jwts.SIG.HS256.key().build();

    //O Login expira em 24 horas = milissegundos
    private final long TEMPO_EXPIRACAO = 86400000;



    /*O que é que este metodo faz?
    Quando o teu Frontend (VS Code) enviar o email e a senha corretos, o Backend vai chamar este metodo.
    Ele vai pegar no email e no perfil do utilizador, vai "embrulhá-los" juntamente com uma data de validade de 24 horas
    e vai trancar tudo com a tua CHAVE_SECRETA.
    O resultado é aquela linha de texto gigante e codificada (o Token) que vai ser enviada para o navegador do utilizador.*/
    //Metodo que cria o Token JWT baseado no email e no perfil do utilizador
    public String gerarToken(String email, String perfil) {
        return Jwts.builder()
                .subject(email)
                .claim("perfil", perfil)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TEMPO_EXPIRACAO))
                .signWith(CHAVE_SECRETA)
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    //Metodo utilitário para extrair qualquer informação específica (Claim) do Token
    public <T> T extrairClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(CHAVE_SECRETA)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }

    //Verifica se o Token pertence ap email do utilizador e se ainda é válido
    public boolean isTokenValido(String token, String email) {
        final String tokenEmail = extrairEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpirado(token));
    }

    //Verifica se a data de expiração do token já passou
    private boolean isTokenExpirado(String token) {
        return extrairClaim(token, Claims::getExpiration).before(new Date());
    }
}
