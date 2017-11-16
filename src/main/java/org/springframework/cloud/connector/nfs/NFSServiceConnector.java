package org.springframework.cloud.connector.nfs;

import lombok.Getter;

/**
 * Provides an array of volume mounts for a named NFS service.
 */
@Getter
public class NFSServiceConnector {
    private VolumeMount[] volumeMounts;

    /**
     * Generates an NFSServiceConnector with AWS SDK S3 client, the S3 endpoint and a baseUrl for
     * public access to the same.  In most cases the endpoint and baseUrl are the same, but there
     * there are cases, when they will be different.
     *
     * @param volumeMounts an array of {@link VolumeMount}s that can be used by an application as persistent file
     *                     storage
     */
    public NFSServiceConnector(VolumeMount[] volumeMounts) {
        this.volumeMounts = volumeMounts;
    }

    public VolumeMount[] getVolumeMounts() {
        return volumeMounts;
    }
}