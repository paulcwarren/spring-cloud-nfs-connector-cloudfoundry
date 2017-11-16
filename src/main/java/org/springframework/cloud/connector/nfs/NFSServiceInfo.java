package org.springframework.cloud.connector.nfs;

import org.springframework.cloud.service.BaseServiceInfo;

/**
 * Provides a normalized implementation of NFS service details which can be gleaned
 * in multiple cloud environments. This class includes all of the necessary parameters
 * to create a valid {@link NFSServiceConnector}.
 */
public class NFSServiceInfo extends BaseServiceInfo {
    private VolumeMount[] volumeMounts;

    /**
     * Default Constructor.
     * The default constructor requires all fields to be provided; however, in some cases fields
     * fields will be provided as null values.
     * @param id the service ID of the provisioned service
     * @param volumeMounts the
     */
    public NFSServiceInfo(String id, VolumeMount[] volumeMounts) {
        super(id);
        this.volumeMounts = volumeMounts;
    }

    @ServiceProperty
    public VolumeMount[] getVolumeMounts() {
        return volumeMounts;
    }
}