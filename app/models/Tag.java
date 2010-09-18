/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import play.db.jpa.Model;

@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = "name"))
public class Tag extends Model implements Comparable<Tag> {

    public String name;

    public Tag(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int compareTo(Tag otherTag) {
        return name.compareTo(otherTag.name);
    }

    public static Tag findOrCreateByName(String name) {
        Tag tag = Tag.find("byName", name).first();
        if (tag == null) {
            tag = new Tag(name);
        }
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tag other = (Tag) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    public static List<Map> getCloud() {
        List<Map> result = Tag.find(
                "select new map(t.name as tag, count(a.id) as ayats) from models.Ayat a join a.tags as t group by t.name").fetch();
        return result;
    }
}
