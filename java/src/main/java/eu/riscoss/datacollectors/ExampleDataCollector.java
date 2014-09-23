/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package eu.riscoss.datacollectors;

import org.json.JSONObject;
import org.json.JSONArray;
import org.apache.commons.io.IOUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import java.io.InputStreamReader;
import java.io.BufferedReader;


public class ExampleDataCollector
{
    private static final String COLLECTOR_ID = "ExampleDataCollector";
    private static final String COLLECTOR_DATATYPE = "NUMBER";

    private static int getData(String exampleParameter)
    {
        return 42;
    }

    private static String mkPost(int value, JSONObject input)
    {
        JSONObject post = new JSONObject();
        post.put("id", COLLECTOR_ID);
        post.put("type", COLLECTOR_DATATYPE);
        post.put("target", input.getString("riscoss_targetName"));
        post.put("value", value);

        JSONArray out = new JSONArray();
        out.put(post);
        return out.toString();
    }

    private static void uploadToRDR(int value, JSONObject input) throws Exception
    {
        HttpClient client = HttpClientBuilder.create().build();
        String host = input.getString("riscoss_rdrHost");
        int port = input.getInt("riscoss_rdrPort");
        String path = input.getString("riscoss_rdrPath");

        HttpPost request = new HttpPost("http://" + host + ":" + port + "" + path);
        request.setEntity(new StringEntity(mkPost(value, input)));

        HttpResponse response = client.execute(request);

        System.out.println("Response Code : " 
                      + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
	        new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
	          System.err.println(line);
        }
    }

    public static void main(String[] args) throws Exception
    {
        String stdin = IOUtils.toString(System.in, "UTF-8");
        JSONObject input = new JSONObject(stdin);

        String exampleParameter = input.getString("exampleParameter");

        int value = getData(exampleParameter);

        uploadToRDR(value, input);
    }
}
