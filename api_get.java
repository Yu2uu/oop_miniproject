import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//org.json-chargebee-1.0.jar
import org.json.JSONObject;
// import org.json.JSONArray; for arrays
import org.json.JSONException;

public class api_get {
    private static String base_url = "https://api.nomics.com/v1/";
    private static String api_key = "f0de9f36e0316eea5b78c51093602a00b9a86976";

    public static JSONObject send_request(String params) throws IOException, InterruptedException, JSONException{
        // create a client
        var client = HttpClient.newHttpClient();

        // create a request
        var request = HttpRequest.newBuilder(URI.create( base_url + "currencies/ticker?key=" + api_key + "&" + params))
        .header("accept", "application/json")
        .GET()
        .build();
        
        // use the client to send the request
        var data = client.send(request, HttpResponse.BodyHandlers.ofString());

        // a delay so that too many requests arent sent to the server, only allowed 1 request per sec
        try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        // the parse the response:
        JSONObject json_object = new JSONObject(data.body().substring(1, data.body().length() - 1)); // converting returned json array into object
        return json_object;
    }

    // public static JSONObject parse_response(String jsonString) throws JSONException{
    //     JSONObject object = new JSONObject(jsonString);
    //     // String pageName = obj.getString("id");
    //     return object;
    // }
}