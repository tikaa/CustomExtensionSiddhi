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

	// ArrayList <String, Geometry> GList = new ArrayList<String, Geometry>();
	// List<List<Geometry>> listOfLists = new ArrayList<List<Geometry>>();

	public Attribute.Type getReturnType() {
		return Attribute.Type.STRING;
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(Type[] attributeTypes, SiddhiContext siddhiContext) {
		// TODO Auto-generated method stub
		// synchronized (this) {
		GeometryList = new HashMap<String, Geometry>();
		// }

	}

	@Override
	protected synchronized Object process(Object data) {

		ArrayList<String> IDList = new ArrayList<String>();
		Object functionParams[] = (Object[]) data;

		double proximityDist = Double.parseDouble(functionParams[0].toString());
		double pointOnelat = Double.parseDouble(functionParams[1].toString());
		double pointOnelong = Double.parseDouble(functionParams[2].toString());
		String id1 = functionParams[3].toString();
		double time = Double.parseDouble(functionParams[4].toString());
		double giventime = Double.parseDouble(functionParams[5].toString());

		String pckttime = String.valueOf(time);
		String id2 = null;
		double timediff;
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		// draw the buffer for each point
		Coordinate firstpoint = new Coordinate(pointOnelat, pointOnelong);
		Point pointOne = geometryFactory.createPoint(firstpoint);

		double bufferRadius = proximityDist / 110574.61087757687;// to convert
		                                                         // to degrees
		Geometry buffer = pointOne.buffer(bufferRadius);

		// if that id already exist update that entry

		GeometryList.put(id1, buffer);
		// iterate
		// through the
		// list of all
		// available
		// vehicles

		for (Map.Entry<String, Geometry> entry : GeometryList.entrySet()) {
			id2 = entry.getKey().toString();

			Geometry myBuffer = (Geometry) entry.getValue(); // get the
			                                                 // buffer for
			                                                 // the current
			                                                 // position of
			                                                 // the vehicle

			if (!id2.equalsIgnoreCase(id1)) { // if the buffer is of
				                              // another vehicle
				if (pointOne.within(myBuffer)) { // if the two vehicles
					                             // are in close
					                             // proximity

					if (!test.containsKey(id1+","+id2)) {// check for how long //NOTE
												 // NEEDTO RESTRUCTURE!!!!!
						                         // they have been close
						String myarray1 = pckttime ;
						test.put(id1+","+id2, myarray1);
						test.put(id2+","+id1, myarray1);

					}

					double timecheck = Double.parseDouble(test.get(id1+","+id2));

					timediff = time - timecheck;

					if (timediff >= giventime){ // if the time difference for
					                           // being
					                           // in close proximity is less
					                           // than
					                           // the user input time period,
					                           // output
					                           // true else false

					

						IDList.add(id2);
					}
				} else {
					if (test.containsKey(id1+","+id2)) {
						test.remove(id1+","+id2);
						test.remove(id2+","+id1);
					}

				}
			}
		}

		// TODO Auto-generated method stub

		return generateOutput(IDList);
	}

	/** generates the final output string **/
	public String generateOutput(ArrayList<String> myList) {

		String finalOutput = "false";
		String myString = "null";
		int i = 0;

		if (myList.isEmpty() == false) {

			finalOutput = "true";
			for (i = 0; i < myList.size(); i++) {
				if (myString.equalsIgnoreCase("null")) {
					myString = myList.get(i);
				} else {
					myString = myString + "," + myList.get(i);
				}
			}

		}
		if (!myString.equalsIgnoreCase("null")) {
			finalOutput = finalOutput + "," + myString;
		}
		return finalOutput;
	}

}
