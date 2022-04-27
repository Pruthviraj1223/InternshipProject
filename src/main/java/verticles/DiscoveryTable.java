package verticles;

public class DiscoveryTable {

    private int port;
    private String name;
    private String password;
    private String ip;
    private String metricType;


     public void setName(String name) {
         this.name = name;
     }

     public  void setPassword(String password) {
         this.password = password;
     }

     public void setIp(String ip) {
         this.ip = ip;
     }

     public void setMetricType(String metricType){
         this.metricType = metricType;
     }

     public void setPort(int port){
          this.port = port;
     }

     public String getName(){
        return name;
     }

     public String getPassword(){
        return password;
     }

     public String getIp(){
        return ip;
     }

     public String getMetricType(){
        return metricType;
     }

     public int getPort(){
         return port;
     }

}
