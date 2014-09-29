/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package customextension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.Attribute.Type;
import org.wso2.siddhi.query.api.extension.annotation.SiddhiExtension;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

@SiddhiExtension(namespace = "geo", function = "geoproximity")
public class GeoProximity extends FunctionExecutor {

	HashMap<String, String> test = new HashMap<String, String>();

	Map<String, Geometry> GeometryList;

	public Attribute.Type getReturnType() {
		return Attribute.Type.STRING;
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(Type[] attributeTypes, SiddhiContext siddhiContext) {
		GeometryList = new HashMap<String, Geometry>();
	}

	@Override
	protected synchronized Object process(Object data) {

		ArrayList<String> idList = new ArrayList<String>();
		Object functionParams[] = (Object[]) data;

		double proximityDist = Double.parseDouble(functionParams[0].toString());
		double pointOnelat = Double.parseDouble(functionParams[1].toString());
		double pointOnelong = Double.parseDouble(functionParams[2].toString());
		String id1 = functionParams[3].toString(); // ID of the first Elem
		double time = Double.parseDouble(functionParams[4].toString());
		double giventime = Double.parseDouble(functionParams[5].toString());

		String pckttime = String.valueOf(time);
		String id2 = null; // ID of the second Elem
		double timediff;
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		// draw the buffer for each point
		Coordinate firstpoint = new Coordinate(pointOnelat, pointOnelong);
		Point pointOne = geometryFactory.createPoint(firstpoint);
		// to convert to degrees from coordinate units
		double bufferRadius = proximityDist / 110574.61087757687;
		Geometry buffer = pointOne.buffer(bufferRadius);
		// if that id already exist update that entry
		GeometryList.put(id1, buffer);

		// iterate through the list of all available vehicles
		for (Map.Entry<String, Geometry> entry : GeometryList.entrySet()) {
			id2 = entry.getKey().toString();
			// get the buffer for the current position of the vehicle
			Geometry myBuffer = (Geometry) entry.getValue();

			if (!id2.equalsIgnoreCase(id1)) { // if the buffer is of another
												// vehicle
				if (pointOne.within(myBuffer)) { // if the two vehicles are in
													// close proximity

					if (!test.containsKey(id1 + "," + id2)) {// check for how
																// long
						String tempArray = pckttime;
						test.put(id1 + "," + id2, tempArray);
						test.put(id2 + "," + id1, tempArray);

					}

					double timecheck = Double.parseDouble(test.get(id1 + ","
							+ id2));
					timediff = time - timecheck;
					// if the time difference for being in close proximity is
					// less than the user input time period,
					// output true else false
					if (timediff >= giventime) {
						idList.add(id2);
					}
				} else {
					if (test.containsKey(id1 + "," + id2)) {
						test.remove(id1 + "," + id2);
						test.remove(id2 + "," + id1);
					}
				}
			}
		}
		return generateOutput(idList);
	}

	/** generates the final output string **/
	public String generateOutput(ArrayList<String> idListFinal) {

		String finalOutput = "false"; // since we have to send in String format
										// cannot use Bool here
		String tempString = "null";
		int i = 0;

		if (idListFinal.isEmpty() == false) {

			finalOutput = "true";
			for (i = 0; i < idListFinal.size(); i++) {
				if (tempString.equalsIgnoreCase("null")) {
					tempString = idListFinal.get(i);
				} else {
					tempString = tempString + "," + idListFinal.get(i);
				}
			}
		}
		if (!tempString.equalsIgnoreCase("null")) {
			finalOutput = finalOutput + "," + tempString;
		}
		return finalOutput;
	}

}
