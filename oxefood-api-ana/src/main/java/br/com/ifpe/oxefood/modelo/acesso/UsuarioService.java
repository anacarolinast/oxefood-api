package br.com.ifpe.oxefood.modelo.acesso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ifpe.oxefood.modelo.seguranca.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.logging.Logger;

@Service
public class UsuarioService implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(UsuarioService.class.getName());

    @Autowired
    private UsuarioRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public UsuarioService(UsuarioRepository userRepository, @Lazy AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.repository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario authenticate(String username, String password) {
        logger.info("Authenticating user: " + username);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            logger.severe("Authentication failed for user: " + username + " - " + e.getMessage());
            throw e;
        }

        Usuario user = repository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.severe("User not found: " + username);
                    return new UsernameNotFoundException("User not found");
                });

        logger.info("User authenticated successfully: " + username);
        return user;
    }

    @Transactional
    public Usuario findByUsername(String username) {
        logger.info("Finding user by username: " + username);

        Usuario user = repository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.severe("User not found: " + username);
                    return new UsernameNotFoundException("User not found");
                });

        logger.info("User found: " + username);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: " + username);

        UserDetails user = repository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.severe("User not found: " + username);
                    return new UsernameNotFoundException("User not found");
                });

        logger.info("User loaded: " + username);
        return user;
    }

    @Transactional
    public Usuario save(Usuario user) {
        if (user == null) {
            logger.severe("Attempted to save null user object");
            throw new IllegalArgumentException("User cannot be null");
        }

        logger.info("Saving user: " + user.getUsername());

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setHabilitado(Boolean.TRUE);
            Usuario savedUser = repository.save(user);
            logger.info("User saved successfully: " + user.getUsername());
            return savedUser;
        } catch (Exception e) {
            logger.severe("Error saving user: " + user.getUsername() + " - " + e.getMessage());
            throw e;
        }
    }

    public Usuario obterUsuarioLogado(HttpServletRequest request) {
        logger.info("Obtaining logged-in user from request");

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);

            logger.info("Extracted username from JWT: " + userEmail);

            Usuario user = findByUsername(userEmail);
            logger.info("Logged-in user obtained: " + userEmail);
            return user;
        }

        logger.warning("Authorization header is missing or not in the expected format");
        return null;
    }
}
