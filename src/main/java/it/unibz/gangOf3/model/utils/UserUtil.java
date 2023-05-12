package it.unibz.gangOf3.model.utils;

import it.unibz.gangOf3.email.EmailSender;
import it.unibz.gangOf3.model.User;
import it.unibz.gangOf3.model.exceptions.UserAlreadyExistsException;
import it.unibz.gangOf3.model.exceptions.UserNotFoundException;
import it.unibz.gangOf3.util.DatabaseUtil;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserUtil {

    public static void createUser(String name, String email, String password, String type, String emergencyEmail, String emergencyPhone) throws UserAlreadyExistsException, SQLException, IOException, MessagingException {
        String emailSQL = "'" + email + "'";
        password = "'" + password + "'";
        type = "'" + "seller".equals(type) + "'";
        emergencyEmail = emergencyEmail != null ? "'" + emergencyEmail + "'" : "NULL";
        emergencyPhone = emergencyPhone != null ? "'" + emergencyPhone + "'" : "NULL";
        String registrationTokenUUID = UUID.randomUUID().toString();
        String registrationToken = "'" + registrationTokenUUID + "'";
        DatabaseUtil.getConnection()
            .prepareStatement("INSERT INTO users (username, email, password, type, emergencyEmail, emergencyPhone, registrationToken) VALUES ('" + name + "', " + emailSQL + ", " + password + ", " + type.equals("seller") + ", " + emergencyEmail + ", " + emergencyPhone + ", " + registrationToken + ");")
            .execute();
        InputStream registrationEmailStream = User.class.getClassLoader().getResourceAsStream("backend/email/registration.html");
        String registrationEmail = new String(registrationEmailStream.readAllBytes());
        EmailSender.sendEmail(email, "Confirm your email", registrationEmail
            .replace("{TOKEN}", registrationTokenUUID)
            .replace("{NAME}", name)
        );
    }

    public static User getUser(String email) throws SQLException, UserNotFoundException {
        email = "'" + email + "'";
        ResultSet resultSet = DatabaseUtil.getConnection()
            .prepareStatement("SELECT email FROM users WHERE email = " + email + ";")
            .executeQuery();
        if (!resultSet.next())
            throw new UserNotFoundException("User not found");
        return new User(resultSet.getString("email"));
    }

}