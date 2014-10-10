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

package src.main.java.org.wso2.siddhi.extension.withingeo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private static final String TRUE = "true";//
	private static final String FALSE = "false";//
	private HashMap<String, String> proximityDevices = new HashMap<String, String>();
	private Map<String, Geometry> GeometryList;

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

		List<String> idList = new ArrayList<String>();
		Object functionParams[] = (Object[]) data;
		double proximityDist = Double.parseDouble(functionParams[0].toString());
		double pointOneLat = Double.parseDouble(functionParams[1].toString());
		double pointOneLong = Double.parseDouble(functionParams[2].toString());
		String firstVehicleId = functionParams[3].toString(); // ID of the first vehicle
		double time = Double.parseDouble(functionParams[4].toString());
		double givenTime = Double.parseDouble(functionParams[5].toString());
		String pcktTime = String.valueOf(time);
		String secondVehicleId = null; // ID of the second vehicle in proximity
		double timeDiff = 0.0;
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		// draw the buffer for each point
		Coordinate firstPoint = new Coordinate(pointOneLat, pointOneLong);
		Point pointOne = geometryFactory.createPoint(firstPoint);
		// to convert to degrees from coordinate units
		double bufferRadius = proximityDist / 110574.61087757687;
		Geometry buffer = pointOne.buffer(bufferRadius);
		// if that id already exist update that entry
		GeometryList.put(firstVehicleId, buffer);
		// iterate through the list of all available vehicles
		for (Map.Entry<String, Geometry> entry : GeometryList.entrySet()) {
			secondVehicleId = entry.getKey().toString();
			// get the buffer for the current position of the vehicle
			String hashMapKey = firstVehicleId + "," + secondVehicleId;
			String hasMapKeyReverse = secondVehicleId + "," + firstVehicleId;
			Geometry currBuffer = (Geometry) entry.getValue();
			if (!secondVehicleId.equalsIgnoreCase(firstVehicleId)) {
				// if the buffer is of another vehicle
				if (pointOne.within(currBuffer)) {
					// if the two vehicles are in close proximity
					if (!proximityDevices.containsKey(hashMapKey)) {
						// check for how long
						String tempArray = pcktTime;
						proximityDevices.put(hashMapKey, tempArray);
						proximityDevices.put(hasMapKeyReverse, tempArray);
					}
					double timecheck = Double.parseDouble(proximityDevices.get(hashMapKey));
					timeDiff = time - timecheck;
					// if the time difference for being in close proximity is
					// less than the user input time period,
					// output true else false
					if (timeDiff >= givenTime) {
						idList.add(secondVehicleId);
					}
				} else {
					if (proximityDevices.containsKey(hashMapKey)) {
						proximityDevices.remove(hashMapKey);
						proximityDevices.remove(hasMapKeyReverse);
					}
				}
			}
		}
		return generateOutput(idList);
	}

	/** generates the final output string **/
	public String generateOutput(List<String> idListFinal) {
		String finalOutput = FALSE;
		// since we have to send in String format cannot use bool here
		String tempString = null; // string null is checked at output check CEP
		if (!idListFinal.isEmpty()) {
			finalOutput = TRUE;
			for (int i = 0; i < idListFinal.size(); i++) {
				if (tempString != null) {
					tempString = tempString + "," + idListFinal.get(i);					
				} else {
					tempString = idListFinal.get(i);
				}
			}
			if (tempString != null) {
				finalOutput = finalOutput + "," + tempString;
			}	
		}		
		return finalOutput;
	}

}
