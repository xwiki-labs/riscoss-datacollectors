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
