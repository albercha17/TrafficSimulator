package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewCityRoadEvent;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.NewJunctionEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder {

    public NewInterCityRoadEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
        super("new_inter_city_road",lssFactory,dqsFactory);

    }
	@Override
	Event crearI(JSONObject data, Weather w) {
		NewInterCityRoadEvent e= new NewInterCityRoadEvent(data.getInt("time"),data.getString("id"), data.getString("src"),data.getString("dest"),
	            data.getInt("length"), data.getInt("co2limit"), data.getInt("maxspeed"), w);
	    return e;
	}

}
