/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models.forum;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import models.Proposal;
import models.User;
import play.db.jpa.Model;

/**
 *
 * @author inf04
 */
@Entity
public class Topic extends Model{

    @ManyToOne
    public Forum forum;
    public String name;
    @ManyToOne
    public User author;
    @Temporal(TemporalType.TIMESTAMP)
    public Date createAt;
    @Temporal(TemporalType.TIMESTAMP)
    public Date updateAt;
    public int nbResponse;
    public int nbDisplay;
    @OneToOne
    public Post lastPost;
    @ManyToOne
    public Proposal proposal;
    
    public boolean finished;

}
