package com.vip.storm;

import java.util.Map;
import java.util.stream.Collector;

import org.apache.jute.Index;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class SentenceSpout extends BaseRichSpout{
	
	private String[] sectences = {
			"there is fire",
			"starting in my heart",
			"reaching a fever pitch",
			"its bring me out of dark"
	};
	
	private SpoutOutputCollector collector;
	
	private int index = 0;
	
	/**
	 *  初始化时被调用，collector:发射数据用的组件
	 */
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	/**
	 * 调用此方法用于发射stream
	 */
	@Override
	public void nextTuple() {
		collector.emit(new Values(sectences[index]));
		index = index >= sectences.length-1 ? 0 : ++index;	
	}

	/**
	 * 声明发射的stream字段
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// the key of stream
		declarer.declare(new Fields("sentence"));
	}

}
