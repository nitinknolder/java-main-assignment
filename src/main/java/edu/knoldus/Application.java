package edu.knoldus;

import twitter4j.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;


public class Application {
    public static void main(String[] args) {
        TwitterApp twitterObj = new TwitterApp("Knoldus");
        System.out.println("1: Get Latest Tweets \n2: Get Tweets Older to Newer \n"
                + "3: Get Retweets HighToLower \n4: Get Number Of Likes Higher To Lower\n"
                + "5: Fetch Tweets On the basis of given Date");
        Scanner scannerObject = new Scanner(System.in);
        System.out.println("Enter Your choice: ");
        Integer userChoice = scannerObject.nextInt();
        switch (userChoice) {

            case 1:
                CompletableFuture<List<Status>> latestTweets = twitterObj.fetchLatestTweets();
                System.out.println("<---- Latest Tweets ---->");
                latestTweets.thenAccept(recentTweets -> recentTweets.forEach(status -> System.out.println(
                        status.getCreatedAt() + " " + status.getText())));
                TwitterApp.sleepMethod();
                break;
            case 2:
                CompletableFuture<List<Status>> sortedTweets = twitterObj.getTweetsOlderToNewer();
                System.out.println("<---- GetTweets from Older To Newer ---->");
                sortedTweets.thenAccept(sortedTweet -> sortedTweet.forEach(status -> System.out.println(
                        status.getCreatedAt() + " " + status.getText())));
                TwitterApp.sleepMethod();
                break;
            case 3:
                CompletableFuture<List<Status>> filterTweets = twitterObj.filterHighToLowRetweets();
                System.out.println("<---- Get ReTweets From High To Low Are ---->");
                filterTweets.thenAccept(sortedTweet -> sortedTweet.forEach(status -> System.out.println(
                        status.getCreatedAt() + " " + status.getText())));
                TwitterApp.sleepMethod();
                break;
            case 4:
                CompletableFuture<List<Status>> filterTweetsOnLikes = twitterObj.filterHighToLowLikes();
                System.out.println("<---- Get Likes from Older To Newer ---->");
                filterTweetsOnLikes.thenAccept(sortedTweet -> sortedTweet.forEach(status -> System.out.println(
                        status.getCreatedAt() + " " + status.getText())));
                TwitterApp.sleepMethod();
                break;

            case 5:
                LocalDate date = LocalDate.of(2018, 3, 10);
                CompletableFuture<List<Status>> filterTweetsOnDate = twitterObj.fetchTweetsOnGivenDate(date);
                System.out.println("<---- Get Likes from Older To Newer ---->");
                filterTweetsOnDate.thenAccept(sortedTweet -> sortedTweet.forEach(status -> System.out.println(
                        status.getCreatedAt() + " " + status.getText())));
                TwitterApp.sleepMethod();
                break;

            default:
                System.out.println("You Have Entered Wrong Choice!!!");
        }
    }
}

