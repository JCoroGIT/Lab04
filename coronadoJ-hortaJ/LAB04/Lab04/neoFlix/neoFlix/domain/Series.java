package domain;  
 
import java.util.ArrayList;

public class Series extends Content{
   
    private int year;
    private ArrayList<Episode> episodes;
    

    public Series(String title, int year){
        super(title);
        this.year=year;
        episodes= new ArrayList<Episode>();
    }


  
    public void addEpisode(Episode e){
        episodes.add(e);
    }
       
    
   /**
   * Returns the calculated rating
   * @return
   * @throws NeoFlixException, CONTENT_EMPTY if the series has no episodes
   *                         , VALUE_UNKNOWN if the rating for any episode is unknown
   *                         , DATA_ERROR if the rating for any episode cannot be calculated due to a data error
   */
   @Override
   public int rating() throws NeoFlixException{
       int r=0;
       int i=0;
       if(episodes.size()<=0){
           throw new NeoFlixException(NeoFlixException.CONTENT_EMPTY);
       }
       else{
           for(Episode e: episodes){
               r = r + e.rating();
               i+=1;
            }
           r = r/i;
           return r;
       }

   }
    
 
   /**
   * Returns the calculated rating, using the default value for episodes with an unknown rating and ignoring those with errors.
   * @return
   * @throws NeoFlixException, UNKNOWN_VALUE if the rating cannot be calculated.
   */

   public int rating(int default_) throws NeoFlixException {
    if (episodes.isEmpty()) {
        throw new NeoFlixException(NeoFlixException.CONTENT_EMPTY);
    }
    int sum = 0;
    int count = 0;
    for (Episode e : episodes) {
        try {
            sum += e.rating();
            count++;
        } catch (NeoFlixException ex) {
            if (NeoFlixException.VALUE_UNKNOWN.equals(ex.getMessage())) {
                sum += default_;
                count++;
            }
        }
    }
    if (count == 0) {
        throw new NeoFlixException(NeoFlixException.VALUE_UNKNOWN);
    }
    return sum / count;
}
 
   //If an episode has no rating, use the average of the previous episodes or of all episodes, depending on the value of the previous parameter.
   //Throw CONTENT_EMPTY and VALUE_UNKNOWN if either of these cases occurs.
   public int rating(boolean previous) throws NeoFlixException {
    if (episodes.isEmpty()) {
        throw new NeoFlixException(NeoFlixException.CONTENT_EMPTY);
    }
    int sum = 0;
    if (previous) {
        int count = 0;
        for (Episode e : episodes) {
            try {
                sum += e.rating();
            } catch (NeoFlixException ex) {
                if (!NeoFlixException.VALUE_UNKNOWN.equals(ex.getMessage()) || count == 0) {
                    throw ex;
                }
                sum += sum / count;
            }
            count++;
        }
        return sum / count;
    }
    int known = 0;
    for (Episode e : episodes) {
        try {
            sum += e.rating();
            known++;
        } catch (NeoFlixException ex) {
            if (!NeoFlixException.VALUE_UNKNOWN.equals(ex.getMessage())) {
                throw ex;
            }
        }
    }
    if (known == 0) {
        throw new NeoFlixException(NeoFlixException.VALUE_UNKNOWN);
    }
    int average = sum / known;
    return (sum + average * (episodes.size() - known)) / episodes.size();
}

    @Override
    public int popularity() throws NeoFlixException {
        if (episodes.isEmpty()) {
            throw new NeoFlixException(NeoFlixException.CONTENT_EMPTY);
        }
        int sum = 0;
        int known = 0;
        for (Episode e : episodes) {
            try {
                sum += e.popularity();
                known++;
            } catch (NeoFlixException ex) {
                if (!NeoFlixException.VALUE_UNKNOWN.equals(ex.getMessage())) {
                    throw ex;
                }
            }
        }
        if (known == 0) {
            throw new NeoFlixException(NeoFlixException.VALUE_UNKNOWN);
        }
        return sum / known;
}
    
    
    @Override
    public String data(boolean withIndicators) throws NeoFlixException{
        StringBuffer answer=new StringBuffer();
        answer.append(title+": "+year+ ( withIndicators ? " ( " +popularity()+" - "+ rating()+" )":""));
        for(Episode e: episodes) {
            answer.append("\n\t"+(e.data(withIndicators)));
        }
        return answer.toString();
    } 
    

}
