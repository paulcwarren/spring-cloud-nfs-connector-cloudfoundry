# spring-cloud-nfs-connector-cloudfoundry
[![Build Status](https://travis-ci.org/paulcwarren/spring-cloud-nfs-connector-cloudfoundry.svg?branch=master)](https://travis-ci.org/paulcwarren/spring-cloud-nfs-connector-cloudfoundry)
[![Apache Version 2 Licence](http://img.shields.io/:license-Apache%20v2-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Spring Cloud NFS Service Connectors for [CloudFoundry Volume Services](https://docs.cloudfoundry.org/devguide/services/using-vol-services.html).

When running in CloudFoundry this Spring Cloud Connector allows you to easily configure your application to write to reliable, non-ephemeral nfs volumes. 

### Add a connector to your project

The maven coordinates for this Spring Cloud Connector are as follows:

```xml
<dependency>
  <groupId>com.github.paulcwarren</groupId>
  <artifactId>spring-cloud-nfs-connector-cloudfoundry</artifactId>
  <version>1.0.0</version>
</dependency>
```

In your Spring application create an `@Configuration` class that extends `AbstractCloudConfig` to create a `NFSConnector` bean.  This bean provides access to the nfs volumes bund to the application. 

```java
@Configuration
public class NFSConfig extends AbstractCloudConfig {

	@Bean
    	public NFSServiceConnector nfs() {
        	return connectionFactory().service(NFSServiceConnector.class);
    	}
}
```

Usage example:

```java
@Component
public class ExampleNFS {

    @Autowired
    NFSServiceConnector nfs;

    public void writeFile(String name, InputStream contents){
    	File file = new File(nfs.getVolumeMounts()[0].getContainerDir(), name);
        IOUtils.copyToFile(contents, file);
    }

    public InputStream readFile(String name){
    	File file = new File(nfs.getVolumeMounts()[0].getContainerDir(), name);
        return IOUtils.openInputStream(file);
    }
}
```

### Deploy and run

#### Cloud Foundry

1. Create an NFS service from the marketplace using the [NFS Service Broker](http://github.com/cloudfoundry/nfs-volume-release).
2. Push your app with `cf push`
3. After the app has been pushed bind your new created service to your app (e.g: `cf bs nameofmyapp nameofmyservice`)
4. Restage your app: `cf restage nameofmyapp`

