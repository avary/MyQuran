/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.db.jpa.Model;

/**
 *
 * @author inf04
 */
@Entity
public class Proposal extends Model{

    @ManyToOne
    public User user;

    @ManyToOne
    public Ayat ayat;

    // 0 = traduction
    // 1 = commentaires
    // 2 = tags
    public int type;

    // 0 = normal
    // 1 = approuvé
    // 2 = rejeté
    public int state;

    @Lob
    public String content;
    @Temporal(TemporalType.DATE) public Date proposalFinish;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Proposal other = (Proposal) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (int) (13 * hash + this.id);
        return hash;
    }


}
