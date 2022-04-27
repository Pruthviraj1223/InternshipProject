package verticles;

import io.vertx.core.json.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class pingDiscovery {
    public boolean ping(String jsonString) throws IOException {

        JsonObject jsonObject = new JsonObject(jsonString);

        System.out.println("Ip = " + jsonObject.getString("ip"));

        ArrayList<String> commands = new ArrayList<>();

        commands.add("fping");

        commands.add("-q");

        commands.add("-c");

        commands.add("3");

        commands.add(jsonObject.getString("ip"));

        ProcessBuilder processBuilder = new ProcessBuilder(commands);

        processBuilder.redirectErrorStream(true); // It must be before the staring of process

        Process process;

        try {

            process = processBuilder.start();

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());

        var bufferedReader = new BufferedReader(inputStreamReader);

        String output;

        output = bufferedReader.readLine();

        System.out.println(output);

        String []arr = output.split(":")[1].split("=")[1].split(",")[0].split("/");

        String val = arr[2].substring(0,arr[2].length()-1);

        return val.equalsIgnoreCase("0");

    }

    public boolean ssh(String jsonString){
        return true;
    }

}
