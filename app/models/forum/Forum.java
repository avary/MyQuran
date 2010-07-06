/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models.forum;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import play.db.jpa.Model;

/**
 *
 * @author inf04
 */
@Entity
public class Forum extends Model implements Comparable<Forum>{

    public String name;
    public int nbTopic;
    public int nbPost;
    @OneToOne
    public Topic lastTopic;
    public int forumOrder;
    public boolean isVisible;
    public int state;

    public int compareTo(Forum o) {
        if(this.forumOrder < o.forumOrder){
            return -1;
        }
        
        if(this.forumOrder > o.forumOrder){
            return 1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Forum other = (Forum) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = (int) (79 * hash + this.id);
        return hash;
    }

}
