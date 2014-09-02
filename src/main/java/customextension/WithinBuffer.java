/*package customextension;

import java.util.ArrayList;
import customextension.StationaryLocation;

import org.apache.log4j.Logger;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.extension.annotation.SiddhiExtension;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

@SiddhiExtension(namespace = "geo", function = "withinbuffer")
public class WithinBuffer extends FunctionExecutor {

	public Geometry[] mybufferList;
	public JsonArray jLocCoordinatesArray;
	public JsonArray jmLocCoordinatesArray;
	Logger log = Logger.getLogger(WithinBuffer.class);
	Attribute.Type returnType;
	int j = 0;

	/*
	 * public AttributeType getReturnType() {
	 * return AttributeType.STRING;
	 * // TODO Auto-generated method stub
	 * // return null;
	 * }
	 */
	/*public Attribute.Type getReturnType() {
		return Attribute.Type.STRING;
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}
	//the input
	//{    "data": {        "type": "Point",        "coordinates": [            -105.01621,            39.57422        ]    }}
	@Override
	public void init(Attribute.Type[] types, SiddhiContext siddhiContext) {
		ArrayList<Object> paramList = new ArrayList<Object>();
		for (int i = 0, size = attributeExpressionExecutors.size(); i < size; i++) {
			paramList.add(attributeExpressionExecutors.get(i).execute(null));
		}
		Double myRadius = 4.0;
		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		//[{'features':[{ 'type': 'Feature', 'properties':{},'geometry:{'type': 'Point', 'coordinates': [  79.94248329162588,6.844997820293952] }}]}, null, null]
		String mystring = paramList.get(0).toString();
		//Double myRadius = Double.parseDouble(paramList.get(3).toString());// we can send the radius as well if we want to !!
		
		//mystring = {'features':[{ 'type': 'Feature', 'properties':{},'geometry':{'type': 'Point', 'coordinates': [  79.94248329162588,6.844997820293952] }},{ 'type': 'Feature', 'properties':{},'geometry':{'type': 'Point', 'coordinates': [  79.94248329162588,6.844997820293952] }}]}
		JsonElement mcoordinateArray = new JsonParser().parse(mystring); //mcoordinateArray = {"features":[{"type":"Feature","properties":{},"geometry":{"type":"Point","coordinates":[79.94248329162588,6.844997820293952]}}]}
		
		JsonObject jmObject = mcoordinateArray.getAsJsonObject();//{"features":[{"type":"Feature","properties":{},"geometry":{"type":"Point","coordinates":[79.94248329162588,6.844997820293952]}},{"type":"Feature","properties":{},"geometry":{"type":"Point","coordinates":[79.94248329162588,6.844997820293952]}}]}
		
		
		jmLocCoordinatesArray = jmObject.getAsJsonArray("features"); //[{"type":"Feature","properties":{},"geometry":{"type":"Point","coordinates":[79.94248329162588,6.844997820293952]}}]
		
		mybufferList = new Geometry[jmLocCoordinatesArray.size()];
		
		for (int i = 0; i < jmLocCoordinatesArray.size(); i++) {//[{"type":"Feature","properties":{},"geometry":{"type":"Point","coordinates":[79.94248329162588,6.844997820293952]}},{"type":"Feature","properties":{},"geometry":{"type":"Point","coordinates":[79.94248329162588,6.844997820293952]}}]
			
		//getting the geometry feature
		
		JsonObject jObject = (JsonObject) jmLocCoordinatesArray.get(i);
		
		JsonObject geometryObject= jObject.getAsJsonObject("geometry");
		
		
			//String myPoint = type.toString();
			
				//{    "type": "Point",    "coordinates": [        -105.01621,        39.57422    ]}
			
				JsonArray coordArray = geometryObject.getAsJsonArray("coordinates");
				
				double lattitude = Double.parseDouble(coordArray.get(0).toString());
				double longitude = Double.parseDouble(coordArray.get(1).toString());
				//inserting for passing to UI
				//jLocCoordinatesArray.add(coordArray);
				
				Coordinate coord = new Coordinate(lattitude, longitude);
				Point point = geometryFactory.createPoint(coord);

				Geometry buffer = point.buffer(myRadius);
				mybufferList[i] = buffer;
			}
		
	}
	
	


	@Override
	protected Object process(Object obj) {

		//int i = 0;
		int x = 0;
		Object functionParams[] = (Object[]) obj;
		String pointOne = (String) functionParams[0];
		String pointTwo = (String) functionParams[1];
		// String pointtocheck = (String)functionParams[1];
		String pointFour = (String) functionParams[3];
		String pointThree = (String) functionParams[2];
		
		Boolean withinid = false;
		JsonElement withinLocation = null;

		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		
		double pointlattitude = Double.parseDouble(pointFour);
		double pointlongitude = Double.parseDouble(pointThree);
		Coordinate checkpoint = new Coordinate(pointlattitude, pointlongitude);
		Point cpoint = geometryFactory.createPoint(checkpoint);

		for (x = 0; x < mybufferList.length-1; x++) {

			  Geometry myBuffer = mybufferList[x];
			if (cpoint.within(myBuffer)) {
				//boolean myId = myBuffer.within(cpoint);
				withinid = true;
				withinLocation = jmLocCoordinatesArray.get(x);			
				
			}
			withinLocation = jmLocCoordinatesArray.get(x);
		}
		//return StationaryLocation().toString;
		StationaryLocation stationaryLocation = new StationaryLocation(withinid,withinLocation);
		
		String finalOutput = stationaryLocation.toString() ;
		System.out.println(finalOutput);
		
		return finalOutput;
		
		
	//	return withinid.toString() + "," + withinLocation;
		// TODO Auto-generated method stub
		// return null;
	}

	

}*/