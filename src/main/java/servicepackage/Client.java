package servicepackage;


import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.IOException;
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
                String id = "";
                System.out.println("\n\n                 *** MENY ***\n");
                System.out.println("[1] Temprapport                 [6] Senaste timmen");
                System.out.println("[2] Elrapport                   [7] Ange nytt elpris");
                System.out.println("[3] Senaste temperatur          [8] Ange ny temperatur i kylsystemet");
                System.out.println("[4] Senaste elförbrukning       [9] Lägg till tekniker");
                System.out.println("[5] Aktuellt Elpris             [10] Högst dagsförbrukning");
                System.out.println("\n\nMenyval: ");
                
                
                int menyVal = scan.nextInt();
                
                if(menyVal >= 1 && menyVal <= 9){
                    System.out.println("Ange serverhall: (A, B, C)");
                    id = scan.next();
                }

                switch(menyVal){
                    case 1: getTempRapport(id);
                        break;
                    case 2: getElRapport(id);
                        break;
                    case 3: getTemperatur(id);
                        break;
                    case 4: getElectricity(id);
                        break;
                    case 5: getElectricityPrice(id);
                        break;
                    case 6: getInfoLatestHour(id);
                        break;
                    case 7: setNewElectricityPrice(id);
                        break;
                    case 8: setNewTemperatur(id);
                        break;
                    case 9: addTekniker();
                        break;
                    case 10: getHighestConsumption();
                        break;
                    default: System.out.println("Felaktigt val");
                        break; 
                }
            }
    }
    
    public static void getTempRapport(String serverhall){
        String id = serverhall;
        System.out.println("Vilken månad?");
        int month = scan.nextInt();
        System.out.println("Vilken dag?");
        int day = scan.nextInt();
        Temperatur maxMinAverageTemp = service.path("rest")
        .path("Service/maxMinAverageTemp/"+id+"/month/"+month+"/day/"+day).accept(MediaType.APPLICATION_XML).get(Temperatur.class);
        
        Temperatur[] tempArray = service.path("rest")
        .path("Service/temp/"+id+"/month/"+month+"/day/"+day).accept(MediaType.APPLICATION_XML).get(Temperatur[].class);
        
        for(Temperatur t : tempArray){

            System.out.println("Temp: " + t.getTemp() + " Datum: "+t.getOurTimeZone(t.getDate()));
        }
        System.out.println("Medel: " + maxMinAverageTemp.getAverageTemp() +"\n"+ "Högsta temperatur: " + maxMinAverageTemp.getMaxTemp() +"\n"+ "Lägsta temperatur: " + maxMinAverageTemp.getMinTemp());
    }
    

    public static void getElRapport(String serverhall){
        String id = serverhall;
        System.out.println("Vilken månad?");
        int month = scan.nextInt();
        System.out.println("Vilken dag?");
        int day = scan.nextInt();
        Electricity maxMinAverageEl = service.path("rest")
        .path("Service/maxMinAverageEl/"+id+"/month/"+month+"/day/"+day).accept(MediaType.APPLICATION_XML).get(Electricity.class);
        
        Electricity elpris = service.path("rest")
        .path("Service/elpris/"+id).accept(MediaType.APPLICATION_XML).get(Electricity.class);
        
        Electricity[] tempArray = service.path("rest")
        .path("Service/el/"+id+"/month/"+month+"/day/"+day).accept(MediaType.APPLICATION_XML).get(Electricity[].class);
        
        Electricity billigastDyrastTimme = service.path("rest")
        .path("Service/billigastDyrastTimme/"+id).accept(MediaType.APPLICATION_XML).get(Electricity.class);
        
        for(Electricity e : tempArray){
            System.out.println("Datum: " +e.getOurTimeZone(e.getDate()) + "Elförbrukning: " + e.getEl()+"Elkostnad: "+e.getEl()*elpris.getElKostnad());
        }
        
        System.out.println("Medelförbrukning: " + maxMinAverageEl.getAverageEl() +"\n"+ "Högsta elförbrukning: " + maxMinAverageEl.getMaxEl() +"\n"+ "Lägsta elförbrukning: " + maxMinAverageEl.getMinEl() +"\n" +"Högsta kostnad: "+ elpris.getElKostnad());
        System.out.println("Dyrast timme: " + billigastDyrastTimme.getDyrastTid());
        System.out.println("Billigast timme: " + billigastDyrastTimme.getBilligastTid());
    }
    

    public static void getTemperatur(String serverhall){
        String id = serverhall;
        Temperatur temp = service.path("rest")
        .path("Service/tempNow/"+id).accept(MediaType.APPLICATION_XML).get(Temperatur.class);
        
        Kylsystem k = service.path("rest")
        .path("Service/kylsysteminfo/"+id).accept(MediaType.APPLICATION_XML).get(Kylsystem.class);
        
        System.out.println("Senaste temperatur: "+temp.getTemp() + " Datum: "+ temp.getOurTimeZone(temp.getDate()));
        System.out.println("Givare är satt till: " + k.getTemp());
        System.out.println("Av: "+ k.getTeknikerNamn() + " Datum: "+k.getOurTimeZone(k.getDate()));
        
    }
    
    public static void getElectricity(String serverhall){
        String id = serverhall;
        Electricity el = service.path("rest")
        .path("Service/elNow/"+id).accept(MediaType.APPLICATION_XML).get(Electricity.class);
        System.out.println("Senaste förbrukning: "+el.getEl() + "Datum: "+ el.getOurTimeZone(el.getDate()));
    }
    
    public static void getElectricityPrice(String serverhall){
        String id = serverhall;
        Electricity el = service.path("rest")
        .path("Service/elpris/"+id).accept(MediaType.APPLICATION_XML).get(Electricity.class);
        System.out.println("Senaste elpris: "+el.getElKostnad() + "Datum: "+ el.getOurTimeZone(el.getDate()));
    }
    
    public static void getInfoLatestHour(String serverhall){
        getTemperatur(serverhall);
        getElectricity(serverhall);
        getElectricityPrice(serverhall);
    }
    
    public static void setNewElectricityPrice(String serverhall){
        String id = serverhall;
        System.out.println("Pris?");
        float d = scan.nextFloat();
        Electricity elpris = new Electricity(d);
        Response res = service.path("rest")
                .path("Service/elpris/update/"+id).accept(MediaType.APPLICATION_XML).post(Response.class, elpris);
        System.out.println(res.getMessage());
    }
    
    public static void setNewTemperatur(String serverhall){
        String id = serverhall;
        System.out.println("Ange tekniker ID: ");
        int teknikerId = scan.nextInt();
        System.out.println("Ange ny temperatur till systemet: ");
        int nyTemp = scan.nextInt();
        Kylsystem k = new Kylsystem(nyTemp, teknikerId);
        Response temp = service.path("rest")
                .path("Service/temp/update/"+id).accept(MediaType.APPLICATION_XML).post(Response.class, k);
        System.out.println(temp.getMessage());
    }
    

    public static void addTekniker(){
        System.out.println("Namn:");
        String namn = scan.next();
        Tekniker t = new Tekniker(namn);
        Response temp = service.path("rest")
                .path("Service/tekniker/add").accept(MediaType.APPLICATION_XML).post(Response.class, t);
        System.out.println(temp.getMessage());
    }
    
    public static void getHighestConsumption(){
        System.out.println("Vilken månad?");
        int month = scan.nextInt();
        System.out.println("Vilken dag?");
        int day = scan.nextInt();
        Electricity el = service.path("rest")
        .path("Service/highestconsumption"+month+"/day/"+day).accept(MediaType.APPLICATION_XML).get(Electricity.class);
        System.out.println("Serverhall med högst energiförbrukning: " + el.getServerhallsNamn() + " - " + el.getEl()+"kw");
    }
}