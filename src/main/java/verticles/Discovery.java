package verticles;

import io.vertx.core.json.JsonObject;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Base64;

public class Discovery {
    public boolean ping(JsonObject jsonObject) throws IOException {

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

        String []arr = output.split(":")[1].split("=")[1].split(",")[0].split("/");

        String loss = arr[2].substring(0,arr[2].length()-1);

        return loss.equalsIgnoreCase("0");

    }

    public String plugin(JsonObject user){

        JsonObject jsonObject = new JsonObject(user.toString());

        jsonObject.put("category","discovery");

        String encoded = Base64.getEncoder().encodeToString(jsonObject.toString().getBytes());

        ProcessBuilder processBuilder = new ProcessBuilder().command("/home/pruthviraj/InternshipProject/plugin.exe",encoded);

        String output = "";

        try {

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream()); //read the output

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            output = bufferedReader.readLine();

            process.waitFor();

            bufferedReader.close();

            process.destroy();

        } catch (IOException | InterruptedException e) {

            e.printStackTrace();

        }

        return output;
    }

}
