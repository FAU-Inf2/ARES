package de.fau.cs.inf2.cas.common.vcs.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("unused")
public class JsonVcsData {

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInDateCache {
    
    
    @JsonProperty("repositoryList")
    public ArrayList<DateCacheEntry> repositoryList;
    

    MixInDateCache(@JsonProperty("repositoryList")
        ArrayList<DateCacheEntry> repositoryList) {}
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInDateCacheEntry {
    
    @JsonProperty("repository")
    public String repository;
    
    @JsonProperty("map")
    public HashMap<String, HashMap<String,Date>> map;
    

    MixInDateCacheEntry(@JsonProperty("repository") 
        String repository, @JsonProperty("map")
        HashMap<String,Date> map) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInFileCache {
    
    
    @JsonProperty("repositoryList")
    public ArrayList<FileCacheMap> repositoryList;
    

    MixInFileCache(@JsonProperty("repositoryList")
        ArrayList<FileCacheMap> repositoryList) {}
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInFileCacheMap {
    
    @JsonProperty("repositoryKey")
    public String repositoryKey;
    
    @JsonProperty("fileList")
    public ArrayList<FileCacheEntry> fileList;
    

    MixInFileCacheMap(@JsonProperty("repositoryKey") 
        String repositoryKey, @JsonProperty("fileList")
        ArrayList<FileCacheEntry> fileList) {}
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInFileCacheEntry {
    
    @JsonProperty("filename")
    public String filename;
    
    @JsonProperty("list")
    public ArrayList<FileCachePair> list;
    

    MixInFileCacheEntry(@JsonProperty("filename") 
        String filename, @JsonProperty("list")
        ArrayList<FileCachePair> list) {}
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInFileCachePair {
    
    @JsonProperty("first")
    public String first;
    
    @JsonProperty("second")
    public String second;
    
    MixInFileCachePair(@JsonProperty("first") 
        String first, @JsonProperty("second")
        String second) {}
  }
  

  private static void addMixIns(ObjectMapper mapper, Class<?> targetClass, Class<?> mixInClass) {
    mapper.addMixIn(targetClass, mixInClass);
  }
  
  /**
   * Creates the json mapper.
   *
   * @return the object mapper
   */
  public static ObjectMapper createJsonMapper() {
    final ObjectMapper mapper = new ObjectMapper();

    {
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper
          .setVisibilityChecker(mapper.getVisibilityChecker().with(JsonAutoDetect.Visibility.NONE));
      mapper.setSerializationInclusion(Include.NON_NULL);
      addMixIns(mapper, DateCache.class, MixInDateCache.class);
      addMixIns(mapper, DateCacheEntry.class, MixInDateCacheEntry.class);
      addMixIns(mapper, FileCache.class, MixInFileCache.class);
      addMixIns(mapper, FileCacheMap.class, MixInFileCacheMap.class);
      addMixIns(mapper, FileCacheEntry.class, MixInFileCacheEntry.class);
      addMixIns(mapper, FileCachePair.class, MixInFileCachePair.class);

    }

    return mapper;
  }
  
}
