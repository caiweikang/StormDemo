package com.vip.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class Topology {

	public static void main(String[] args) throws Exception {
		SentenceSpout sentenceSpout = new SentenceSpout();
		SplitSentenceBolt splitSentenceBolt = new SplitSentenceBolt();
		WordCountBolt wordCountBolt = new WordCountBolt();
		ReportBolt reportBolt = new ReportBolt();
		
		// 注册 spout 和 bolt
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("sentenceSpoutId", sentenceSpout);
		builder.setBolt("splitSentenceBoltId", splitSentenceBolt).shuffleGrouping("sentenceSpoutId"); // 随机分组流
		builder.setBolt("wordCountBoltId", wordCountBolt).fieldsGrouping("splitSentenceBoltId", new Fields("word")); // 按照字段分组流
		builder.setBolt("reportBoltId", reportBolt).globalGrouping("wordCountBoltId"); // 所有tuple汇总到同一个task
		
		// 生产拓扑
		StormTopology topology = builder.createTopology();
		
		Config conf = new Config();
		// 提交到本地模拟的集群中运算
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("topulogyName", conf, topology);
		
		// 真正提交到storm集群
	//	StormSubmitter.submitTopology("topulogyName", conf, topology);
		
		Thread.sleep(10*1000);
		cluster.killTopology("topulogyName");
		cluster.shutdown();
	}

}
