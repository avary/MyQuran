/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models.forum;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import models.User;
import play.db.jpa.Model;

/**
 *
 * @author inf04
 */
@Entity
public class Post extends Model{

    @ManyToOne
    public Topic topic;
    public String title;
    @ManyToOne
    public User author;
    @Temporal(TemporalType.TIMESTAMP)
    public Date createAt;
    @Temporal(TemporalType.TIMESTAMP)
    public Date updateAt;
    @Lob
    public String content;
    /**
     * state = 0 à valider
     * state = 1 OK
     * state = 2 supprimer
     */
    public int state;
    
}
