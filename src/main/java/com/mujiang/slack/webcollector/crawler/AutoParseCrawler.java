/*
 * Copyright (C) 2014 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.mujiang.slack.webcollector.crawler;


import com.mujiang.slack.webcollector.fetcher.Executor;
import com.mujiang.slack.webcollector.fetcher.Visitor;
import com.mujiang.slack.webcollector.model.CrawlDatum;
import com.mujiang.slack.webcollector.model.CrawlDatums;
import com.mujiang.slack.webcollector.model.Links;
import com.mujiang.slack.webcollector.model.Page;
import com.mujiang.slack.webcollector.net.HttpRequest;
import com.mujiang.slack.webcollector.net.HttpResponse;
import com.mujiang.slack.webcollector.net.Requester;
import com.mujiang.slack.webcollector.util.RegexRule;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hu
 */
public abstract class AutoParseCrawler extends Crawler implements Executor,Visitor,Requester{

    public static final Logger LOG = LoggerFactory.getLogger(AutoParseCrawler.class);

    /**
     * 是否自动抽取符合正则的链接并加入后续任务
     */
    protected boolean autoParse = true;

    protected Visitor visitor;
    protected Requester requester;

    public AutoParseCrawler(boolean autoParse) {
        this.autoParse = autoParse;
        this.visitor = this;
        this.requester = this;
        this.executor=this;
    }

    @Override
    public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
        HttpRequest request=new HttpRequest(crawlDatum);
        return request.getResponse();
    }

    /**
     * URL正则约束
     */
    protected RegexRule regexRule = new RegexRule();


    @Override
    public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {
        HttpResponse response = requester.getResponse(datum);
        Page page = new Page(datum, response);
        visitor.visit(page, next);
        if (autoParse && !regexRule.isEmpty()) {
            parseLink(page, next);
        }
    }


    protected void parseLink(Page page, CrawlDatums next) {
        String conteType = page.getResponse().getContentType();
        if (conteType != null && conteType.contains("text/html")) {
            Document doc = page.getDoc();
            if (doc != null) {
                Links links = new Links().addByRegex(doc, regexRule);
                next.add(links);
            }
        }

    }

    /**
     * 添加URL正则约束
     *
     * @param urlRegex
     */
    public void addRegex(String urlRegex) {
        regexRule.addRule(urlRegex);
    }

    /**
     *
     * @return 返回是否自动抽取符合正则的链接并加入后续任务
     */
    public boolean isAutoParse() {
        return autoParse;
    }

    /**
     * 设置是否自动抽取符合正则的链接并加入后续任务
     *
     * @param autoParse
     */
    public void setAutoParse(boolean autoParse) {
        this.autoParse = autoParse;
    }

    /**
     *
     * @return
     */
    public RegexRule getRegexRule() {
        return regexRule;
    }

    /**
     *
     * @param regexRule
     */
    public void setRegexRule(RegexRule regexRule) {
        this.regexRule = regexRule;
    }


    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Requester getRequester() {
        return requester;
    }

    public void setRequester(Requester requester) {
        this.requester = requester;
    }
}
