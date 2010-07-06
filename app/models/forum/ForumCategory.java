/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models.forum;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

/**
 *
 * @author inf04
 */
@Entity
public class ForumCategory extends Model{

    public String name;
    public int categoryOrder;

    @ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    public List<Forum> forums;

    public void setForums(List<Forum> forums){
        Collections.sort(forums);
        this.forums = forums;
    }
}
