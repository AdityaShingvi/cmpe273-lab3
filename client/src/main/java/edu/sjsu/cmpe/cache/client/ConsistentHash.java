package edu.sjsu.cmpe.cache.client;

import java.security.*;
import java.util.*;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash<CacheServiceInterface> {

  //private final HashFunction hashFunction;
  //private final int numberOfReplicas;
  private final SortedMap<String, CacheServiceInterface> circle = new TreeMap<String, CacheServiceInterface>();

  public ConsistentHash(List<CacheServiceInterface> nodes) {

    /*this.hashFunction = hashFunction;
    this.numberOfReplicas = numberOfReplicas;*/

    for (CacheServiceInterface node : nodes) {
      add(node);
    }
  }

  public void add(CacheServiceInterface node) {
    circle.put(hash(node.toString()),node);
   }

  public void remove(CacheServiceInterface node) {
    circle.remove(hash(node.toString()));
   }

  public CacheServiceInterface get(Object key) {
    if (circle.isEmpty()) {
      return null;
    }
    String hash = hash(key);
    if (!circle.containsKey(hash)) {
      SortedMap<String, CacheServiceInterface> tailMap = circle.tailMap(hash);
      hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
    }
    return circle.get(hash);
  }
  
  public String hash(Object key){
	  StringBuffer hexString = null;
	  try{
	  MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(((String)key).getBytes());
      byte byteData[] = md.digest();
            
      hexString = new StringBuffer();
  		for (int i=0;i<byteData.length;i++) {
  		String hex=Integer.toHexString(0xff & byteData[i]);
 	     	if(hex.length()==1) hexString.append('0');
 	     	hexString.append(hex);
 	     	
  		}
	  }catch(Exception e){}
	  return hexString.toString();
  }
}