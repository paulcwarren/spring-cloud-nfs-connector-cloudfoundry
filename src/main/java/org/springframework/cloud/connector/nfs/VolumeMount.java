package org.springframework.cloud.connector.nfs;

import lombok.Getter;

@Getter
public class VolumeMount {

    private String containerDir;
    private Mode mode;

    public VolumeMount(String containerDir, Mode mode) {
        this.containerDir = containerDir;
        this.mode = mode;
    }

    public String getContainerDir() {
        return containerDir;
    }

    public Mode getMode() {
        return mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VolumeMount that = (VolumeMount) o;

        if (containerDir != null ? !containerDir.equals(that.containerDir) : that.containerDir != null) return false;
        return mode == that.mode;
    }

    @Override
    public int hashCode() {
        int result = containerDir != null ? containerDir.hashCode() : 0;
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        return result;
    }
}
