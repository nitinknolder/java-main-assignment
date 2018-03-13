package edu.knoldus;


import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.auth.AccessToken;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;



import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TwitterApp {
    private twitter4j.Twitter twitter = new TwitterFactory().getInstance();
    private Query latestTweetQuery;

    TwitterApp(String hashTag) {
        Properties properties = new Properties();
        try {
            InputStream input = new FileInputStream("/home/knoldus/IdeaProjects"
                    + "/assignmentjava802/src/main/resources/config.properties");
            properties.load(input);
            twitter.setOAuthConsumer(properties.getProperty("consumerKey"),
                    properties.getProperty("consumerSecretKey"));
            twitter.setOAuthAccessToken(new AccessToken(properties.getProperty("accessToken"),
                    properties.getProperty("accessTokenSecret")));
            latestTweetQuery = new Query(hashTag);
            latestTweetQuery.setCount(50);
            latestTweetQuery.resultType(Query.RECENT);

        } catch (Exception msg) {
            msg.printStackTrace();
        }
    }

    public static void sleepMethod() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.getMessage();
        }
    }

    public CompletableFuture<List<Status>> fetchLatestTweets() {
        return supplyAsync(() -> {
                    List<Status> latestTweets = new ArrayList<>();
                    try {
                        latestTweets = this.twitter.search(latestTweetQuery).getTweets();
                    } catch (TwitterException msg) {
                        System.out.println("Exception Occurred" + msg.getMessage());
                    }
                    return latestTweets;
                }
        );
    }

    public CompletableFuture<List<Status>> getTweetsOlderToNewer() {
        return supplyAsync(() -> {
            List<Status> olderToNewer = new ArrayList<>();
            try {

                QueryResult filterResultsOldToNew = this.twitter.search(latestTweetQuery);
                filterResultsOldToNew.getTweets().sort(Comparator.comparing(tweets -> tweets.getCreatedAt().getTime()));
                olderToNewer.addAll(filterResultsOldToNew.getTweets());

            } catch (TwitterException msg) {
                System.out.println("Exception Occurred" + msg.getMessage());
            }
            return olderToNewer;
        });

    }

    public CompletableFuture<List<Status>> filterHighToLowRetweets() {
        return CompletableFuture.supplyAsync(() -> {
            List<Status> sortedTweets = Collections.emptyList();
            try {
                sortedTweets = twitter.search(latestTweetQuery).getTweets().stream()
                        .sorted(Comparator.comparing(Status::getRetweetCount).reversed())
                        .collect(Collectors.toList());
            } catch (TwitterException msg) {
                System.out.println("Exception Occurred" + msg.getMessage());
            }

            return sortedTweets;
        });
    }

    public CompletableFuture<List<Status>> filterHighToLowLikes() {
        return CompletableFuture.supplyAsync(() -> {
            List<Status> filterLikes = Collections.emptyList();
            try {
                filterLikes = twitter.search(latestTweetQuery).getTweets().stream()
                        .sorted(Comparator.comparing(Status::getFavoriteCount).reversed())
                        .collect(Collectors.toList());
            } catch (TwitterException msg) {

                System.out.println("Exception Occurred" + msg.getMessage());
            }
            return filterLikes;
        });
    }

    public CompletableFuture<List<Status>> fetchTweetsOnGivenDate(LocalDate date) {
        latestTweetQuery.setSince(date.toString());
        return supplyAsync(() -> {
            List<Status> tweetsForParticularDate = new ArrayList<>();
            try {
                QueryResult queryResult = this.twitter.search(latestTweetQuery);
                tweetsForParticularDate.addAll(queryResult.getTweets());
                System.out.println("Number of Tweets On the Basis of Date are: "
                        + tweetsForParticularDate.size());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return tweetsForParticularDate;
        });
    }

}