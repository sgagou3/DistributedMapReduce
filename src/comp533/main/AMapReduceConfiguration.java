package comp533.main;

import comp533.barrier.AMapReduceBarrier;
import comp533.base.ATokenCounter;
import comp533.client.AClientMain;
import comp533.client.AClientTokenCounter;
import comp533.client.AFaceBookClientMain;
import comp533.client.ClientTokenCounter;
import comp533.facebook.AFaceBookMapReduceMain;
import comp533.facebook.FaceBookFriendFinder;
import comp533.factories.AMapperFactory;
import comp533.factories.APartitionerFactory;
import comp533.factories.AReducerFactory;
import comp533.joiner.AMapReduceJoiner;
import comp533.mapper.ATokenMapper;
import comp533.mapper.AnIntegerSummingMapper;
import comp533.mvc.controller.AMapReduceController;
import comp533.mvc.model.AMapReduceModel;
import comp533.mvc.model.MapReduceModel;
import comp533.mvc.view.AMapReduceView;
import comp533.partition.AMapReducePartitioner;
import comp533.reduce.AMapEntry;
import comp533.reduce.ATokenReducer;
import comp533.server.AMapReduceIntegerSummerMain;
import comp533.server.AMapReduceTokenCounterMain;
import comp533.slave.AMapReduceSlave;
import comp533.summation.AnIntegerSummer;
import gradingTools.comp533s21.assignment1.interfaces.MapReduceConfiguration;

public class AMapReduceConfiguration implements MapReduceConfiguration {

	@Override
	public Object getBarrier(final int aNumThreads) {
		return new AMapReduceBarrier(aNumThreads);
	}

	@Override
	public Class<?> getBarrierClass() {
		return AMapReduceBarrier.class;
	}

	@Override
	public Class<?> getClientTokenCounter() {
		return AClientMain.class;
	}

	@Override
	public Class<?> getControllerClass() {
		return AMapReduceController.class;
	}

	@Override
	public Object getIntSummingMapper() {
		return new AnIntegerSummingMapper();
	}

	@Override
	public Class<?> getIntSummingMapperClass() {
		return AnIntegerSummingMapper.class;
	}

	@Override
	public Object getJoiner(final int aNumThreads) {
		return new AMapReduceJoiner(aNumThreads);
	}

	@Override
	public Class<?> getJoinerClass() {
		return AMapReduceJoiner.class;
	}

	@Override
	public Class<?> getKeyValueClass() {
		return AMapEntry.class;
	}

	@Override
	public Class<?> getMapperFactory() {
		return AMapperFactory.class;
	}

	@Override
	public Class<?> getModelClass() {
		return AMapReduceModel.class;
	}

	@Override
	public Object getPartitioner() {
		return APartitionerFactory.getPartitioner();
	}

	@Override
	public Class<?> getPartitionerClass() {
		return AMapReducePartitioner.class;
	}

	@Override
	public Class<?> getPartitionerFactory() {
		return APartitionerFactory.class;
	}

	@Override
	public Object getReducer() {
		return AReducerFactory.getReducer();
	}

	@Override
	public Class<?> getReducerClass() {
		return ATokenReducer.class;
	}

	@Override
	public Class<?> getReducerFactory() {
		return AReducerFactory.class;
	}

	@Override
	public Class<?> getRemoteClientFacebookMapReduce() {
		return AFaceBookClientMain.class;
	}

	@Override
	public Class<?> getRemoteClientObjectClass() {
		return AClientTokenCounter.class;
	}

	@Override
	public Class<?> getRemoteClientObjectInterface() {
		return ClientTokenCounter.class;
	}

	@Override
	public Class<?> getRemoteModelInterface() {
		return MapReduceModel.class;
	}

	@Override
	public Class<?> getServerFacebookMapReduce() {
		return AFaceBookMapReduceMain.class;
	}

	@Override
	public Class<?> getServerIntegerSummer() {
		return AMapReduceIntegerSummerMain.class;
	}

	@Override
	public Class<?> getServerTokenCounter() {
		return AMapReduceTokenCounterMain.class;
	}

	@Override
	public Class<?> getSlaveClass() {
		return AMapReduceSlave.class;
	}

	@Override
	public Class<?> getStandAloneFacebookMapReduce() {
		return FaceBookFriendFinder.class;
	}

	@Override
	public Class<?> getStandAloneIntegerSummer() {
		return AnIntegerSummer.class;
	}

	@Override
	public Class<?> getStandAloneTokenCounter() {
		return ATokenCounter.class;
	}

	@Override
	public Object getTokenCountingMapper() {
		return AMapperFactory.getMapper();
	}

	@Override
	public Class<?> getTokenCountingMapperClass() {
		return ATokenMapper.class;
	}

	@Override
	public Class<?> getViewClass() {
		return AMapReduceView.class;
	}
}
