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
		
		// ע�� spout �� bolt
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("sentenceSpoutId", sentenceSpout);
		builder.setBolt("splitSentenceBoltId", splitSentenceBolt).shuffleGrouping("sentenceSpoutId"); // ���������
		builder.setBolt("wordCountBoltId", wordCountBolt).fieldsGrouping("splitSentenceBoltId", new Fields("word")); // �����ֶη�����
		builder.setBolt("reportBoltId", reportBolt).globalGrouping("wordCountBoltId"); // ����tuple���ܵ�ͬһ��task
		
		// ��������
		StormTopology topology = builder.createTopology();
		
		Config conf = new Config();
		// �ύ������ģ��ļ�Ⱥ������
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("topulogyName", conf, topology);
		
		// �����ύ��storm��Ⱥ
	//	StormSubmitter.submitTopology("topulogyName", conf, topology);
		
		Thread.sleep(10*1000);
		cluster.killTopology("topulogyName");
		cluster.shutdown();
	}

}
