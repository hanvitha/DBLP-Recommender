package com.recommender.dblp.Repository;

/**
 * Created by admin on 22/10/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Component
public class DatabaseRepository {

    @Autowired
    private JdbcTemplate template;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String,Object>> getCoAuthors(String authorName){
        return template.queryForList("select a.name author, b.name coAuthor, count(*) noofpublications from author a inner join author b on a.paper_key = b.paper_key\n" +
                "inner join author_id ai on a.name=ai.author_name_id where ai.author_name= '" + authorName + "' and a.name <> b.name group by a.name, b.name order by noofpublications desc");

    }

    public List<Map<String,Object>> getConferenceList(String authorList){
        return template.queryForList("select count(*) noOfPublications, ci.conf_name confName from author a inner join paper p on a.paper_key=p.paper_key " +
                " inner join conference c on c.name=p.conference inner join conf_id ci on ci.conf_name_id=c.name " +
                "where a.name in (" + authorList + ") group by ci.conf_name order by noOfPublications desc");

    }

    public List<Map<String,Object>> getConferencePapers(){
        return template.queryForList("select title from conference c inner join paper p on c.name = p.conference");

    }

    public List<Map<String,Object>> getAuthorPapers(int id){
        return template.queryForList("select title from author a inner join paper p on a.paper_key = p.paper_key where a.name="+id);

    }

    public Map<String,Object> getAuthorId(String authorName){
        return template.queryForMap("select author_name_id id from author_id where author_name='"+authorName+"'");

    }

    public List<Map<String,Object>> getConferenceByAuthor(String authorName){
        return template.queryForList("select distinct(c.detail) conferenceName from author a inner join conference c on a.paper_key = c.name inner join author_id ai on a.name = ai.author_name_id where ai.author_name = '"+authorName+"'");

    }

    public List<Map<String,Object>> getAllConferences(){
        return template.queryForList("select distinct(conf_name) conferenceName from conf_id");
    }
    public List<Map<String,Object>> getAllPapersByConference(int id){
        return template.queryForList("select title from conference c inner join paper p on c.name = p.conference where name="+id);
    }

    public List<Map<String,Object>> getAllConferenceId(){
        return template.queryForList("select conf_name_id id, conf_name name from conf_id");
    }
}
