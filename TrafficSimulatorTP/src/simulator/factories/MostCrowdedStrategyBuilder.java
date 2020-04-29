package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;
import simulator.model.RoundRobinStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected MostCrowdedStrategy createTheInstance(JSONObject data) {
		MostCrowdedStrategy r=new MostCrowdedStrategy(data.getInt("timeslot"));
		return r;
	}

}
