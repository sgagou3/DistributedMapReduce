package comp533.factories;

import comp533.mapper.ATokenMapper;
import comp533.mapper.TokenMapper;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapperFactory extends AMapReduceTracer {
	@SuppressWarnings("rawtypes")
	static TokenMapper mapper = new ATokenMapper();;

	@SuppressWarnings("rawtypes")
	public static void setMapper(final TokenMapper aNewMapper) {
		traceSingletonChange(AMapperFactory.class, aNewMapper);
		mapper = aNewMapper;
	}

	@SuppressWarnings("rawtypes")
	public static TokenMapper getMapper() {
		return mapper;
	}
}
