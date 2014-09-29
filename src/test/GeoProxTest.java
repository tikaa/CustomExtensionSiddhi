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
import java.util.List;

import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.config.SiddhiConfiguration;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;

public class GeoProxTest {
	public static void main(String[] args) throws Exception {

		// from anurudhdha
		SiddhiConfiguration conf = new SiddhiConfiguration();
		List<Class> classList = new ArrayList<Class>();
		classList.add(GeoProximity.class);
		conf.setSiddhiExtensions(classList);

		SiddhiManager siddhiManager = new SiddhiManager();
		siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

		siddhiManager
				.defineStream("define stream cseEventStream ( id int , time double, longitude double,lat double) ");
		// format of the data being sent
		// {'geometries':[{ 'type': 'Point', 'coordinates': [100.5, 0.5] },{
		// 'type': 'Point', 'coordinates': [100.5, 0.5] }]}
		// {"geometries":[{"type":"Point","coordinates":[
		// 79.94248329162588,6.844997820293952]},{"type":"Point","coordinates":[100.0,0.0]}]}

		String queryReference = siddhiManager
				.addQuery("from cseEventStream "
						+ "select id, time, geo:geoproximity(1,lat,longitude,id,time,1 ) as tt "
						+ "insert into StockQuote;");

		siddhiManager.addCallback(queryReference, new QueryCallback() {
			@Override
			public void receive(long timeStamp, Event[] inEvents,
					Event[] removeEvents) {
				EventPrinter.print(timeStamp, inEvents, removeEvents);

			}
		});

		// id,time,longitude,lat,speed, false as speedFlag sending numeric values to test each scenario, 
		InputHandler inputHandler = siddhiManager
				.getInputHandler("cseEventStream");
		
		inputHandler.send(new Object[] { 1, 234.345, 100.786, 6.9876 }); //stationary
		inputHandler.send(new Object[] { 2, 244.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 3, 244.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 1, 254.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 2, 254.345, 5, 100 });
		inputHandler.send(new Object[] { 3, 254.345, 5, 100 });
		inputHandler.send(new Object[] { 1, 264.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 4, 244.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 5, 254.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 4, 254.345, 5, 100 });
		inputHandler.send(new Object[] { 4, 254.345, 5, 100 });
		inputHandler.send(new Object[] { 1, 264.345, 100.786, 6.9876 }); //stationary
		inputHandler.send(new Object[] { 2, 274.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 1, 284.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 5, 264.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 5, 274.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 5, 284.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 2, 284.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 5, 294.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 5, 234.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 2, 244.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 3, 244.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 1, 254.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 5, 254.345, 5, 100 });
		inputHandler.send(new Object[] { 3, 254.345, 5, 100 });
		inputHandler.send(new Object[] { 1, 264.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 2, 274.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 1, 284.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 3, 264.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 3, 274.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 3, 284.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 2, 284.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 1, 294.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 1, 234.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 2, 244.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 3, 244.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 1, 254.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 2, 254.345, 5, 100 });
		inputHandler.send(new Object[] { 3, 254.345, 5, 100 });
		inputHandler.send(new Object[] { 1, 264.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 2, 274.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 1, 284.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 3, 264.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 3, 274.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 3, 284.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 2, 284.345, 100.786, 6.9876 });
		inputHandler.send(new Object[] { 1, 294.345, 100.786, 6.9876 });
		
		Thread.sleep(100);		
		siddhiManager.shutdown();
	}

}
