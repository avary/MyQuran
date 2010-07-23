/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    // 2 = chapitre
    public int type;

    // 0 = normal
    // 1 = approuvé
    // 2 = rejeté
    public int state;

    @ManyToOne
    public Chapter chapter;

    @Lob
    public String content;
}
