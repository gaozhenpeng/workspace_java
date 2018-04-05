package com.at.bigdata.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleWatcher implements Watcher{

	@Override
	public void process(WatchedEvent event) {
		KeeperState keeperState = event.getState();
		
		
		String evPath = event.getPath();
		
		EventType evType = event.getType();
		log.info("Event Info, type: '{}', path: '{}', state: '{}';", 
				evType.name(), evPath, keeperState.name());
	}

}
