package fr.craftechmc.environment.common.fog;

import java.util.LinkedList;

import fr.craftechmc.admin.common.fogpath.ANameGetter;
import fr.craftechmc.core.math.Vector3d;

/**
 * A fog path is a schema of the possible moves of a fog entity in the world
 * Created by arisu on 24/07/2016.
 */
public class FogPath extends ANameGetter
{
    private final LinkedList<Vector3d> checkpoints = new LinkedList<>();

    public void addCheckpoint(final double x, final double y, final double z)
    {
        this.addCheckpoint(new Vector3d(x, y, z));
    }

    public void addCheckpoint(final Vector3d coords)
    {
        synchronized (this.checkpoints)
        {
            if (this.checkpoints.size() > 0)
            {
                final Vector3d lastCheckpoint = this.checkpoints.get(this.checkpoints.size() - 1);
                if (lastCheckpoint.getX() == coords.getX() && lastCheckpoint.getY() == coords.getY()
                        && lastCheckpoint.getZ() == coords.getZ())
                    return;
            }
            this.checkpoints.add(coords);
        }
    }

    public LinkedList<Vector3d> getCheckpoints()
    {
        return this.checkpoints;
    }
}