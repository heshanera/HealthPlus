package Doctor;

import java.util.GregorianCalendar;
/**
 *
 * @author heshan
 */
public class Appointment {

    GregorianCalendar startTime;
    GregorianCalendar endTime;
    
    public Appointment(GregorianCalendar start, GregorianCalendar end) {
        setStartTime(start);
        setEndTime(end);
    }
    
    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public void setStartTime(GregorianCalendar startTime) {
        this.startTime = startTime;
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public void setEndTime(GregorianCalendar endTime) {
        this.endTime = endTime;
    }

}
