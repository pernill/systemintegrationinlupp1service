package servicepackage;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "serverhall") 
public class Serverhall implements Serializable{
    private static final long serialVersionUID = 1L;
    private String serverhallsNamn;
    
    public Serverhall(){
        
    }
    
    public Serverhall(String serverhallsNamn){
        this.serverhallsNamn = serverhallsNamn;
    }
    
    @XmlElement 
    public void setServerhallsNamn(String serverhallsNamn){
        this.serverhallsNamn = serverhallsNamn;
    }
    
    public String getServerhallsNamn(){
        return serverhallsNamn;
    }
}
