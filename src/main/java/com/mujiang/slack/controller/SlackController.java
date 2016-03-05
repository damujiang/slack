package com.mujiang.slack.controller;

import com.mujiang.slack.service.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TONY on 2/27/16.
 */
@RestController
@EnableAutoConfiguration
public class SlackController {

    @Autowired
    SlackService slackService;

    @RequestMapping("/slack")
    public String slack(String text){
        //https://api.slack.com/outgoing-webhooks

        try {
            return slackService.publishBlog(text);
        }catch(Exception ex){
            return ex.getLocalizedMessage();
        }
       // return "{\"text\":\"received:"+text +"\"}";
    }
}
