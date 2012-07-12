import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.storytotell.ejb.CourseManager;
import org.storytotell.tpet.entity.Course;

/**
 *
 * @author fusion
 */
@Named("courseUI")
@RequestScoped
public class CourseUI {
  
  @EJB private CourseManager courseManager;
  
  public Course getCurrent() {
    return courseManager.findById(1);
  }
}
