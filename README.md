Twitter-Gnip-Stream
===================

This Demo is build as a early version Prototype for ReduceData Inc http://www.reducedata.com/

This Demo collects large Datasets from the Gnip Twitter Stream and uses MySql as the backend Database.

Getting Started with Gnip : http://support.gnip.com/

Repostiory Description:

*Package gnipparser has a server that would connect to the Gnip stream and the parser would parse the Json, gets the required data and writes them to a  MySql Database.

*Package twitterbrowser is a Play framework repository that reads the required values from the database and renders insightfull stats using highcharts, Google charts. 

Charts used in the Template design inclues:
1) Line chart -- Tweet Vs Retweet comparison
2) Stacked Bar chart -- Tweet Vs Retweet Funnel
3) Pie chart -- Anyalysis on the Platform where the Tweets Came from
4) Geo Chart -- Geo mapping based on the origin of the tweets.

Connecting to gnipstream uses the source code available at: https://github.com/gnip/support/tree/master/Premium%20Stream%20Connection/Java

Note:  Server and Parser are in the early versions, subsequently gnip might forcefully disconnect the stream if the bandwidth fails to meet the flow in of large payloads.

