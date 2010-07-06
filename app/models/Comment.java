/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

/**
 *
 * @author ali
 */
@Entity(name="comment")
public class Comment extends Model{

    @ManyToOne
    public User user;

    @ManyToOne
    public Ayat ayat;

    @ManyToOne
    public Sourat sourat;
    
    @Lob
    public String content;

}
