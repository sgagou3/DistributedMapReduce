package comp533.factories;

import comp533.mapper.ATokenMapper;
import comp533.mapper.TokenMapper;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapperFactory extends AMapReduceTracer {
	static TokenMapper mapper = new ATokenMapper();;

	public static void setMapper(final TokenMapper aNewMapper) {
		traceSingletonChange(AMapperFactory.class, aNewMapper);
		mapper = aNewMapper;
	}

	public static TokenMapper getMapper() {
		return mapper;
	}
}
