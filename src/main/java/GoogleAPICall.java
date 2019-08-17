import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class GoogleAPICall {

    public static String call(String queryPara, Boolean isEducation) throws UnsupportedEncodingException {

        String query = URLEncoder.encode(queryPara, "UTF-8");
        String API_KEY ="AIzaSyDwIqovTnVH7ANajZHioo9UOPfcjKZj5zQ";
       // String API_KEY ="AIzaSyDjN1X4Pft7uVZUb0veneXbY3D05PFTdnU";

        String url = "https://kgsearch.googleapis.com/v1/entities:search?query="+query+"&key="+API_KEY+"&limit=1&indent=True&types=Corporation&types=Organization";

        if(isEducation){
            url="https://kgsearch.googleapis.com/v1/entities:search?query="+query+"&key="+ API_KEY+"&limit=1&indent=True&types=CollegeOrUniversity&types=EducationalOrganization";
        }

        Map mapresp1 = requestMethod(url);

        Map<String, String> newmap = new HashMap<String, String>();
        String abc = "";
        String bcd = "";
        String def ="";
        if (mapresp1 != null) {
            for (Object mapKey : mapresp1.keySet()) {
                Object reulstsObj = mapresp1.get(mapKey);

                if (reulstsObj instanceof List) {
                    List<Object> resultsObjList = (List<Object>) reulstsObj;
                    if (resultsObjList != null && ((List) reulstsObj).size() > 0) {
                        for (Object eachResultInfo : resultsObjList) {
                            abc = eachResultInfo.toString();

                        }
                    }

                }
            }

        }
        if (abc != null) {
            if(abc.length() > 2){
                bcd = abc.split(",")[2];
                if(bcd != null) {
                    def = bcd.split("=")[1];
                }

            }
        }
        if (def.contains("&amp;")){
            def = def.replace("&amp;","&");
        }
        return def;
    }

    public static Map requestMethod(String urlIs)
    {
        Map mapResp=new HashMap();

        //            query = URLEncoder.encode(query, "UTF-8");
//            String urlIs="https://kgsearch.googleapis.com/v1/entities:search?query="+query+"&key="+ API_KEY+"&limit=1&indent=True&types=CollegeOrUniversity&types=EducationalOrganization";
        try {

            HttpClient httpClient = HttpClientBuilder.create().build();
            org.apache.http.client.methods.HttpGet request = new org.apache.http.client.methods.HttpGet(urlIs);
            request.addHeader("Accept", "application/json");
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonString = EntityUtils.toString(entity, "UTF-8");
                ObjectReader reader = new ObjectMapper().reader(Map.class);
                mapResp = reader.readValue(jsonString);


            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mapResp;
    }

}
