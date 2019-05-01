package servicepackage;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;


public class Client {
    static Scanner scan = new Scanner(System.in);
    private final static ClientConfig config = new DefaultClientConfig();
    private final static com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(config);
    private final static WebResource service = client.resource(
            UriBuilder.fromUri("http://localhost:19566/webServiceServerhall").build());

    public static void main(String[] args) throws IOException{
       

            
            while(true){
                System.out.println("Menyval: ");
                int menyVal = scan.nextInt();
                switch(menyVal){
                    case 1: getTempRapport();
                        break;
                    case 2: getElRapport();
                        break;
                    case 3: getTemperatur();
                        break;
                    case 4: getElectricity();
                        break;
                    case 5: getElectricityPrice();
                        break;
                    case 6: getInfoLatestHour();
                        break;
                    case 7: setNewElectricityPrice();
                        break;
                    case 8: setNewTemperatur();
                        break;
                    case 9: addTekniker();
                        break;
                    default: System.out.println("Felaktigt val");
                        break; 
                }
            }
    }
    
    public static void getTempRapport(){
        String id = "A";
        Temperatur maxMinAverageTemp = service.path("rest")
        .path("Service/maxMinAverageTemp/"+id).accept(MediaType.APPLICATION_XML).get(Temperatur.class);
        
        Temperatur[] tempArray = service.path("rest")
        .path("Service/temp/"+id).accept(MediaType.APPLICATION_XML).get(Temperatur[].class);
        
        for(Temperatur t : tempArray){
            System.out.println("Temp: " + t.getTemp() + "Datum: " +t.getDate());
        }
        System.out.println("Medel: " + maxMinAverageTemp.getAverageTemp() +"\n"+ "Högsta temperatur: " + maxMinAverageTemp.getMaxTemp() +"\n"+ "Lägsta temperatur: " + maxMinAverageTemp.getMinTemp());
    }
    
    
    
    public static void getElRapport(){
        String id = "A";
        
        Electricity maxMinAverageEl = service.path("rest")
        .path("Service/maxMinAverageEl/"+id).accept(MediaType.APPLICATION_XML).get(Electricity.class);
        
        Electricity elpris = service.path("rest")
        .path("Service/elpris/"+id).accept(MediaType.APPLICATION_XML).get(Electricity.class);
        
        Electricity[] tempArray = service.path("rest")
        .path("Service/el/"+id).accept(MediaType.APPLICATION_XML).get(Electricity[].class);
        
        Electricity billigastDyrastTimme = service.path("rest")
        .path("Service/billigastDyrastTimme/"+id).accept(MediaType.APPLICATION_XML).get(Electricity.class);
        
        for(Electricity e : tempArray){
            System.out.println("Datum: " +e.getDate() + "Elförbrukning: " + e.getEl()+"Elkostnad: "+e.getEl()*elpris.getElKostnad());
        }
        
        System.out.println("Medelförbrukning: " + maxMinAverageEl.getAverageEl() +"\n"+ "Högsta elförbrukning: " + maxMinAverageEl.getMaxEl() +"\n"+ "Lägsta elförbrukning: " + maxMinAverageEl.getMinEl() +"\n" +"Högsta kostnad: "+ elpris.getElKostnad());
        System.out.println("Dyrast timme: " + billigastDyrastTimme.getDyrastTid());
        System.out.println("Billigast timme: " + billigastDyrastTimme.getBilligastTid());
    }
    
    
    
    public static void getTemperatur(){
        String id = "B";
        Temperatur temp = service.path("rest")
        .path("Service/tempNow/"+id).accept(MediaType.APPLICATION_XML).get(Temperatur.class);
        System.out.println("Senaste temperatur: "+temp.getTemp() + "Datum: "+ temp.getDate());
    }
    
    public static void getElectricity(){
        String id = "A";
        Electricity el = service.path("rest")
        .path("Service/elNow/"+id).accept(MediaType.APPLICATION_XML).get(Electricity.class);
        System.out.println("Senaste förbrukning: "+el.getEl() + "Datum: "+ el.getDate());
    }
    
    public static void getElectricityPrice(){
        String id = "A";
        Electricity el = service.path("rest")
        .path("Service/elpris/"+id).accept(MediaType.APPLICATION_XML).get(Electricity.class);
        System.out.println("Senaste elpris: "+el.getElKostnad() + "Datum: "+ el.getDate());
    }
    
    public static void getInfoLatestHour(){
        getTemperatur();
        getElectricity();
        getElectricityPrice();
    }
    
    public static void setNewElectricityPrice(){
        String id = "A";
        System.out.println("Pris?");
        Electricity elpris = new Electricity(0.44f);
        ClientResponse el = service.path("rest")
                .path("Service/elpris/update/"+id).accept(MediaType.APPLICATION_XML).post(ClientResponse.class, elpris);
        System.out.println(el);
    }
    
    public static void setNewTemperatur(){
        String id = "A";
        Kylsystem k = new Kylsystem(21,1);
        ClientResponse temp = service.path("rest")
                .path("Service/temp/update/"+id).accept(MediaType.APPLICATION_XML).post(ClientResponse.class, k);
        System.out.println(temp);
    }
    
    
    public static void addTekniker(){
        System.out.println("Namn:");
        String namn = scan.next();
        Tekniker t = new Tekniker(namn);
        ClientResponse temp = service.path("rest")
                .path("Service/tekniker/add").accept(MediaType.APPLICATION_XML).post(ClientResponse.class, t);
        System.out.println(temp);
    }

}
