package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveAllStrategy;
import simulator.model.MoveFirstStrategy;

public class MoveAllStrategyBuilder extends Builder<DequeuingStrategy>{

	public MoveAllStrategyBuilder() {
		super("most_all_dqs");
	}

	@Override
	protected DequeuingStrategy createTheInstance(JSONObject data) {
		MoveAllStrategy m= new MoveAllStrategy();
		return m;
	}
}