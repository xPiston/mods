package fr.craftechmc.weaponry.items;

public class DispersionWeaponDescriptors
{
    public static class DispesionWeaponDescriptor
    {
        private String  name;
        private String  texture;
        private String  model;
        private int     fireRate;
        private int     bulletNumber;
        private double  fireRange;
        private double  bulletSpeed;
        private double  bulletDispersion;
        private double  zoomCapacity;
        private boolean hasTwoCanon;

        public String getName()
        {
            return this.name;
        }

        public void setName(final String name)
        {
            this.name = name;
        }

        public String getTexture()
        {
            return this.texture;
        }

        public void setTexture(final String texture)
        {
            this.texture = texture;
        }

        public String getModel()
        {
            return this.model;
        }

        public void setModel(final String model)
        {
            this.model = model;
        }

        public int getFireRate()
        {
            return this.fireRate;
        }

        public void setFireRate(final int fireRate)
        {
            this.fireRate = fireRate;
        }

        public int getBulletNumber()
        {
            return this.bulletNumber;
        }

        public void setBulletNumber(final int bulletNumber)
        {
            this.bulletNumber = bulletNumber;
        }

        public double getFireRange()
        {
            return this.fireRange;
        }

        public void setFireRange(final double fireRange)
        {
            this.fireRange = fireRange;
        }

        public double getBulletSpeed()
        {
            return this.bulletSpeed;
        }

        public void setBulletSpeed(final double bulletSpeed)
        {
            this.bulletSpeed = bulletSpeed;
        }

        public double getBulletDispersion()
        {
            return this.bulletDispersion;
        }

        public void setBulletDispersion(final double bulletDispersion)
        {
            this.bulletDispersion = bulletDispersion;
        }

        public double getZoomCapacity()
        {
            return this.zoomCapacity;
        }

        public void setZoomCapacity(final double zoomCapacity)
        {
            this.zoomCapacity = zoomCapacity;
        }

        public boolean isHasTwoCanon()
        {
            return this.hasTwoCanon;
        }

        public void setHasTwoCanon(final boolean hasTwoCanon)
        {
            this.hasTwoCanon = hasTwoCanon;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            long temp;
            temp = Double.doubleToLongBits(this.bulletDispersion);
            result = prime * result + (int) (temp ^ temp >>> 32);
            result = prime * result + this.bulletNumber;
            temp = Double.doubleToLongBits(this.bulletSpeed);
            result = prime * result + (int) (temp ^ temp >>> 32);
            temp = Double.doubleToLongBits(this.fireRange);
            result = prime * result + (int) (temp ^ temp >>> 32);
            result = prime * result + this.fireRate;
            result = prime * result + (this.hasTwoCanon ? 1231 : 1237);
            result = prime * result + (this.model == null ? 0 : this.model.hashCode());
            result = prime * result + (this.name == null ? 0 : this.name.hashCode());
            result = prime * result + (this.texture == null ? 0 : this.texture.hashCode());
            temp = Double.doubleToLongBits(this.zoomCapacity);
            result = prime * result + (int) (temp ^ temp >>> 32);
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
            final DispesionWeaponDescriptor other = (DispesionWeaponDescriptor) obj;
            if (Double.doubleToLongBits(this.bulletDispersion) != Double.doubleToLongBits(other.bulletDispersion))
                return false;
            if (this.bulletNumber != other.bulletNumber)
                return false;
            if (Double.doubleToLongBits(this.bulletSpeed) != Double.doubleToLongBits(other.bulletSpeed))
                return false;
            if (Double.doubleToLongBits(this.fireRange) != Double.doubleToLongBits(other.fireRange))
                return false;
            if (this.fireRate != other.fireRate)
                return false;
            if (this.hasTwoCanon != other.hasTwoCanon)
                return false;
            if (this.model == null)
            {
                if (other.model != null)
                    return false;
            }
            else if (!this.model.equals(other.model))
                return false;
            if (this.name == null)
            {
                if (other.name != null)
                    return false;
            }
            else if (!this.name.equals(other.name))
                return false;
            if (this.texture == null)
            {
                if (other.texture != null)
                    return false;
            }
            else if (!this.texture.equals(other.texture))
                return false;
            if (Double.doubleToLongBits(this.zoomCapacity) != Double.doubleToLongBits(other.zoomCapacity))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "DispesionWeaponDescriptor [name=" + this.name + ", texture=" + this.texture + ", model="
                    + this.model + ", fireRate=" + this.fireRate + ", bulletNumber=" + this.bulletNumber
                    + ", fireRange=" + this.fireRange + ", bulletSpeed=" + this.bulletSpeed + ", bulletDispersion="
                    + this.bulletDispersion + ", zoomCapacity=" + this.zoomCapacity + ", hasTwoCanon="
                    + this.hasTwoCanon + "]";
        }
    }
}
