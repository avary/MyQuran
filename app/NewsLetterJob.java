
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.Proposal;
import models.User;
import notifiers.Notifier;
import play.jobs.Job;
import play.jobs.On;
import play.jobs.OnApplicationStart;
import play.mvc.Router;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ali
 */
@On("0 0 23 * * ?")
//@OnApplicationStart
public class NewsLetterJob extends Job{

    @Override
    public void doJob() throws Exception {
        System.out.println("Send emails...");
        List<Proposal> proposals = Proposal.find("state = ? and proposalFinish = ? "
                + "order by type", 1,new Date()).fetch();

        if(proposals == null || proposals.isEmpty()){
            System.out.println("No updates");
            return;
        }

        List<User> users = User.find("enabled = true and banned = false and "
                + "newsletter = true").fetch();

        if(users == null || users.isEmpty()){
            System.out.println("No users subscripted");
            return;
        }

        List<Proposal> translations = new ArrayList<Proposal>();
        List<Proposal> comments = new ArrayList<Proposal>();
        List<Proposal> tags = new ArrayList<Proposal>();
        for (Proposal p : proposals) {
            if(p.type == 0){
                translations.add(p);
            }else if(p.type == 1){
                comments.add(p);
            }else{
                tags.add(p);
            }
        }

        for (User user : users) {
            Notifier.sendNewsLetter(user,translations,comments,tags);
        }
        
        System.out.println("Finish emails");
    }

}
