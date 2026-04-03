package fr.efreifive.manageplayer.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.efreifive.manageplayer.dto.ErrorResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final String expectedApiKey;

    public ApiKeyFilter(ObjectMapper objectMapper, @Value("${app.security.api-key:dev-api-key}") String expectedApiKey) {
        this.objectMapper = objectMapper;
        this.expectedApiKey = expectedApiKey;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return "/health".equals(path) || path.startsWith("/error");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String providedApiKey = request.getHeader("X-API-KEY");

        if (!expectedApiKey.equals(providedApiKey)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), new ErrorResponse("UNAUTHORIZED", "Cle API manquante ou invalide.", List.of()));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
