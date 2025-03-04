package dependencies;

import dependencies.customer.Customer;
import dependencies.databse.CustomerDatabase;
import org.mindrot.jbcrypt.BCrypt;
import utils.JwtUtil;

import java.util.logging.Logger;

import static utils.JwtUtil.invalidateToken;


public class AuthFacade {

    private static final Logger logger = Logger.getLogger(AuthFacade.class.getName());

    private static AuthFacade instance;

    public static synchronized AuthFacade getInstance() {
        if (instance == null) {
            instance = new AuthFacade();
        }
        return instance;
    }

    public String loginWithCredentials(String email, String password) {
        try {
            Customer customer = CustomerDatabase.getInstance().getCustomerByEmail(email);

            if (customer == null) {
                logger.warning("Authentication failed for user (no such email): " + email);
                return null;
            }

            if (checkPassword(password, customer.getPassword())) {
                String token = JwtUtil.createToken(customer.getEmail());
                logger.info("User authenticated successfully: " + email);
                return token; // Return the generated token
            } else {
                logger.warning("Authentication failed for user: " + email + " (incorrect password)");
                return null;
            }
        } catch (Exception e) {
            logger.severe("Error during authentication: " + e.getMessage());
            return null;
        }
    }

    public boolean register(String name,String email, String password) {
        try {
            Customer customer = new Customer(name,email, hashPassword(password));
            CustomerDatabase.getInstance().addCustomer(customer);
            logger.info("User registered successfully: " + email);
            return true;
        } catch (Exception e) {
            logger.severe("Error during registration: " + e.getMessage());
            return false;
        }
    }

    public boolean loginWithGoogle(String googleCredential) {
        try {
            boolean isGoogleLoginSuccessful = googleAuthenticate(googleCredential);
            if (isGoogleLoginSuccessful) {
                logger.info("Google login successful for: " + googleCredential);
                return true;
            } else {
                logger.warning("Google login failed for: " + googleCredential);
                return false;
            }
        } catch (Exception e) {
            logger.severe("Error during Google login: " + e.getMessage());
            return false;
        }
    }

    private boolean googleAuthenticate(String googleCredential) {
        return googleCredential.equals("valid-google-token");
    }

    public boolean loginWithFacebook(String facebookToken) {
        try {
            boolean isFacebookLoginSuccessful = facebookAuthenticate(facebookToken);
            if (isFacebookLoginSuccessful) {
                logger.info("Facebook login successful for: " + facebookToken);
                return true;
            } else {
                logger.warning("Facebook login failed for: " + facebookToken);
                return false;
            }
        } catch (Exception e) {
            logger.severe("Error during Facebook login: " + e.getMessage());
            return false;
        }
    }

    private boolean facebookAuthenticate(String facebookToken) {
        return facebookToken.equals("valid-facebook-token");
    }

    public boolean isLoggedIn() {
        boolean loggedIn = checkSession();
        logger.info("User logged in status: " + loggedIn);
        return loggedIn;
    }

    private boolean checkSession() {
        return true;
    }

    public boolean validateToken(String token) {
        return JwtUtil.validateToken(token);
    }

    public boolean logout(String token) {
        try {
            invalidateToken(token);
            logger.info("User logged out successfully.");
            return true;
        } catch (Exception e) {
            logger.severe("Error during logout: " + e.getMessage());
            return false;
        }
    }


    public String hashPassword(String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashedPassword;
    }

    public boolean checkPassword(String password, String storedHashedPassword) {
        return BCrypt.checkpw(password, storedHashedPassword);
    }
}
