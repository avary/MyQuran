/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

/**
 *
 * @author ali
 */
@Entity(name="chapter")
public class Chapter extends Model{

    public String title;

    @ManyToOne
    public User user;

    @ManyToMany
    public List<Ayat> ayats;

    public Chapter(){
        this.title = "";
        this.ayats = new ArrayList<Ayat>();
    }

}
