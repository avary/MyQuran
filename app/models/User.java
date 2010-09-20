/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 *
 * @author ali
 */
@Entity(name = "user")
public class User extends Model implements Comparable {

    public String username;
    public String email;
    public String password;
    public String confirm;
    public boolean enabled;
    public boolean banned;
    public boolean notification;
    public boolean isAdmin;
    @Temporal(TemporalType.TIMESTAMP) public Date lastVisit;

    public boolean newsletter;
    public boolean newPost;

    public User(String username, String email, String password, String confirm) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.confirm = confirm;
        this.notification = false;
    }

    public static User connect(String username, String password) {
        User user = find("byUsername", username).first();

        byte[] uniqueKey = password.getBytes();
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder hashString = new StringBuilder();

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            } else {
                hashString.append(hex.substring(hex.length() - 2));
            }
        }

        if (user != null && user.password.equals(hashString.toString())) {
            return user;
        }

        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if ((this.username == null) ? (other.username != null) : !this.username.equals(other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.username != null ? this.username.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.username;
    }

    public int compareTo(Object o) {
        User otherUser = (User) o;
        return this.username.compareTo(otherUser.username);
    }
}
