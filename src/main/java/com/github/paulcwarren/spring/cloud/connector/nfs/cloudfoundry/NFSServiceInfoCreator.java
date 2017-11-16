package com.github.paulcwarren.spring.cloud.connector.nfs.cloudfoundry;

import org.springframework.cloud.connector.nfs.Mode;
import org.springframework.cloud.connector.nfs.NFSServiceInfo;
import org.springframework.cloud.connector.nfs.VolumeMount;
import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides an implementation of {@link CloudFoundryServiceInfoCreator} that detects NFS tagged
 * services bound to a Cloud Foundry deployed application.  This class will be discovered and
 * used automatically in a Spring Cloud Connector enabled application.
 */
public class NFSServiceInfoCreator extends CloudFoundryServiceInfoCreator<NFSServiceInfo> {
    /**
     * Default Constructor.
     * The default behavior of the constructor is to match any Cloud Foundry service with a
     * tag of "nfs"
     */
    public NFSServiceInfoCreator() {
        super(new Tags("nfs"));
    }

    /**
     * Parses Cloud Foundry serviceData Map and returns a normalized NFSServiceInfo instance
     * to be used by Spring Cloud service connector creators.  This Cloud Foundry serviceData
     * Map is created based on the VCAP_SERVICES environment variable.
     *
     * @param serviceData a java.util.Map containing the following required fields:  name,
     *                    containerDir and mode
     * @return {@link NFSServiceInfo NFSServiceInfo}
     */
    @Override
    public NFSServiceInfo createServiceInfo(Map<String, Object> serviceData) {

        String id = (String) serviceData.get("name");

        List<Map<String,String>> volumeMounts = (List<Map<String,String>>) serviceData.get("volume_mounts");

        List<VolumeMount> mounts = new ArrayList<>();
        for (Map<String,String> volumeMount : volumeMounts) {
            String containerDir = volumeMount.get("container_dir");
            String mode = volumeMount.get("mode");
            Mode m = Mode.ReadWrite;
            if ("ro".equals(mode)) {
                m = Mode.ReadOnly;
            }
            VolumeMount mount = new VolumeMount(containerDir, m);
            mounts.add(mount);
        }

        return new NFSServiceInfo(id, mounts.toArray(new VolumeMount[]{}));
    }
}