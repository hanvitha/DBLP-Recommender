package com.recommender.dblp.util;

import com.recommender.dblp.Repository.DatabaseRepository;

import java.util.*;

/**
 * Created by pavan on 4/29/17.
 */
public class RecommenderUtil {

    private static ArrayList<String> distinctWords = new ArrayList<>();
    private static HashMap<String,Double> conferenceList = new HashMap<>();

    public static String getCoAuthors(String authorName, int k, DatabaseRepository databaseRepository) {
        String authorList = "";
        try {
            List<Map<String,Object>> data = databaseRepository.getCoAuthors(authorName);
            for(Map<String, Object> singleData : data) {
                if (k > 0) {
                    //coAuthorsList.add(rs.getInt("coAuthor"));
                    authorList += singleData.get("coAuthor") + ",";
                    k--;
                }
            }
            authorList = authorList.substring(0, authorList.lastIndexOf(","));
            if (authorList.length() == 0) {
                throw new Exception("Author not found in the database.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return authorList;
    }

    public static ArrayList<String> getConferences(int k, String authorList, DatabaseRepository databaseRepository) throws Exception{
        ArrayList<String> conferenceList = new ArrayList<>();
        try {
            List<Map<String,Object>> data = databaseRepository.getConferenceList(authorList);
            for(Map<String, Object> singleData : data) {
                if (k > 0) {
                    conferenceList.add((String)singleData.get("confName"));
                    k--;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return conferenceList;
    }

    private static void getDistance(String conferenceName){
        ArrayList<String> conferenceWords = new ArrayList<>();
        int commonCount = 0;
        int count = 0;
        for(String words : conferenceName.split(" ")){
            if(!conferenceWords.contains(words)){
                conferenceWords.add(words);
            }
        }

        for(String words : conferenceName.split("")){
            if(distinctWords.contains(words)){
                commonCount++;
            }
        }
        count = distinctWords.size() + conferenceWords.size();
        double distance = (1.0-((double)commonCount/(double)count));
        conferenceList.put(conferenceName, distance);
    }

    public static void getAuthorConferences(String authorName, DatabaseRepository databaseRepository){
        String conferenceNames = "";
        distinctWords.clear();
        conferenceList.clear();
        try {
            List<Map<String,Object>> data = databaseRepository.getConferenceByAuthor(authorName);
            for(Map<String, Object> singleData : data) {
                conferenceNames += singleData.get("conferenceName");
            }
            for(String words : conferenceNames.split("")){
                if(!distinctWords.contains(words)){
                    distinctWords.add(words);
                }
            }

            List<Map<String,Object>> data1 = databaseRepository.getAllConferences();
            for(Map<String, Object> singleData : data1) {
                getDistance((String)singleData.get("conferenceName"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void getDistanceByPaper(String conferenceName, List<Map<String, Object>> papers){
        ArrayList<String> conferenceWords = new ArrayList<>();
        int commonCount = 0;
        int count = 0;
        String paperTitle = "";
        for(Map<String, Object> paper : papers) {
            paperTitle = (String)paper.get("title");
            for (String words : paperTitle.split(" ")) {
                if (!conferenceWords.contains(words)) {
                    conferenceWords.add(words);
                }
            }
        }

        for(String words : conferenceWords){
            if(distinctWords.contains(words)){
                commonCount++;
            }
        }
        count = distinctWords.size() + conferenceWords.size();
        double distance = (1.0-((double)commonCount/(double)count));
        conferenceList.put(conferenceName, distance);
    }

    public static void getAuthorConferencesByPaper(String authorName, DatabaseRepository databaseRepository){
        distinctWords.clear();
        conferenceList.clear();
        String conferenceNames = "";
        try {
            Map<String, Object> id = databaseRepository.getAuthorId(authorName);
            List<Map<String,Object>> data = databaseRepository.getAuthorPapers((int)id.get("id"));
            for(Map<String, Object> singleData : data) {
                conferenceNames += singleData.get("title");
            }
            for(String words : conferenceNames.split("")){
                if(!distinctWords.contains(words)){
                    distinctWords.add(words);
                }
            }

            List<Map<String,Object>> data1 = databaseRepository.getAllConferenceId();
            for(Map<String, Object> singleData : data1) {
                List<Map<String,Object>> data2 = databaseRepository.getAllPapersByConference((int)singleData.get("id"));
                getDistanceByPaper((String)singleData.get("name"),data2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<String> printConferences(int k){
        Map<String, Double> map = sortByValues(conferenceList);
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        ArrayList<String> conferences = new ArrayList<>();
        while(iterator2.hasNext() && k > 0) {
            Map.Entry me = (Map.Entry)iterator2.next();
            conferences.add((String)me.getKey());
            k--;
        }
        return conferences;
    }

    private static HashMap sortByValues(HashMap map) {

        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}
