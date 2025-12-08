/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aquilaFunc;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 *
 * @author brawl
 */
public class Func_app {
    
    public static String data_apiowner(String placa) throws MalformedURLException, IOException{
        StringBuilder builds = new StringBuilder();
        URL uri = new URL(String.format("http://127.0.0.1:9091/consulta/info/placa=%s", placa));
        HttpURLConnection httpConnect = (HttpURLConnection) uri.openConnection();
        httpConnect.setRequestMethod("GET");
        httpConnect.connect();
        int codeResp = httpConnect.getResponseCode();
        if (codeResp == HttpURLConnection.HTTP_OK){
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(uri.openStream()));
            String inputLine;
            while((inputLine = buffReader.readLine()) != null){
                builds.append(inputLine);
                
            }
            buffReader.close();
        }else{
            System.out.println("");
        }
        
        return builds.toString();
    }
    
}
