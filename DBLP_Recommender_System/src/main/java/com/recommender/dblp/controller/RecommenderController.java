package com.recommender.dblp.controller;

import com.recommender.dblp.Repository.DatabaseRepository;
import com.recommender.dblp.util.RecommenderUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by pavan on 3/17/17.
 */
@CrossOrigin
@RestController
@RequestMapping("/recommend")
public class RecommenderController {

    private final static Logger logger = Logger.getLogger(RecommenderController.class);

    @Autowired
    private DatabaseRepository databaseRepository;


    @RequestMapping(value = "/conferenceByAuthorName", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity conferenceByAuthorName(HttpServletRequest request, @RequestParam(name = "authorName") String authorName) throws Exception{
        String authorsList = RecommenderUtil.getCoAuthors(authorName, 3, databaseRepository);
        return ResponseEntity.ok(RecommenderUtil.getConferences(3, authorsList,databaseRepository));
    }

    @RequestMapping(value = "/conferenceByConferenceName", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity conferenceByConferenceName(HttpServletRequest request, @RequestParam(name = "authorName") String authorName) throws Exception{
        RecommenderUtil.getAuthorConferences(authorName, databaseRepository);
        return ResponseEntity.ok(RecommenderUtil.printConferences(3));
    }

    @RequestMapping(value = "/conferenceByPublication", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity conferenceByPublication(HttpServletRequest request, @RequestParam(name = "authorName") String authorName) throws Exception{
        RecommenderUtil.getAuthorConferencesByPaper(authorName, databaseRepository);
        return ResponseEntity.ok(RecommenderUtil.printConferences(3));
    }
}
