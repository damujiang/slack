package com.mujiang.slack.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mujiang.slack.contentextractor.ContentExtractor;
import com.mujiang.slack.model.Article;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

/**
 * Created by TONY on 2/27/16.
 */
@Service
public class SlackService {
    public String publishBlog(String articleUrl) throws Exception {
        Article article = ContentExtractor.getArticleByUrl(articleUrl);

        //String str="{\"status\":\"publish\",\"author\":\"mujiang\",\"title\":\""+URLEncoder.encode(article.getTitle())+"\",\"content\":\""+URLEncoder.encode(article.getContent())+"\"}";

        //return str;
         // str="{\"status\":\"publish\",\"author\":\"mujiang\",\"title\":\"titittttd test\",\"content\":\"content test\"}";
        JSONObject js = new JSONObject();
        js.accumulate("status","publish").accumulate("author", "mujiang")
                .accumulate("title", article.getTitle())
                .accumulate("content", article.getContentHtml());
//       HttpResponse<JsonNode> jsonResponse = Unirest.post("http://requestb.in/17gdhpu1")
        //return js.toString();
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://blog.chuhan.me/wp-json/wp/v2/posts")

                .header("Authorization", "Basic bXVqaWFuZzpoYWZ1ZGF4dWU=")
               // .basicAuth("mujiang", "hafudaxue")
                .header("Content-Type", "application/json")
                .body(js.toString())
                .asJson();

        String responseStr = jsonResponse.getBody().toString();


        return js.toString();
    }

}
