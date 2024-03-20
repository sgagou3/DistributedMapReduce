package comp533.reduce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

@SuppressWarnings("serial")
public class AFaceBookFriendReducer extends AMapReduceTracer implements TokenReducer<String, List<String>> {

	@Override
	public Map<String, List<String>> reduce(final List<MapEntry<String, List<String>>> aMapEntryList) {
		final Map<String, List<String>> aReducedResult = new HashMap<String, List<String>>();
		for (MapEntry<String, List<String>> aMapEntry : aMapEntryList) {
			if (!aReducedResult.containsKey(aMapEntry.getKey())) {
				aReducedResult.put(aMapEntry.getKey(), aMapEntry.getValue());
			} else {
				final List<String> aNewFriendList = aMapEntry.getValue();
				final List<String> anExistingFriendList = aReducedResult.get(aMapEntry.getKey());

				final Set<String> aCurrentSet = new HashSet<String>(anExistingFriendList);
				final Set<String> aNewSet = new HashSet<String>(aNewFriendList);

				aNewSet.addAll(aMapEntry.getValue());

				aCurrentSet.retainAll(aNewSet);

				if (aCurrentSet.size() == 0) {
					aReducedResult.remove(aMapEntry.getKey());
				} else {
					final List<String> aNewerFriendList = new ArrayList<String>(aCurrentSet);
					Collections.sort(aNewerFriendList);
					aReducedResult.replace(aMapEntry.getKey(), aNewerFriendList);
				}
			}
		}
		traceReduce(aMapEntryList, aReducedResult);
		return aReducedResult;
	}

}
