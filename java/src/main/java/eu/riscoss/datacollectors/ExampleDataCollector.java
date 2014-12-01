/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.riscoss.datacollectors;

import org.json.JSONObject;
import org.json.JSONArray;
import org.apache.commons.io.IOUtils;

public class ExampleDataCollector
{
    private static final String COLLECTOR_ID = "ExampleDataCollector";
    private static final String COLLECTOR_DATATYPE = "NUMBER";

    private static String getData(JSONObject input)
    {
        String exampleParameter = input.getString("exampleParameter");
        return ""+42;
    }

    private static JSONObject testInput()
    {
        JSONObject input = new JSONObject("");
        input.put("exampleParameter", "hi");
        input.put("riscoss_targetName", "test");
        return input;
    }

    public static void main(String[] args) throws Exception
    {
        JSONObject input;
        if (args.length > 0 && "--stdin-conf".equals(args[args.length-1])) {
            String stdin = IOUtils.toString(System.in, "UTF-8");
            input = new JSONObject(stdin);
        } else {
            input = testInput();
            System.out.println("using " + input + " as test configuration.");
            System.out.println("In production, use --stdin-conf and pass configuration to stdin");
        }

        JSONObject outObj = new JSONObject();
        outObj.put("id", COLLECTOR_ID);
        outObj.put("type", COLLECTOR_DATATYPE);
        outObj.put("target", input.getString("riscoss_targetName"));
        outObj.put("value", getData(input));

        JSONArray outArray = new JSONArray();
        outArray.put(outObj);
        System.out.println("-----BEGIN RISK DATA-----");
        System.out.println(outArray.toString());
        System.out.println("-----END RISK DATA-----");
    }
}
