package com.at.bigdata.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWatcher implements Watcher{
	private static final Logger logger = LoggerFactory.getLogger(SimpleWatcher.class);

	@Override
	public void process(WatchedEvent event) {
		KeeperState keeperState = event.getState();
		
		
		String evPath = event.getPath();
		
		EventType evType = event.getType();
		logger.info("Event Info, type: '{}', path: '{}', state: '{}';", 
				evType.name(), evPath, keeperState.name());
	}

}
