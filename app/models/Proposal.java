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

    @Lob
    public String content;
}
