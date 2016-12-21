package fr.craftechmc.tracking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Charsets;

public class TrackingManager
{
    public LinkedHashMap<String, PlayerData> players = new LinkedHashMap<>();

    private TrackingManager()
    {

    }

    private static TrackingManager INSTANCE;

    public static final TrackingManager getInstance()
    {
        if (TrackingManager.INSTANCE == null)
            return TrackingManager.INSTANCE = new TrackingManager();
        else
            return TrackingManager.INSTANCE;
    }

    public String formatTime(final long millis)
    {
        final long second = millis / 1000 % 60;
        final long minute = millis / (1000 * 60) % 60;
        final long hour = millis / (1000 * 60 * 60) % 24;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public void writeTrackingData() throws IOException
    {
        for (final PlayerData data : this.players.values())
        {
            if (data.getLastJoinDate() != 0)
            {
                data.getSessions()
                        .add(Pair.of(System.currentTimeMillis() - data.getLastJoinDate(), data.getCurrentSession()));
                data.setLastJoinDate(0);
                data.setCurrentSession(0);
            }
        }
        final File file = new File("trackingdata.dat");
        OutputStreamWriter writer = null;
        try
        {
            writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
            writer.write(this.toString(this.players));
        } finally
        {
            if (writer != null)
                writer.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void readTrackingData() throws ClassNotFoundException, IOException
    {
        this.players = (LinkedHashMap<String, PlayerData>) this
                .fromString(FileUtils.readFileToString(new File("trackingdata.dat")));
    }

    public Object fromString(final String s)
    {
        final byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = null;
        Object o = null;

        try
        {
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            o = ois.readObject();
            ois.close();
        } catch (ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
        }

        return o;
    }

    public String toString(final Serializable o)
    {
        ByteArrayOutputStream baos = null;

        try
        {
            baos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.close();
        } catch (final IOException e)
        {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static class PlayerData implements Serializable
    {
        private static final long              serialVersionUID = 5583247031840133175L;
        private long                           lastJoinDate;
        private int                            currentSession;
        private ArrayList<Pair<Long, Integer>> sessions         = new ArrayList<>();

        public long getLastJoinDate()
        {
            return this.lastJoinDate;
        }

        public void setLastJoinDate(final long lastJoinDate)
        {
            this.lastJoinDate = lastJoinDate;
        }

        public int getCurrentSession()
        {
            return this.currentSession;
        }

        public void setCurrentSession(final int currentSession)
        {
            this.currentSession = currentSession;
        }

        public ArrayList<Pair<Long, Integer>> getSessions()
        {
            return this.sessions;
        }

        public void setSessions(final ArrayList<Pair<Long, Integer>> sessions)
        {
            this.sessions = sessions;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.currentSession;
            result = prime * result + (int) (this.lastJoinDate ^ this.lastJoinDate >>> 32);
            result = prime * result + (this.sessions == null ? 0 : this.sessions.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (this.getClass() != obj.getClass())
                return false;
            final PlayerData other = (PlayerData) obj;
            if (this.currentSession != other.currentSession)
                return false;
            if (this.lastJoinDate != other.lastJoinDate)
                return false;
            if (this.sessions == null)
            {
                if (other.sessions != null)
                    return false;
            }
            else if (!this.sessions.equals(other.sessions))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "PlayerData [lastJoinDate=" + this.lastJoinDate + ", currentSession=" + this.currentSession
                    + ", sessions=" + this.sessions + "]";
        }
    }
}