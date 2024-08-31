package comp533.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comp533.reduce.AMapEntry;
import comp533.reduce.MapEntry;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AFaceBookFriendMapper extends AMapReduceTracer implements TokenMapper<String, List<String>> {
	static final String SEPARATOR = "_";

	private void mappingHelper(final String[] aCharacterFriendArray, final String aFirstUser,
			final List<MapEntry<String, List<String>>> aFriendEntryList) {
		final int aFriendArraySize = aCharacterFriendArray.length;
		String aCombinedUserString = null;

		for (int anotherCounter = 1; anotherCounter < aFriendArraySize; anotherCounter++) {
			final String aSecondUser = aCharacterFriendArray[anotherCounter];

			if (aFirstUser.compareTo(aSecondUser) < 0) {
				aCombinedUserString = aFirstUser + SEPARATOR + aSecondUser;
			} else {
				aCombinedUserString = aSecondUser + SEPARATOR + aFirstUser;
			}

			final MapEntry<String, List<String>> aMapEntry = new AMapEntry<String, List<String>>(aCombinedUserString,
					List.of(Arrays.copyOfRange(aCharacterFriendArray, 1, aFriendArraySize)));
			aFriendEntryList.add(aMapEntry);
		}
	}

	@Override
	public List<MapEntry<String, List<String>>> map(final List<String> aKeyList) {
		final List<MapEntry<String, List<String>>> aFriendEntryList = new ArrayList<MapEntry<String, List<String>>>();
		final int aKeyListSize = aKeyList.size();
		for (int aCounter = 0; aCounter < aKeyListSize; aCounter++) {
			final String aFriendString = aKeyList.get(aCounter);
			final String[] aCharacterFriendArray = aFriendString.split(",");

			final String aFirstUser = aCharacterFriendArray[0];
			mappingHelper(aCharacterFriendArray, aFirstUser, aFriendEntryList);
		}
		traceMap(aKeyList, aFriendEntryList);
		return aFriendEntryList;
	}
}
