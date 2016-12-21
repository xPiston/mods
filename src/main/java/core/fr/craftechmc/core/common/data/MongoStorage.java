package fr.craftechmc.core.common.data;

import com.mongodb.*;

public class MongoStorage
{
    public static void initStorage()
    {
        final MongoClient mongoClient = new MongoClient();
        final DBCollection coll = mongoClient.getDB("chat").getCollection("messages");

        final DBCursor cur = coll.find().sort(BasicDBObjectBuilder.start("$natural", 1).get())
                .addOption(Bytes.QUERYOPTION_TAILABLE | Bytes.QUERYOPTION_AWAITDATA);

        System.out.println("== open cursor ==");

        final Runnable task = () ->
        {
            System.out.println("\tWaiting for events");
            while (cur.hasNext())
            {
                final DBObject obj = cur.next();
                System.out.println(obj);

            }
        };
        new Thread(task).start();
    }
}