package edu.sjsu.cmpe.cache.client;

import java.util.*;

public class Client {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        String value[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
       
        List<CacheServiceInterface> CS = new ArrayList<CacheServiceInterface>();
        CS.add(new DistributedCacheService("http://localhost:3000"));
        CS.add(new DistributedCacheService("http://localhost:3001"));
        CS.add(new DistributedCacheService("http://localhost:3002"));
        ConsistentHash<CacheServiceInterface> chash = new ConsistentHash<CacheServiceInterface>(CS);
        DistributedCacheService dcs = null;
        
       for(int putkey=0; putkey < 10; putkey++)	{
        	dcs = (DistributedCacheService)chash.get(value[putkey]);
        	dcs.put((putkey+1), value[putkey]);
          	System.out.println("The key value pair " + (putkey+1) + "-" + value[putkey]+ " is assigned to server " + dcs.getCacheServerUrl());
        }
        
        for(int getkey=0; getkey < 10; getkey++)	{
        	dcs = (DistributedCacheService)chash.get(value[getkey]);
        	String value1 = dcs.get(getkey+1);
           	System.out.println("Value - " + value1 + " fetched from server : " + dcs.getCacheServerUrl());
        }
        
        System.out.println("Existing Cache Client");
    }

}
