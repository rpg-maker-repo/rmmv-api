package com.trinary.rpgmaker;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.trinary.rpgmaker.handler.EJBAccessExceptionHandler;
import com.trinary.rpgmaker.resource.PluginBaseResource;
import com.trinary.rpgmaker.resource.PluginResource;
import com.trinary.rpgmaker.resource.RoleResource;
import com.trinary.rpgmaker.resource.TagResource;
import com.trinary.rpgmaker.resource.TokenResource;
import com.trinary.rpgmaker.resource.UserResource;

@ApplicationPath("/")
public class RestApplication extends Application {
	
	public RestApplication() {
		BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/rmmv-api");
        beanConfig.setResourcePackage("com.trinary.rpgmaker.resource");
        beanConfig.setScan(true);
	}

	/* (non-Javadoc)
	 * @see javax.ws.rs.core.Application#getClasses()
	 */
	@Override
	public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<Class<?>>();

        // RMMV resources
        resources.add(PluginResource.class);
        resources.add(PluginBaseResource.class);
        resources.add(UserResource.class);
        resources.add(TokenResource.class);
        resources.add(RoleResource.class);
        resources.add(TagResource.class);

        // Swagger resources
        resources.add(ApiListingResource.class);
        resources.add(SwaggerSerializers.class);
        
        // Exception mappers
        resources.add(EJBAccessExceptionHandler.class);

        return resources;
	}
}